package edu.upenn.cis350.hwk2.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vowel extends Sound {
    public Vowel(int id, String name, String className, String form) {
        super(id, name, className, form);
    }

    public static final Set<String> VOWELCLASS = new HashSet<>(Arrays.asList("high_front",
            "mid_front", "low", "mid_back", "high_back"));

    public static Vowel[] VOWELS = new Vowel[] {
            new Vowel(1, "i", "high_front", "short"),
            new Vowel(2, "i·", "high_front", "long"),

            new Vowel(3, "e", "mid_front", "short"),
            new Vowel(4, "e·", "mid_front", "long"),

            new Vowel(5, "a", "low", "short"),
            new Vowel(6, "a·", "low", "long"),

            new Vowel(7, "o", "mid_back", "short"),
            new Vowel(8, "o·", "mid_back", "long"),

            new Vowel(9, "u", "high_back", "short"),
            new Vowel(10, "u·", "high_back", "long")
    };

//    public static Vowel[] SHORT = new Vowel[] {
//            new Vowel(1, "i"),
//            new Vowel(3, "e"),
//            new Vowel(5, "a"),
//            new Vowel(7, "o"),
//            new Vowel(9, "u")
//    };
//
//    public static Vowel[] LONG = new Vowel[] {
//            new Vowel(2, "i·"),
//            new Vowel(4, "e·"),
//            new Vowel(6, "a·"),
//            new Vowel(8, "o·"),
//            new Vowel(10, "u·")
//    };

    public static boolean contains(Vowel[] vs, String name) {
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static List<Sound> getSoundsByClass(String className) {
        return getSoundsByClass(className, VOWELS);
    }

    public static List<Sound> getSoundsByForm(String form) {
        return getSoundsByForm(form, VOWELS);
    }
}
