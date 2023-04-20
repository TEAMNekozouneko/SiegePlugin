package amedouhu.siegeplugin.events;

import amedouhu.siegeplugin.SiegePlugin;
import amedouhu.siegeplugin.apis.SendMessage;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerDeathEvent implements Listener {
    // 大将の死亡を検知する
    FileConfiguration config = SiegePlugin.getPlugin().getConfig();
    SendMessage sendMessage = new SendMessage();
    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent e) {
        // プレイヤーが死亡したとき
        Player target = e.getEntity();
        if (target.getScoreboardTags().contains("RED")) {
            // 死亡したプレイヤーが赤チームの大将なら
            // 青チームの勝ちで終了させる
            // イベントを設定する
            e.setDroppedExp(0);
            e.getDrops().clear();
            e.setKeepInventory(false);
            // ゲームルールを設定する
            target.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
            // 終了メッセージを送信する
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getScoreboardTags().contains("siege")) {
                    sendMessage.sendPlayer(player,ChatColor.BLUE + "青 " + ChatColor.WHITE + "チームが勝利しました。");
                    player.sendTitle("試合終了", ChatColor.BLUE + "青チームが勝利しました",10,40,10);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1.0f,1.0f);
                    // スコアボードタグをすべて除去する
                    player.getScoreboardTags().remove("siege");
                    player.getScoreboardTags().remove("red");
                    player.getScoreboardTags().remove("RED");
                    player.getScoreboardTags().remove("blue");
                    player.getScoreboardTags().remove("BLUE");
                    // 体力・空腹を設定する
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    // インベントリをクリアする
                    player.getInventory().clear();
                    // リストネームをクリアする
                    player.setPlayerListName(player.getName());
                }
            }
        }else if (target.getScoreboardTags().contains("BLUE")) {
            // 死亡したプレイヤーが青チームの大将なら
            // 赤チームの勝ちで終了させる
            // イベントを設定する
            e.setDroppedExp(0);
            e.getDrops().clear();
            e.setKeepInventory(false);
            // ゲームルールを設定する
            target.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN,true);
            // 終了メッセージを送信する
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getScoreboardTags().contains("siege")) {
                    sendMessage.sendPlayer(player,ChatColor.RED + "赤 " + ChatColor.WHITE + "チームが勝利しました。");
                    player.sendTitle("試合終了", ChatColor.RED + "赤チームが勝利しました",10,40,10);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1.0f,1.0f);
                    // スコアボードタグをすべて除去する
                    player.getScoreboardTags().remove("siege");
                    player.getScoreboardTags().remove("red");
                    player.getScoreboardTags().remove("RED");
                    player.getScoreboardTags().remove("blue");
                    player.getScoreboardTags().remove("BLUE");
                    // 体力・空腹を設定する
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    // インベントリをクリアする
                    player.getInventory().clear();
                    // リストネームをクリアする
                    player.setPlayerListName(player.getName());
                }
            }

        }
    }
}
