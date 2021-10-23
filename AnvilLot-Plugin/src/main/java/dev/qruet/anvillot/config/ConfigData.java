package dev.qruet.anvillot.config;

public enum ConfigData {

    MAX_REPAIR_COST("Default Max Repair Cost", Integer.class),
    HARD_LIMIT("Hard Limit", Boolean.class),

    REPAIR_COST_EQUATION("Repair Cost Equation", String.class),
    REPAIR_PROGRESSION_EQUATION("Repair Progression Equation", String.class),

    EXPERIENCE_BAR("Experience Bar", RootPath.class),
    EXPERIENCE_BAR_ENABLED(EXPERIENCE_BAR + ".Enabled", Boolean.class),
    EXPERIENCE_BAR_TITLE(EXPERIENCE_BAR + ".Title", String.class),
    EXPERIENCE_BAR_COLOR(EXPERIENCE_BAR + ".Bar Color", String.class),
    EXPERIENCE_BAR_STYLE(EXPERIENCE_BAR + ".Bar Style", String.class),
    EXPERIENCE_BAR_FOG(EXPERIENCE_BAR + ".Fog", Boolean.class),
    EXPERIENCE_BAR_DARK_SKY(EXPERIENCE_BAR + ".Dark Sky", Boolean.class),

    TOO_EXPENSIVE_BAR("Too Expensive Bar", RootPath.class),
    TOO_EXPENSIVE_BAR_ENABLED(TOO_EXPENSIVE_BAR + ".Enabled", Boolean.class),
    TOO_EXPENSIVE_BAR_TITLE(TOO_EXPENSIVE_BAR + ".Title", String.class),
    TOO_EXPENSIVE_BAR_COLOR(TOO_EXPENSIVE_BAR + ".Bar Color", String.class),
    TOO_EXPENSIVE_BAR_STYLE(TOO_EXPENSIVE_BAR + ".Bar Style", String.class),
    TOO_EXPENSIVE_BAR_FOG(TOO_EXPENSIVE_BAR + ".Fog", Boolean.class),
    TOO_EXPENSIVE_BAR_DARK_SKY(TOO_EXPENSIVE_BAR + ".Dark Sky", Boolean.class),

    HARD_LIMIT_BAR("Hard Limit Bar", RootPath.class),
    HARD_LIMIT_BAR_TITLE(HARD_LIMIT_BAR + ".Title", String.class),
    HARD_LIMIT_BAR_COLOR(HARD_LIMIT_BAR + ".Bar Color", String.class),
    HARD_LIMIT_BAR_STYLE(HARD_LIMIT_BAR + ".Bar Style", String.class),
    HARD_LIMIT_BAR_FOG(HARD_LIMIT_BAR + ".Fog", Boolean.class),
    HARD_LIMIT_BAR_DARK_SKY(HARD_LIMIT_BAR + ".Dark Sky", Boolean.class),

    DISABLED_SOUND_EFFECT("Disabled Sound Effect", RootPath.class),
    DISABLED_SOUND_EFFECT_ENABLED(DISABLED_SOUND_EFFECT + ".Enabled", Boolean.class),
    DISABLED_SOUND_EFFECT_SOUND(DISABLED_SOUND_EFFECT + ".Sound", String.class),
    DISABLED_SOUND_EFFECT_VOLUME(DISABLED_SOUND_EFFECT + ".Volume", Float.class),
    DISABLED_SOUND_EFFECT_PITCH(DISABLED_SOUND_EFFECT + ".Pitch", Float.class);

    private final String path;
    private final Class<?> type;
    private Object value;

    ConfigData(String path, Class<?> type) {
        this.path = path;
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return path;
    }

    public void set(Object obj) {
        this.value = obj;
    }

    public Object get() {
        return value;
    }

    public static class RootPath {

    }

}
