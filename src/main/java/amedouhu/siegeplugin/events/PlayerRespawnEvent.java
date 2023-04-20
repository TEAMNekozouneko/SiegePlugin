package amedouhu.siegeplugin.events;

import amedouhu.siegeplugin.SiegePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerRespawnEvent implements Listener {
    // リスポーン位置を設定する
    FileConfiguration config = SiegePlugin.getPlugin().getConfig();
    @EventHandler
    public void onPlayerRespawn(org.bukkit.event.player.PlayerRespawnEvent e) {
        // プレイヤーがリスポーンしたとき
        Player target = e.getPlayer();
        if (target.getScoreboardTags().contains("red")) {
            // 死亡したプレイヤーが赤チームの兵役なら
            String locationString = config.getString("red");
            String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
            Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
            e.setRespawnLocation(location);
        }else if (target.getScoreboardTags().contains("blue")) {
            String locationString = config.getString("blue");
            String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
            Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
            e.setRespawnLocation(location);
        }
    }
}
