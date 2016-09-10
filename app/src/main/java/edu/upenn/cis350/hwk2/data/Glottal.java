package edu.upenn.cis350.hwk2.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fran on 3/4/2016.
 */
public class Glottal extends Sound {
    public Glottal(int id, String name, String className, String form) {
        super(id, name, className, form);
    }


    public static Glottal[] GLOTTALS = new Glottal[] {
            new Glottal(1, "hp", "labial", "plain"),
            new Glottal(2, "ʔp", "labial", "plain"),
            new Glottal(3, "hpʰ", "labial", "aspirated"),
            new Glottal(4, "ʔpʼ", "labial", "ejective"),
            new Glottal(5, "ʔb", "labial", "voiced"),
            new Glottal(0, "", "", ""),

            new Glottal(6, "ht", "dental", "plain"),
            new Glottal(7, "ʔt", "dental", "plain"),
            new Glottal(8, "htʰ", "dental", "aspirated"),
            new Glottal(9, "ʔtʼ", "dental", "ejective"),
            new Glottal(0, "", "", ""),
            new Glottal(0, "", "", ""),

            new Glottal(10, "hṭ", "alveolar", "plain"),
            new Glottal(11, "ʔṭ", "alveolar", "plain"),
            new Glottal(12, "hṭʰ", "alveolar", "aspirated"),
            new Glottal(13, "ʔṭʼ", "alveolar", "ejective"),
            new Glottal(14, "ʔd", "alveolar", "voiced"),
            new Glottal(0, "", "", ""),

            new Glottal(15, "hc", "palatal", "plain"),
            new Glottal(16, "ʔc", "palatal", "plain"),
            new Glottal(17, "hcʰ", "palatal", "aspirated"),
            new Glottal(18, "ʔcʼ", "palatal", "ejective"),
            new Glottal(0, "", "", ""),
            new Glottal(0, "", "", ""),

            new Glottal(19, "hk", "velar", "plain"),
            new Glottal(20, "ʔk", "velar", "plain"),
            new Glottal(21, "hkʰ", "velar", "aspirated"),
            new Glottal(22, "ʔkʼ", "velar", "ejective"),
            new Glottal(0, "", "", ""),
            new Glottal(0, "", "", ""),

            new Glottal(23, "hq", "uvular", "plain"),
            new Glottal(24, "ʔq", "uvular", "plain"),
            new Glottal(25, "hqʰ", "uvular", "aspirated"),
            new Glottal(26, "ʔqʼ", "uvular", "ejective"),
            new Glottal(0, "", "", ""),
            new Glottal(0, "", "", ""),

            new Glottal(27, "hs", "fricatives", "aspirated"),
            new Glottal(28, "ʔs", "fricatives", "ejective"),
            new Glottal(29, "ʔsʼ", "fricatives", "ejective"),
            new Glottal(30, "hš", "fricatives", "aspirated"),
            new Glottal(31, "ʔš", "fricatives", "ejective"),
            new Glottal(0, "", "", ""),

            new Glottal(32, "hm", "sonorants", "aspirated"),
            new Glottal(33, "ʔm", "sonorants", "glottalized "),
            new Glottal(34, "hn", "sonorants", "aspirated"),
            new Glottal(35, "ʔn", "sonorants", "glottalized "),
            new Glottal(36, "hl", "sonorants", "aspirated"),
            new Glottal(37, "ʔl", "sonorants", "glottalized "),

            new Glottal(38, "hw", "glides", "aspirated"),
            new Glottal(39, "ʔw", "glides", "glottalized "),
            new Glottal(40, "hy", "glides", "aspirated"),
            new Glottal(41, "ʔy", "glides", "glottalized "),

    };

    public static List<Sound> getSoundsByClass(String className) {
        return getSoundsByClass(className, GLOTTALS);
    }
}
