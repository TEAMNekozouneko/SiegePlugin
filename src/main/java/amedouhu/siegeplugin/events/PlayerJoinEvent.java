package amedouhu.siegeplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {
    // プレイヤーが参加した時スコアボードタグを持っているなら削除する
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        // プレイヤーが参加した時
        Player player = e.getPlayer();
        player.getScoreboardTags().remove("siege");
        player.getScoreboardTags().remove("red");
        player.getScoreboardTags().remove("RED");
        player.getScoreboardTags().remove("blue");
        player.getScoreboardTags().remove("BLUE");
    }
}
