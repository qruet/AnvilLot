package dev.qruet.anvillot.config;

import com.google.common.base.Preconditions;
import dev.qruet.anvillot.config.assets.SoundMeta;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.Arrays;

public class GeneralPresets {

    public static int DEFAULT_MAX_COST = -1;
    public static boolean HARD_LIMIT = false;

    public static SoundMeta DISABLED_ALERT = null;

    public static String REPAIR_COST_EQUATION;
    public static String REPAIR_PROGRESSION_EQUATION;

    public static boolean EXPERIENCE_BAR_ENABLED = true;
    public static boolean TOO_EXPENSIVE_BAR_ENABLED = true;
    public static boolean HARD_LIMIT_BAR_ENABLED = false;

    public static void init() {
        DEFAULT_MAX_COST = (int) ConfigData.MAX_REPAIR_COST.get();
        HARD_LIMIT = (boolean) ConfigData.HARD_LIMIT.get();

        REPAIR_COST_EQUATION = (String) ConfigData.REPAIR_COST_EQUATION.get();
        REPAIR_PROGRESSION_EQUATION = (String) ConfigData.REPAIR_PROGRESSION_EQUATION.get();

        EXPERIENCE_BAR_ENABLED = (boolean) ConfigData.EXPERIENCE_BAR_ENABLED.get();
        TOO_EXPENSIVE_BAR_ENABLED = (boolean) ConfigData.TOO_EXPENSIVE_BAR_ENABLED.get();
        HARD_LIMIT_BAR_ENABLED = HARD_LIMIT;

        if ((boolean) ConfigData.DISABLED_SOUND_EFFECT_ENABLED.get()) {
            String sN = (String) ConfigData.DISABLED_SOUND_EFFECT_SOUND.get();
            Sound sound = Sound.valueOf(sN);
            float volume = (float) ConfigData.DISABLED_SOUND_EFFECT_VOLUME.get();
            float pitch = (float) ConfigData.DISABLED_SOUND_EFFECT_PITCH.get();
            DISABLED_ALERT = new SoundMeta(sound, volume, pitch);
        }

        if (EXPERIENCE_BAR_ENABLED) {
            ExperienceBarPresets.init();
        }

        if (TOO_EXPENSIVE_BAR_ENABLED) {
            TooExpensiveBarPresets.init();
        }

    }

    public static class ExperienceBarPresets {
        public static String TITLE = "&a%level";
        public static BarColor COLOR = BarColor.GREEN;
        public static BarStyle STYLE = BarStyle.SEGMENTED_12;
        public static boolean FOG = false;
        public static boolean DARK_SKY = false;

        public static void init() {
            TITLE = (String) ConfigData.EXPERIENCE_BAR_TITLE.get();

            String bC = String.valueOf(ConfigData.EXPERIENCE_BAR_COLOR.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarColor.values()).anyMatch(c -> {
                return c.toString().equals(bC);
            }), "Invalid bar color specified in config for \"Experience Bar\"." +
                    " Please enter one of the following colors: " + Arrays.toString(BarColor.values()));
            COLOR = BarColor.valueOf(bC);

            String bS = String.valueOf(ConfigData.EXPERIENCE_BAR_STYLE.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarStyle.values()).anyMatch(c -> {
                return c.toString().equals(bS);
            }), "Invalid bar style specified in config for \"Experience Bar\"." +
                    " Please enter one of the following styles: " + Arrays.toString(BarStyle.values()));
            STYLE = BarStyle.valueOf(bS);

            FOG = (boolean) ConfigData.EXPERIENCE_BAR_FOG.get();
            DARK_SKY = (boolean) ConfigData.EXPERIENCE_BAR_DARK_SKY.get();
        }

    }

    public static class TooExpensiveBarPresets {
        public static String TITLE = "&cExperience Cost %cost";
        public static BarColor COLOR = BarColor.RED;
        public static BarStyle STYLE = BarStyle.SOLID;
        public static boolean FOG = true;
        public static boolean DARK_SKY = false;

        public static void init() {
            TITLE = (String) ConfigData.TOO_EXPENSIVE_BAR_TITLE.get();

            String bC = String.valueOf(ConfigData.TOO_EXPENSIVE_BAR_COLOR.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarColor.values()).anyMatch(c -> {
                return c.toString().equals(bC);
            }), "Invalid bar color specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following colors: " + Arrays.toString(BarColor.values()));
            COLOR = BarColor.valueOf(bC);

            String bS = String.valueOf(ConfigData.TOO_EXPENSIVE_BAR_STYLE.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarStyle.values()).anyMatch(c -> {
                return c.toString().equals(bS);
            }), "Invalid bar style specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following styles: " + Arrays.toString(BarStyle.values()));
            STYLE = BarStyle.valueOf(bS);

            FOG = (boolean) ConfigData.TOO_EXPENSIVE_BAR_FOG.get();
            DARK_SKY = (boolean) ConfigData.TOO_EXPENSIVE_BAR_DARK_SKY.get();
        }
    }

    public static class HardLimitBarPresets {
        public static String TITLE = "&cRepair Limit Reached";
        public static BarColor COLOR = BarColor.RED;
        public static BarStyle STYLE = BarStyle.SOLID;
        public static boolean FOG = true;
        public static boolean DARK_SKY = true;

        public static void init() {
            TITLE = (String) ConfigData.HARD_LIMIT_BAR_TITLE.get();

            String bC = String.valueOf(ConfigData.HARD_LIMIT_BAR_COLOR.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarColor.values()).anyMatch(c -> {
                return c.toString().equals(bC);
            }), "Invalid bar color specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following colors: " + Arrays.toString(BarColor.values()));
            COLOR = BarColor.valueOf(bC);

            String bS = String.valueOf(ConfigData.HARD_LIMIT_BAR_STYLE.get()).toUpperCase();
            Preconditions.checkArgument(Arrays.stream(BarStyle.values()).anyMatch(c -> {
                return c.toString().equals(bS);
            }), "Invalid bar style specified in config for \"Too Expensive Bar\"." +
                    " Please enter one of the following styles: " + Arrays.toString(BarStyle.values()));
            STYLE = BarStyle.valueOf(bS);

            FOG = (boolean) ConfigData.HARD_LIMIT_BAR_FOG.get();
            DARK_SKY = (boolean) ConfigData.HARD_LIMIT_BAR_DARK_SKY.get();
        }
    }
}
