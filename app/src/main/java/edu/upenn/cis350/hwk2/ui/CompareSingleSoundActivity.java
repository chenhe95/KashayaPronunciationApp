package edu.upenn.cis350.hwk2.ui;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import edu.upenn.cis350.hwk2.R;
import edu.upenn.cis350.hwk2.data_management.KashayaPlayer;
import edu.upenn.cis350.hwk2.data_management.PlayerParameter;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompareSingleSoundActivity extends AppCompatActivity {

    private KashayaPlayer player = KashayaPlayer.getPlayerInstance();
    private String word;
    private String ownRecording;
    private Button recordNewButton;
    private Thread currentlyExecuting = null;

    private AtomicBoolean recording = new AtomicBoolean(false);

    private volatile MediaRecorder recorder = null;

    public boolean isRecording() {return recording.get();}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_single_sound);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Record and compare");
        setSupportActionBar(toolbar);

        // get word you are comparing with
        TextView title = (TextView) findViewById(R.id.soundText);
        String delimeter = " - ";
        word = this.getIntent().getStringExtra("word");

        // set word you are comparing with
        if (word.contains(delimeter)) {
            String[] tokens = word.split(delimeter);
            title.setText(tokens[1]);
        } else {
            title.setText(word);
        }


        ownRecording = word + "-recording";

    }

    public void playBothOnClick (View v) {
        KashayaPlayer.getPlayerInstance().playMedia(word);
        player.playMedia(ownRecording, PlayerParameter.SEARCH_USER_RECORDING_LIST);

    }

    public void recordNewButtonOnClick(View v) {

        recordNewButton = (Button) findViewById(R.id.recordNewButton);
            //now recording
        System.out.println("activating record sound button: recording state " + recording.get());
        if (!isRecording()) {

            // set image
            ImageView status = (ImageView) findViewById(R.id.status);
            status.setImageResource(R.drawable.ic_record_voice_over_green_24dp);

            // start recording if we are not
            recordNewButton.setText("RECORDING...");
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(
                    player.getResourceLoader().getUserRecordedDir() + File.separator + ownRecording +
                            "." + KashayaPlayer.ResourceLoader.FILE_EXTENSION);
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();   // Recording is now started
            recording.set(true);
            recordNewButton.setText("Recording ...");
        } else {

            // set image
            ImageView status = (ImageView) findViewById(R.id.status);
            status.setImageDrawable(null);

            // if we are recording, then stop
            recordNewButton.setText("RECORD NEW SOUND");
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }
            if (currentlyExecuting != null) {
                currentlyExecuting.interrupt();
                currentlyExecuting = null;
            }
            recordNewButton.setText("Record");
            recording.set(false);
        }
    }

    public void playOnlyUserRecordedVersionOnClick(View v) {

        player.playMedia(ownRecording, PlayerParameter.SEARCH_USER_RECORDING_LIST);
    }

    public void playOnlyOriginalVersionOnClick(View v) {
        KashayaPlayer.getPlayerInstance().playMedia(word);
    }

    @Override
    public void onStop() {

        if (isRecording()) {
            if (recorder != null) {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            }
            if (currentlyExecuting != null) {
                currentlyExecuting.interrupt();
                currentlyExecuting = null;
            }
        } else {
            KashayaPlayer.interruptKashayaPlayer();
        }

        super.onStop();
    }
}
