package edu.upenn.cis350.hwk2.data_management;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copy;

/**
 * Created by He on 2/19/2016.
 */
public class KashayaPlayer {

    /**
     * Where the files will be downloaded to in the phone
     */
    private static final String DOWNLOAD_DIRECTORY = "kashaya-downloadables";

    /**
     * If there is a password on the zip files (words.zip/sounds.zip/images.zip) downloaded from the web, it will use this
     */
    private static final String ZIP_PASSWORD = "none";

    /**
     * The links containing all of the files we need to download into the Phone for the app to work
     */
    private static final String[] FILE_LINKS =
            {"http://www.ling.upenn.edu/~gene/Kashaya/AppSounds/words.zip",
                    "http://www.ling.upenn.edu/~gene/Kashaya/AppSounds/sounds.zip",
                    "http://www.ling.upenn.edu/~gene/Kashaya/AppSounds/images.zip"};

    /**
     * Singleton of KashayaPlayer
     * We don't want to do fishy monkey banana business with downloading and potentially overwriting files
     * So we have a singleton to control instances
     */
    private static KashayaPlayer playerInstance = null;

    /**
     * Optional activity, in case there needs to be interaction between Activity - KashayaPlayer - ResourceLoader
     */
    private Activity activity = null;

    /**
     * Singleton of ResourceLoader for similar reasons to KashayaPlayer
     */
    private static ResourceLoader resourceLoader = null;

    /**
     * To make sure that we are not playing multiple sounds on top of each other,
     * we will control the sound playing with a lock and only play the sound when the lock is freed
     * (when the previous sound is done playing)
     */
    private Semaphore playLock = new Semaphore(1);

    /**
     * MediaPlayer
     */
    private MediaPlayer mediaPlayer = null;
    private PrepareMediaTask currentTask = null;

