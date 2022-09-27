package me.itzjordon.simplescoreboard;

import me.itzjordon.simplescoreboard.scoreboardstuff.BasicScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class SimpleScoreboard extends JavaPlugin {
    static Map<Player, BasicScoreboard> scoreboards = new HashMap<>();
    public int uniqueMembers = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        new BukkitRunnable() {
            @Override
            public void run() {
//                not very performance friendly ;-;
                Bukkit.getOnlinePlayers().forEach(SimpleScoreboard.this::showScoreboard);
            }
        }.runTaskTimer(this, 100, 100); // will update the scoreboard every 5 seconds.
        BasicScoreboard sb = new BasicScoreboard("Scoreboard");
        new TPSGrabber().runTaskTimer(this, 100, 1);
    }

    public void showScoreboard(Player player) {
        if (!scoreboards.containsKey(player)) {
            setScoreboard(player);
        }
        updateScoreboard(scoreboards.get(player), player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        File playerData = new File(Bukkit.getWorlds().get(0).getWorldFolder().getAbsoluteFile(), "playerdata"); // the playerdata folder.
        uniqueMembers = playerData.list().length;
        // updates whenever someone joins.
    }

    private void updateScoreboard(BasicScoreboard ss, Player player) {
        ss.reset();
        double tps = Math.round((1.0D - TPSGrabber.getTPS(200) / 20.0D) * 100.0D);
        ss.add("&8");
        ss.add(ChatColor.translateAlternateColorCodes('&', "&9Player"));
        ss.add(player.getName());
        // get the player's health and maxhealth:
        ss.add("&bHealth&7: &3" + Math.floor(player.getHealth()) + " / " + player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        ss.add(ChatColor.translateAlternateColorCodes('&', "&9Server"));
//      not very performance friendly ;-;
        int onlinePlayers = Bukkit.getOnlinePlayers().size(); // doesn't account for vanished people, you can do that with checking if the player is visible to the other player
//        player.canSee(otherPlayer)
        ss.add(String.format(ChatColor.translateAlternateColorCodes('&', "&bOnline&7: &3%s"), onlinePlayers));
        ss.add("&bUnique members&7: &3" + uniqueMembers);
        ss.add("&bTPS&7: &3"+tps);
        ss.add("&cIdk what else to put here :p");
        ss.update();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));
    }

    public void setScoreboard(Player player) {
        if (scoreboards.containsKey(player)) {
            scoreboards.get(player).reset();
        }
        scoreboards.remove(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        BasicScoreboard ss = new BasicScoreboard(ChatColor.translateAlternateColorCodes('&', "&cBasic SB"));
        updateScoreboard(ss, player);
        ss.update();
        ss.send(player);
        scoreboards.put(player, ss);
    }
}
