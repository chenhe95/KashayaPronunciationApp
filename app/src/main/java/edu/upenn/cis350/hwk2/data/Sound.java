package edu.upenn.cis350.hwk2.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Sound {
    private String name;
    private int id;
    private String className;
    private String form;

    public Sound(int id, String name, String className, String form) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.form = form;
    }

    public String getName() {
        return name;
    }

   static List<Sound> getSoundsByClass(String className, Sound[] searchSpace) {
        List<Sound> soundsFound = new ArrayList<>();
        for (Sound sound: searchSpace) {
            if (className.equals(sound.getClassName())) {
                soundsFound.add(sound);
            }
        }
        return soundsFound;
    }

   static List<Sound> getSoundsByForm(String formName, Sound[] searchSpace) {
        List<Sound> soundsFound = new ArrayList<>();
        for (Sound sound : searchSpace) {
            if (formName.equals(sound.getForm())) {
                soundsFound.add(sound);
            }
        }
        return soundsFound;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getForm() {
        return form;
    }

    @Override
    public String toString() {
        return getName();
    }
}
