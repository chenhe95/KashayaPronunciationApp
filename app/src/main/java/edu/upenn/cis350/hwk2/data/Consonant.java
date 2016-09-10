package edu.upenn.cis350.hwk2.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Consonant extends Sound {
    public Consonant(int id, String name, String className, String form) {
        super(id, name, className, form);
    }

    public static final Set<String> CONSONANTCLASS = new HashSet<>(Arrays.asList("labial",
            "dental", "alveolar", "palatal", "velar", "uvular", "voiced", "fricatives",
            "glottals", "sonorants", "glides"));

    public static Consonant[] CONSONANTS = new Consonant[] {
            // labial
            new Consonant(1, "p", "labial", "plain"),
            new Consonant(2, "pʰ", "labial", "aspirated"),
            new Consonant(3, "pʼ", "labial", "ejective"),
            // dental
            new Consonant(4, "t", "dental", "plain"),
            new Consonant(5, "tʰ", "dental", "aspirated"),
            new Consonant(6, "tʼ", "dental", "ejective"),
            // alveolar
            new Consonant(7, "ṭ", "alveolar", "plain"),
            new Consonant(8, "ṭʰ", "alveolar", "aspirated"),
            new Consonant(9, "ṭʼ", "alveolar", "ejective"),
            // palatal
            new Consonant(10, "c", "palatal", "plain"),
            new Consonant(11, "cʰ", "palatal", "aspirated"),
            new Consonant(12, "cʼ", "palatal", "ejective"),
            // velar
            new Consonant(13, "k", "velar", "plain"),
            new Consonant(14, "kʰ", "velar", "aspirated"),
            new Consonant(15, "kʼ", "velar", "ejective"),
            // uvular
            new Consonant(16, "q", "uvular", "plain"),
            new Consonant(17, "qʰ", "uvular", "aspirated"),
            new Consonant(18, "qʼ", "uvular", "ejective"),
            // voiced
            new Consonant(19, "b", "voiced", "plain"),
            new Consonant(20, "d", "voiced", "aspirated"),
            new Consonant(0, "", "", ""),
            // fricatives
            new Consonant(21, "s", "fricatives", ""),
            new Consonant(22, "sʼ", "fricatives", ""),
            new Consonant(23, "š", "fricatives", ""),
            // glottals
            new Consonant(24, "h", "glottals", ""),
            new Consonant(25, "ʔ", "glottals", ""),
            new Consonant(0, "", "", ""),
            // sonorants
            new Consonant(26, "m", "sonorants", ""),
            new Consonant(27, "n", "sonorants", ""),
            new Consonant(28, "l", "sonorants", ""),
            // glides
            new Consonant(29, "w", "glides", ""),
            new Consonant(30, "y", "glides", ""),
            new Consonant(0, "", "", "")
    };

//    public static Consonant[] CONSONANTS = new Consonant[] {
//            // labial
//            new Consonant(1, "pa", "labial", "plain"),
//            new Consonant(2, "pʰa", "labial", "aspirated"),
//            new Consonant(3, "pʼa", "labial", "ejective"),
//            // dental
//            new Consonant(4, "ta", "dental", "plain"),
//            new Consonant(5, "tʰa", "dental", "aspirated"),
//            new Consonant(6, "tʼa", "dental", "ejective"),
//            // alveolar
//            new Consonant(7, "ṭa", "alveolar", "plain"),
//            new Consonant(8, "ṭʰa", "alveolar", "aspirated"),
//            new Consonant(9, "ṭʼa", "alveolar", "ejective"),
//            // palatal
//            new Consonant(10, "ca", "palatal", "plain"),
//            new Consonant(11, "cʰa", "palatal", "aspirated"),
//            new Consonant(12, "cʼa", "palatal", "ejective"),
//            // velar
//            new Consonant(13, "ka", "velar", "plain"),
//            new Consonant(14, "kʰa", "velar", "aspirated"),
//            new Consonant(15, "kʼa", "velar", "ejective"),
//            // uvular
//            new Consonant(16, "qa", "uvular", "plain"),
//            new Consonant(17, "qʰa", "uvular", "aspirated"),
//            new Consonant(18, "qʼa", "uvular", "ejective"),
//            // voiced
//            new Consonant(19, "ba", "voiced", "plain"),
//            new Consonant(20, "da", "voiced", "aspirated"),
//            new Consonant(0, "", "", ""),
//            // fricatives
//            new Consonant(21, "sa", "fricatives", ""),
//            new Consonant(22, "sʼa", "fricatives", ""),
//            new Consonant(23, "ša", "fricatives", ""),
//            // glottals
//            new Consonant(24, "ha", "glottals", ""),
//            new Consonant(25, "ʔa", "glottals", ""),
//            new Consonant(0, "", "", ""),
//            // sonorants
//            new Consonant(26, "ma", "sonorants", ""),
//            new Consonant(27, "na", "sonorants", ""),
//            new Consonant(28, "la", "sonorants", ""),
//            // glides
//            new Consonant(29, "wa", "glides", ""),
//            new Consonant(30, "ya", "glides", ""),
//            new Consonant(0, "", "", "")
//    };

//    public static Consonant[] PLAIN = new Consonant[] {
//            new Consonant(1, "pa"),
//            new Consonant(4, "ta"),
//            new Consonant(7, "ṭa"),
//            new Consonant(10, "ca"),
//            new Consonant(13, "ka"),
//            new Consonant(16, "qa"),
//            new Consonant(19, "ba"),
//            new Consonant(21, "sa"),
//            new Consonant(24, "ha"),
//            new Consonant(26, "ma"),
//            new Consonant(29, "wa")
//    };
//
//    public static Consonant[] ASPIRATED = new Consonant[] {
//            new Consonant(2, "pʰa"),
//            new Consonant(5, "tʰa"),
//            new Consonant(8, "ṭʰa"),
//            new Consonant(11, "cʰa"),
//            new Consonant(14, "kʰa"),
//            new Consonant(17, "qʰa"),
//            new Consonant(20, "da"),
//            new Consonant(22, "sʼa"),
//            new Consonant(25, "ʔa"),
//            new Consonant(27, "na"),
//            new Consonant(30, "ya")
//    };
//
//    public static Consonant[] EJECTIVE = new Consonant[] {
//            new Consonant(3, "pʼa"),
//            new Consonant(6, "tʼa"),
//            new Consonant(9, "ṭʼa"),
//            new Consonant(12, "cʼa"),
//            new Consonant(15, "kʼa"),
//            new Consonant(18, "qʼa"),
//            new Consonant(23, "ša"),
//            new Consonant(28, "la")
//    };

    public static boolean contains(String name) {
        for (int i = 0; i < CONSONANTS.length; i++) {
            if (CONSONANTS[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static List<Sound> getSoundsByClass(String className) {
        return getSoundsByClass(className, CONSONANTS);
    }

    public static List<Sound> getSoundsByForm(String formName) {
        return getSoundsByForm(formName, CONSONANTS);
    }
}
