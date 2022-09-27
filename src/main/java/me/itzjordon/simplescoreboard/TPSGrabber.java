package me.itzjordon.simplescoreboard;

import org.bukkit.scheduler.BukkitRunnable;

public class TPSGrabber extends BukkitRunnable {

    // im using this method because it works cross-version without using NMS & reflection...
    // tbh, this was copied off of a spigot thread (https://www.spigotmc.org/threads/get-the-server-tps.147065/page-2).

    public static int ticks = 0;
    public static long[] tickList = new long[600];
    public static long lastTick = 0L;

    public static double getTPS()
    {
        return getTPS(100);
    }

    public static double getTPS(int ticks)
    {
        if (TPSGrabber.ticks < ticks) {
            return 20.0D;
        }
        int target = (TPSGrabber.ticks - 1 - ticks) % tickList.length;
        long elapsed = System.currentTimeMillis() - tickList[target];

        return ticks / (elapsed / 1000.0D);
    }

    public static long getElapsed(int tickID)
    {
        if (ticks - tickID >= tickList.length)
        {
        }

        long time = tickList[(tickID % tickList.length)];
        return System.currentTimeMillis() - time;
    }

    public void run()
    {
        tickList[(ticks % tickList.length)] = System.currentTimeMillis();

        ticks += 1;
    }
}
