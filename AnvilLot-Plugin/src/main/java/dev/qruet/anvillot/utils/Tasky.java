package dev.qruet.anvillot.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.lang.ref.WeakReference;

/**
 * Tasky.java
 * <p>
 * Requires Java 8, uses lambda expressions.
 *
 * @author PLARSON (Wowserman)
 * https://www.spigotmc.org/members/wowserman.124342/
 * <p>
 * Created Jun 29, 2018
 */

/**
 * Modified for personal use
 * @author Qruet
 * @version 3.0.0-Beta-SNAPSHOT
 */
public final class Tasky {

    /// MARK – Make sure you set the plugin. Hopefully you read this or notice you need to.

    private static WeakReference<Plugin> plugin;

    public static void setPlugin(final Plugin plugin) {
        Tasky.plugin = new WeakReference<>(plugin);
    }

    public interface Response<T> {
        void onCompletion(T t);
    }

    public static Thread async(final Thread thread) {
        thread.start();
        return thread;
    }

    /// MARK

    public static BukkitRunnable async(final Consumer<BukkitRunnable> block) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }

        };
        runnable.runTaskAsynchronously(plugin.get());
        return runnable;
    }

    public static BukkitRunnable sync(final Consumer<BukkitRunnable> block) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTask(plugin.get());
        return runnable;
    }

    public static BukkitRunnable async(final Consumer<BukkitRunnable> block, final long delay) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskLaterAsynchronously(plugin.get(), delay);
        return runnable;
    }

    public static BukkitRunnable sync(final Consumer<BukkitRunnable> block, final long delay) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskLater(plugin.get(), delay);
        return runnable;
    }

    public static BukkitRunnable sync(final Consumer<BukkitRunnable> block, final long delay, final long period) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskTimer(plugin.get(), delay, period);
        return runnable;
    }

    public static BukkitRunnable async(final Consumer<BukkitRunnable> block, final long delay, final long period) {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                block.accept(this);
            }
        };
        runnable.runTaskTimerAsynchronously(plugin.get(), delay, period);
        return runnable;
    }
}