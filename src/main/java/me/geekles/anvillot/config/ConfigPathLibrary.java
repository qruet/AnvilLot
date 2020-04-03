package me.geekles.anvillot.config;

public enum ConfigPathLibrary {

    MAX_REPAIR_COST("Default Max Repair Cost"),

    EXPERIENCE_BAR("Experience Bar"),
    EXPERIENCE_BAR_ENABLED(EXPERIENCE_BAR + ".Enabled"),
    EXPERIENCE_BAR_TITLE(EXPERIENCE_BAR + ".Title"),
    EXPERIENCE_BAR_COLOR(EXPERIENCE_BAR + ".Bar Color"),
    EXPERIENCE_BAR_STYLE(EXPERIENCE_BAR + ".Bar Style"),
    EXPERIENCE_BAR_FOG(EXPERIENCE_BAR + ".Fog"),
    EXPERIENCE_BAR_DARK_SKY(EXPERIENCE_BAR + ".Dark Sky"),

    TOO_EXPENSIVE_BAR("Too Expensive Bar"),
    TOO_EXPENSIVE_BAR_ENABLED(TOO_EXPENSIVE_BAR + ".Enabled"),
    TOO_EXPENSIVE_BAR_TITLE(TOO_EXPENSIVE_BAR + ".Title"),
    TOO_EXPENSIVE_BAR_COLOR(TOO_EXPENSIVE_BAR + ".Bar Color"),
    TOO_EXPENSIVE_BAR_STYLE(TOO_EXPENSIVE_BAR + ".Bar Style"),
    TOO_EXPENSIVE_BAR_FOG(TOO_EXPENSIVE_BAR + ".Fog"),
    TOO_EXPENSIVE_BAR_DARK_SKY(TOO_EXPENSIVE_BAR + ".Dark Sky"),

    TOO_EXPENSIVE_SOUND_EFFECT("Too Expensive Sound Effect"),
    TOO_EXPENSIVE_SOUND_EFFECT_ENABLED(TOO_EXPENSIVE_SOUND_EFFECT + ".Enabled"),
    TOO_EXPENSIVE_SOUND_EFFECT_SOUND(TOO_EXPENSIVE_SOUND_EFFECT + ".Sound"),
    TOO_EXPENSIVE_SOUND_EFFECT_VOLUME(TOO_EXPENSIVE_SOUND_EFFECT + ".Volume"),
    TOO_EXPENSIVE_SOUND_EFFECT_PITCH(TOO_EXPENSIVE_SOUND_EFFECT + ".Pitch");

    private String path;

    ConfigPathLibrary(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }

}
