package me.geekles.anvillot.config;

import com.google.common.base.Preconditions;
import me.geekles.anvillot.config.assets.SoundMeta;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class GeneralPresets {

    public static int DEFAULT_MAX_COST = -1;
    public static SoundMeta TOO_EXPENSIVE_ALERT = null;

    public static boolean TOO_EXPENSIVE_ALERT_ENABLED = true;
    public static boolean EXPERIENCE_BAR_ENABLED = true;
    public static boolean TOO_EXPENSIVE_BAR_ENABLED = true;

    public static void init(FileConfiguration config) {
        DEFAULT_MAX_COST = config.getInt("" + ConfigPathLibrary.MAX_REPAIR_COST);
        TOO_EXPENSIVE_ALERT_ENABLED = config.getBoolean("" + ConfigPathLibrary.TOO_EXPENSIVE_SOUND_EFFECT_ENABLED);
        EXPERIENCE_BAR_ENABLED = config.getBoolean("" + ConfigPathLibrary.EXPERIENCE_BAR_ENABLED);
        TOO_EXPENSIVE_BAR_ENABLED = config.getBoolean("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_ENABLED);

        if (config.getBoolean("" + ConfigPathLibrary.TOO_EXPENSIVE_SOUND_EFFECT_ENABLED)) {
            String sN = config.getString("" + ConfigPathLibrary.TOO_EXPENSIVE_SOUND_EFFECT_SOUND);
            Sound sound = Sound.valueOf(sN);

            float volume = (float) config.getDouble("" + ConfigPathLibrary.TOO_EXPENSIVE_SOUND_EFFECT_VOLUME);
            float pitch = (float) config.getDouble("" + ConfigPathLibrary.TOO_EXPENSIVE_SOUND_EFFECT_PITCH);

            TOO_EXPENSIVE_ALERT = new SoundMeta(sound, volume, pitch);
        }

        if (EXPERIENCE_BAR_ENABLED)
            ExperienceBarPresets.init(config);
        if (TOO_EXPENSIVE_BAR_ENABLED)
            TooExpensiveBarPresets.init(config);
    }

    public static class ExperienceBarPresets {
        public static String TITLE = "&a%level";
        public static BarColor COLOR = BarColor.GREEN;
        public static BarStyle STYLE = BarStyle.SEGMENTED_12;
        public static boolean FOG = false;
        public static boolean DARK_SKY = false;

        public static void init(FileConfiguration config) {
            TITLE = config.getString("" + ConfigPathLibrary.EXPERIENCE_BAR_TITLE);

            String bC = config.getString("" + ConfigPathLibrary.EXPERIENCE_BAR_COLOR).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarColor.values()).anyMatch(c -> {
                return c.toString().equals(bC);
            }), "Invalid bar color specified in config for \"Experience Bar\"." +
                    " Please enter one of the following colors: " + Arrays.toString(BarColor.values()));
            COLOR = BarColor.valueOf(bC);

            String bS = config.getString("" + ConfigPathLibrary.EXPERIENCE_BAR_STYLE).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarStyle.values()).anyMatch(c -> {
                return c.toString().equals(bS);
            }), "Invalid bar style specified in config for \"Experience Bar\"." +
                    " Please enter one of the following styles: " + Arrays.toString(BarStyle.values()));
            STYLE = BarStyle.valueOf(bS);

            FOG = config.getBoolean("" + ConfigPathLibrary.EXPERIENCE_BAR_FOG);
            DARK_SKY = config.getBoolean("" + ConfigPathLibrary.EXPERIENCE_BAR_DARK_SKY);
        }

    }

    public static class TooExpensiveBarPresets {
        public static String TITLE = "&cExperience Cost %cost";
        public static BarColor COLOR = BarColor.RED;
        public static BarStyle STYLE = BarStyle.SOLID;
        public static boolean FOG = true;
        public static boolean DARK_SKY = true;

        public static void init(FileConfiguration config) {
            TITLE = config.getString("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_TITLE);

            String bC = config.getString("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_COLOR).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarColor.values()).anyMatch(c -> {
                return c.toString().equals(bC);
            }), "Invalid bar color specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following colors: " + Arrays.toString(BarColor.values()));
            COLOR = BarColor.valueOf(bC);

            String bS = config.getString("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_STYLE).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarStyle.values()).anyMatch(c -> {
                return c.toString().equals(bS);
            }), "Invalid bar style specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following styles: " + Arrays.toString(BarStyle.values()));
            STYLE = BarStyle.valueOf(bS);

            FOG = config.getBoolean("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_FOG);
            DARK_SKY = config.getBoolean("" + ConfigPathLibrary.TOO_EXPENSIVE_BAR_DARK_SKY);
        }
    }
}