    /**
     * What to do when the sound is done playing
     */
    private MediaPlayer.OnCompletionListener onCompletionCloser =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mp != null) {
                        try {
                            //mp.stop();
                            //mp.prepare();
                            mp.stop();
                            mp.reset();
                            playLock.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

    private KashayaPlayer(Activity activity) {
        this.activity = activity;
        resourceLoader = new ResourceLoader(activity, false);
    }

    /**
     * Is there a sound currently playing?
     * @return
     */
    public static boolean isPlaying() {
        if (playerInstance != null) {
            return playerInstance.playLock.availablePermits() == 0;
        }
        return false;
    }

    /**
     * Initializing the KashayaPlayer, MainActivity handles this
     * @param activity
     */
    public static void initialize(Activity activity) {
        if (playerInstance == null && activity != null) {
            playerInstance = new KashayaPlayer(activity);
            System.out.println("Initialized KashayaPlayer");
        }
    }

    /**
     * Get rid of the MediaPlayer instance by releasing it and setting it up for garbage collection
     * This is because the MediaPlayer being active heavily consumes resources
     */
    public void dispose() {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * Is the KashayaPlayer initialized?
     * @return
     */
    public static boolean isInitialized() {
        return playerInstance != null;
    }

    /**
     * Returns the singleton
     * @return
     */
    public static KashayaPlayer getPlayerInstance() {
        if (playerInstance == null) {
            throw new IllegalStateException("player has not been initialized yet");
        }
        return playerInstance;
    }

    /**
     * Plays the media
     * @param name The name of the file
     * @param parameters Where do you want the method to look for the sound files? Null/default is look everywhere
     */
    public void playMedia(String name, PlayerParameter... parameters) {
        System.out.println("Request for playing media " + name);

        PlayerParameter[] selectedParams =
                parameters == null || parameters.length == 0 ? PlayerParameter.values() :
                        parameters;

        Uri foundUri = null;
        for (PlayerParameter param : selectedParams) {
            switch (param) {
                case SEARCH_SOUND_LIST:
                    foundUri = resourceLoader.getSoundResource(name);
                    break;
                case SEARCH_WORD_LIST:
                    foundUri = resourceLoader.getWordResource(name);
                    break;
                case SEARCH_USER_RECORDING_LIST:
                    foundUri = resourceLoader.getUserRecordedResource(name);
                    break;
            }
            if (foundUri != null) {
                break;
            }
        }

        if (foundUri == null) {
            Log.d("KashayaPlayer", "Cannot find resource " + name);
            return;
        }
        currentTask = new PrepareMediaTask(foundUri, name);
        currentTask.execute(mediaPlayer);
    }

    public static void interruptKashayaPlayer() {
        if (isPlaying() && playerInstance.currentTask != null && playerInstance.currentTask.cancel(true)) {
            System.out.println("Cancelled playing sound task");
        }
    }

    /**
     * Get singleton of ResourceLoader
     * @return
     */
    public static ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * Playing sounds requires a lot of delays and such
     * We do not want to have it in the main thread because it will stall too many things
     * so we start a new AsyncTask which is similar to a thread
     */
    private class PrepareMediaTask extends AsyncTask<MediaPlayer, Integer, Long> {

        private Uri uri = null;
        private String soundName = null;

        private PrepareMediaTask(Uri uri, String name) {
            this.uri = uri;
            soundName = name;
        }

        @Override
        protected Long doInBackground(MediaPlayer... mps) {

            try {
                if (!playLock.tryAcquire(Integer.MAX_VALUE, TimeUnit.MILLISECONDS)) {
                    System.out.println("Current sound already playing");
                    return 0L;
                }
            } catch (InterruptedException e) {
            }


            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnCompletionListener(onCompletionCloser);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            try {
                mediaPlayer.setDataSource(activity, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return 0L;
        }

        @Override
        public void onCancelled() {
            if (mediaPlayer != null) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }

        @Override
        protected void onPostExecute(Long result) {
        }
    }


    /*
     * Created by He on 2/15/2016.
     */
    public static class ResourceLoader {

        /**
         * The default file extension
         */
        public static final String FILE_EXTENSION = "mp4";

        /**
         * The name of the file directory where the actual extracted sound files are stored
         */
        private String filedir = null;

        /**
         * The files downloaded are zips, but we can't really do much with them
         * So we only download into the cache and extract them into 'firedir'
         * Files in the cache are easily/semi-frequently disposed of automatically
         * So it is not wasteful
         */
        private String cachedir = null;

        /**
         * File name to word Uri map
         */
        private Map<String, Uri> wordUris = null;

        /**
         * Sound name to sound Uri map
         */
        private Map<String, Uri> soundUris = null;

        /**
         * The directory where word files are stored
         */
        private String wordsDir = null;

        /**
         * The directory where sound files are stored
         */
        private String soundsDir = null;

        /**
         * The directory where image files are stored
         */
        private String imagesDir = null;

        /**
         * The base directory where the resources are stored
         * The image, words, sounds, user_recorded directory are subdirectories of base directory
         */
        private String baseDir = null;

        /**
         * The directory where the user recorded sounds are stored
         */
        private String userRecordedDir = null;

        /**
         * Activity instance sent from KashayaPlayer, which can be used to communicate back to
         * MainActivity
         */
        private Activity activity = null;

        /**
         * Creates an instance of ResourceLoader
         * @param activity
         * @param dl Do we want to force a download?
         *           Otherwise we will simply check to see if the files are there, and if they are
         *           we will not re-download/force-download
         */
        private ResourceLoader(Activity activity, boolean dl) {
            this.activity = activity;
            this.filedir = activity.getFilesDir().getAbsolutePath();
            this.cachedir = activity.getCacheDir().getAbsolutePath();
            wordUris = new HashMap<>();
            soundUris = new HashMap<>();
            baseDir = filedir + File.separator + DOWNLOAD_DIRECTORY;
            wordsDir = baseDir + File.separator + "words";
            soundsDir = baseDir + File.separator + "sounds";
            imagesDir = baseDir + File.separator + "images";
            userRecordedDir = baseDir + File.separator + "user_recordings";
            File dir = new File(userRecordedDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dl || !hasDirectories()) {
                Log.d("ResourceLoader", "No files found, downloading required files!");
                downloadRequiredFiles();
            } else {
                try {
                    Log.d("ResourceLoader", "Files found, proceeding");
                    loadMP3Uri();
                    listFiles();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private ResourceLoader(Activity act) {
            this(act, false);
        }

        /**
         * Gets the file name (with no extensions) to Uri map
         * @return
         */
        public Map<String, Uri> getWordMap() {
            return wordUris;
        }

        /**
         * Gets the word Uri resource given the file name with extensions removed
         * @param name
         * @return
         */
        public Uri getWordResource(String name) {
            return wordUris.get(name);
        }

        /**
         * Gets the sound Uri resource given the file name with extensions removed
         * @param name
         * @return
         */
        public Uri getSoundResource(String name) {
            return soundUris.get(name);
        }

        /**
         * Gets the user recorded resource given the file name with extensions removed
         * @param name
         * @return
         */
        public Uri getUserRecordedResource(String name) {
            Map<String, Uri> map = getUserRecordedURIs();
            return map.get(name);
        }

        /**
         * Deletes the sound given the name
         * This method is a bit adaptive in that if you get the extension incorrect,
         * it will still delete it because it looks for file names that are 'close enough' in the name
         * @param name
         */
        public void deleteUserSound(String name) {
            File dir = new File(userRecordedDir);
            List<String> deletionCandidates = new ArrayList<String>();
            for (File file : dir.listFiles()) {
                String fileNameIter = file.getName();
                if (fileNameIter.equals(name) || fileNameIter.equals(name + "." + FILE_EXTENSION)) {
                    file.delete();
                    return;
                }

                if (fileNameIter.contains(name)) {
                    deletionCandidates.add(fileNameIter);
                }
            }

            if (deletionCandidates.isEmpty()) {
                System.out.println("unsuccessfully deleted " + name);
                return;
            }

            int ld = Integer.MAX_VALUE;
            String consideraton = deletionCandidates.get(0);
            for (String candidate : deletionCandidates) {
                int currLevenhsteinDistance = levenshteinDistance(candidate, name);
                if (currLevenhsteinDistance < ld) {
                    consideraton = candidate;
                    ld = currLevenhsteinDistance;
                }
            }
            if (ld <= FILE_EXTENSION.length() + 1) {
                File del = new File(userRecordedDir + File.separator + consideraton);
                del.delete();
            }
        }

        /**
         * gets the image resource given the name of the image file WITH the extensions attached
         * @param name
         * @return
         */
        public Bitmap getImage(String name) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagesDir + File.separator + name, options);
            return bitmap;
        }

        /**
         * Gets the name of the directory where we store user recorded sounds
         * @return
         */
        public String getUserRecordedDir() {
            return userRecordedDir;
        }

        /**
         * Gets the most updated version of the user recorded Uris
         * @return
         */
        public Map<String, Uri> getUserRecordedURIs() {
            Map<String, Uri> nameUriMap = new HashMap<>();
            File dir = new File(getUserRecordedDir());
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String fileName = file.getName();
                    String[] tokens = fileName.split("\\.");
                    String abbrevFileName = fileName;
                    if (tokens.length > 1) {
                        abbrevFileName = "";
                        String extension = tokens[tokens.length - 1];
                        for (int i = 0; i < tokens.length - 1; i++) {
                            abbrevFileName +=
                                    (i == tokens.length - 2) ? (tokens[i]) : (tokens[i] + ".");
                        }
                    }
                    nameUriMap.put(abbrevFileName, Uri.fromFile(file));
                }
            }
            return nameUriMap;
        }

        /**
         * Downloads the required files
         */
        public void downloadRequiredFiles() {
            Toast.makeText(activity, "Downloading required files, please wait until download is over before using application!", Toast.LENGTH_LONG).show();
            for (String link : FILE_LINKS) {
                try {
                    String[] filetoken = link.split("/");
                    String zipName = filetoken[filetoken.length - 1];

                    File cacheDir = new File(cachedir, DOWNLOAD_DIRECTORY);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdir();
                    }
                    URL url = new URL(link);
                    File file = new File(cacheDir.getAbsolutePath(), zipName);
                    new DownloadFilesTask(activity, cacheDir.getAbsolutePath(), zipName)
                            .execute(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * More or less for debug purposes, prints what is currently there
         */
        public void printLoadedKeys() {
            for (String key : soundUris.keySet()) {
                System.out.println("KEY: " + key);
            }
        }

        /**
         * The minimum insertions, deletions, modificiations required to transform one string into the other
         * https://en.wikipedia.org/wiki/Levenshtein_distance
         * @param lhs
         * @param rhs
         * @return
         */
        private int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
            int len0 = lhs.length() + 1;
            int len1 = rhs.length() + 1;

            int[] cost = new int[len0];
            int[] newcost = new int[len0];

            for (int i = 0; i < len0; i++) cost[i] = i;

            for (int j = 1; j < len1; j++) {
                newcost[0] = j;
                for (int i = 1; i < len0; i++) {
                    int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
                    int cost_replace = cost[i - 1] + match;
                    int cost_insert = cost[i] + 1;
                    int cost_delete = newcost[i - 1] + 1;
                    newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
                }

                int[] swap = cost;
                cost = newcost;
                newcost = swap;
            }
            return cost[len0 - 1];
        }

        private boolean hasDirectories() {

            File fileImages = new File(imagesDir);
            File fileWords = new File(wordsDir);
            File fileSounds = new File(soundsDir);

            return fileImages.exists() && fileWords.exists() && fileSounds.exists();
        }

        private void listFiles() {
            File fileImages = new File(imagesDir);
            File fileWords = new File(wordsDir);
            File fileSounds = new File(soundsDir);

            if (!hasDirectories()) {
                System.out.println("Missing directories");
                return;
            }

            File[] fileArr = new File[] {fileImages, fileWords, fileSounds};
            String[] fileMessage = new String[] {"In Images:", "In Words: ", "In Sounds: "};

            try {
                for (int i = 0; i < fileArr.length; i++) {
                    System.out.println(fileMessage[i]);
                    for (File f : fileArr[i].listFiles()) {
                        System.out.println(f.getName());
                        if (f.isDirectory()) {
                            for (File subf : f.listFiles()) {
                                System.out.println(subf.getAbsolutePath());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Populates the map with the Uri files
         * @throws FileNotFoundException
         */
        private void loadMP3Uri() throws FileNotFoundException {
            File wordsDirFile = new File(wordsDir);
            File soundsDirFile = new File(soundsDir);
            File imagesDirFile = new File(imagesDir);

            if (!wordsDirFile.exists() || !soundsDirFile.exists() || !wordsDirFile.isDirectory() ||
                    !soundsDirFile.isDirectory() || !imagesDirFile.exists() ||
                    !imagesDirFile.isDirectory()) {
                throw new FileNotFoundException("required files not found");
            }
            for (File file : wordsDirFile.listFiles()) {
                String fileName = file.getName();
                if (fileName.startsWith(".") || file.isDirectory()) {
                    continue;
                }
                wordUris.put(fileName.substring(0, fileName.length() - 7), Uri.fromFile(file));


            }

            for (File file : soundsDirFile.listFiles()) {
                String fileName = file.getName();
                if (fileName.startsWith(".") || file.isDirectory()) {
                    continue;
                }
                soundUris.put(fileName.substring(0, fileName.length() - 4), Uri.fromFile(file));

            }
        }

        /**
         * A thread to download the files on the side, so we do not cause large amounts of lag
         * for the user
         */
        private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

            private Activity activity = null;
            private String directory = null;
            private String fileName = null;

            private DownloadFilesTask(Activity activity, String directory, String fileName) {
                this.directory = directory;
                this.fileName = fileName;
                this.activity = activity;
            }

            @Override
            protected Long doInBackground(URL... urls) {
                int count = urls.length;
                long totalSize = 0;
                for (int i = 0; i < count; i++) {
                    try {
                        // sets up the files for download
                        File file = new File(directory + File.separator + fileName);

                        if (!file.exists()) {
                            System.out.println("Creating file " + file.getAbsolutePath());
                            file.createNewFile();
                        }
                        // downloads from server to the file
                        FileUtils.copyURLToFile(urls[i], file);


                        publishProgress((int) ((i / (float) count) * 100));
                        // Escape early if cancel() is called
                        if (isCancelled()) break;

                        // begins extracting from the cache to the file directory
                        // this is nice because we don't have to worry about cleaning the cache
                        // since it is freed regularly by the operating system on-demand
                        ZipFile zipFile = new ZipFile(file.getAbsolutePath());

                        try {
                            String saveDirectoryName = fileName.contains("words") ? wordsDir :
                                    fileName.contains("sounds") ? soundsDir : imagesDir;
                            // String saveDirectoryName = baseDir;
                            Enumeration<? extends ZipEntry> entries = zipFile.entries();
                            File saveDirectory = new File(saveDirectoryName);
                            if (saveDirectory.exists() && saveDirectory.isDirectory()) {
                                FileUtils.deleteDirectory(saveDirectory);
                            }
                            while (entries.hasMoreElements()) {
                                // extracting each of the files from the zip
                                ZipEntry entry = entries.nextElement();

                                File entryDestination = new File(baseDir, entry.getName());
                                if (entry.isDirectory()) {
                                    entryDestination.mkdirs();
                                } else {
                                    entryDestination.getParentFile().mkdirs();
                                    InputStream in = zipFile.getInputStream(entry);
                                    OutputStream out = new FileOutputStream(entryDestination);
                                    copy(in, out);
                                    closeQuietly(in);
                                    out.close();
                                }
                            }
                        } finally {
                            zipFile.close();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("ResourceLoader", "Downloaded files " + fileName);
                try {
                    if (hasDirectories()) {
                        loadMP3Uri();
                        listFiles();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return totalSize;
            }

            @Override
            protected void onProgressUpdate(Integer... progress) {
                // activity.setProgressPercent(progress[0]);
            }

            /**
             * What to do once the download is done
             * @param result
             */
            @Override
            protected void onPostExecute(Long result) {
                Toast.makeText(activity, "Finished downloading files, the application is ready to use!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
