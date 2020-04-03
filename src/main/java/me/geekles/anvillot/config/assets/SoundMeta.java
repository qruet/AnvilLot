package me.geekles.anvillot.config.assets;

import org.bukkit.Sound;

/**
 * Responsible for storing basic meta regarding bukkit sounds (for deserialization purposes)
 * @author qruet
 * @version 3.0.0-Beta-SNAPSHOT
 */
public class SoundMeta {

    private final Sound sound;
    private final float vol;
    private final float pitch;

    public SoundMeta(Sound sound, float vol, float pitch) {
        this.sound = sound;
        this.vol = vol;
        this.pitch = pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return vol;
    }

    public float getPitch() {
        return pitch;
    }

}
