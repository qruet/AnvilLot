package me.geekles.repaircost.checks;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface ModeChecker {

    void update();
    void terminate();
    UUID getUUID();

}
