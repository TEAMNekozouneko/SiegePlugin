package amedouhu.siegeplugin.commands;

import amedouhu.siegeplugin.SiegePlugin;
import amedouhu.siegeplugin.apis.SendMessage;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class Siege implements CommandExecutor, TabCompleter {
    // 「siege」コマンドの処理クラス
    FileConfiguration config = SiegePlugin.getPlugin().getConfig();
    SendMessage sendMessage = new SendMessage();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // コマンドが実行されたとき
        if (sender.hasPermission("siege")) {
            // 権限を付与されているなら
            if (1 <= args.length) {
                // 引数の長さが1以上なら
                Player target;
                switch (args[0]) {
                    case "inventory":
                        // インベントリを設定する
                        // 対象のプレイヤーを取得する
                        target = null;
                        switch (args.length) {
                            case 1:
                                if (sender instanceof Player) {
                                    // 送信者がプレイヤーなら
                                    target = (Player) sender;
                                }
                                break;
                            case 2:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.getName().equals(args[1])) {
                                        // 引数のプレイヤーがオンラインなら
                                        target = player;
                                        break;
                                    }
                                }
                                break;
                        }
                        // 対象のプレイヤーを取得できたかを確認する
                        if (target != null) {
                            // 対象のプレイヤーを取得できているなら
                            Inventory inventory = target.getInventory();
                            // config.ymlを初期化する
                            config.set("inventory",null);
                            // config.ymlを上書きする
                            for (int i=0;i<43;i++) {
                                // スロットにアイテムを設置する
                                if (inventory.getItem(i) != null) {
                                    // ループ中のスロットにアイテムが設定されているなら
                                    ItemStack itemStack = inventory.getItem(i);
                                    Map<String, Object> itemMap = Objects.requireNonNull(itemStack).serialize();
                                    config.createSection("inventory." + i,itemMap);
                                    SiegePlugin.getPlugin().saveConfig();
                                }
                            }
                            SiegePlugin.getPlugin().saveConfig();
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"処理は正常に実行されました。");
                        }else {
                            // 対象のプレイヤーを取得できていないなら
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"対象のプレイヤーを取得できませんでした。");
                        }
                        break;
                    case "run":
                        // オンラインプレイヤーをリスト化する
                        List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                        Collections.shuffle(players);
                        // チーム分けを行う
                        for (int i=0;i<players.size();i++) {
                            Player player = players.get(i);
                            ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                            LeatherArmorMeta meta = (LeatherArmorMeta) chestPlate.getItemMeta();
                            // 現在のチームから退出させる
                            player.getScoreboardTags().remove("siege");
                            player.getScoreboardTags().remove("red");
                            player.getScoreboardTags().remove("RED");
                            player.getScoreboardTags().remove("blue");
                            player.getScoreboardTags().remove("BLUE");
                            if (i < players.size() / 2) {
                                // 赤チームに分類する
                                // 赤色の皮防具を装備させる
                                Objects.requireNonNull(meta).addEnchant(Enchantment.BINDING_CURSE, 1, true);
                                meta.setUnbreakable(true);
                                Objects.requireNonNull(meta).setColor(Color.RED);
                                chestPlate.setItemMeta(meta);
                                player.getInventory().setItem(38,chestPlate);
                                if (i == players.size() / 2 - 1) {
                                    // 赤色の大将に分類する
                                    sendMessage.sendPlayer(player,"あなたは" + ChatColor.RED + " 赤 " + ChatColor.WHITE + "チームの大将です。");
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_HELMET);
                                    player.getInventory().setItem(39,itemStack);
                                    player.setPlayerListName(ChatColor.RED + "赤大将 " + player.getName());
                                    player.addScoreboardTag("RED");
                                }else {
                                    // 赤色の兵士に分類する
                                    sendMessage.sendPlayer(player,"あなたは" + ChatColor.RED + " 赤 " + ChatColor.WHITE + "チームです。");
                                    player.setPlayerListName(ChatColor.RED + "赤 " + player.getName());
                                    player.addScoreboardTag("red");
                                }
                            }else {
                                // 青チームに分類する
                                // 青色の皮防具を装備させる
                                Objects.requireNonNull(meta).addEnchant(Enchantment.BINDING_CURSE, 1, true);
                                meta.setUnbreakable(true);
                                Objects.requireNonNull(meta).setColor(Color.BLUE);
                                chestPlate.setItemMeta(meta);
                                player.getInventory().setItem(38,chestPlate);
                                if (i == players.size() - 1) {
                                    // 青色の大将に分類する
                                    sendMessage.sendPlayer(player,"あなたは" + ChatColor.BLUE + " 青 " + ChatColor.WHITE + "チームの大将です。");
                                    ItemStack itemStack = new ItemStack(Material.DIAMOND_HELMET);
                                    player.getInventory().setItem(39,itemStack);
                                    player.setPlayerListName(ChatColor.BLUE + "青大将 " + player.getName());
                                    player.addScoreboardTag("BLUE");
                                }else {
                                    sendMessage.sendPlayer(player,"あなたは" + ChatColor.BLUE + " 青 " + ChatColor.WHITE + "チームです。");
                                    player.setPlayerListName(ChatColor.BLUE + "青 " + player.getName());
                                    player.addScoreboardTag("blue");
                                }
                            }
                            player.addScoreboardTag("siege");
                        }
                        // 実行メッセージを送信する
                        sendMessage.sendSender(sender,"処理は正常に実行されました。");
                        // ゲームを開始する
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getScoreboardTags().contains("siege")) {
                                // ループ中のプレイヤーがSiegeのプレイヤーなら
                                // 試合開始のメッセージを送信する
                                player.sendTitle("試合開始",null,10,40,10);
                                player.playSound(player, Sound.BLOCK_ANVIL_USE,1.0f,1.0f);
                                // 体力・空腹を設定する
                                player.setHealth(20);
                                player.setFoodLevel(20);
                                // ゲームモードを設定する
                                player.setGameMode(GameMode.ADVENTURE);
                                if (player.getScoreboardTags().contains("red") || player.getScoreboardTags().contains("RED")) {
                                    String locationString = config.getString("red");
                                    String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
                                    Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
                                    player.teleport(location);
                                }else if (player.getScoreboardTags().contains("blue") || player.getScoreboardTags().contains("BLUE")) {
                                    String locationString = config.getString("blue");
                                    String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
                                    Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
                                    player.teleport(location);
                                }
                                // インベントリを設定する
                                Inventory inventory = player.getInventory();
                                for (int i=0;i<43;i++) {
                                    // スロットにアイテムを設置する
                                    if (config.get("inventory." + i) != null) {
                                        // ループ中のスロットにアイテムが設定されているなら
                                        Map<String, Object> itemMap = config.getConfigurationSection("inventory." + i).getValues(true);
                                        ItemStack itemStack = ItemStack.deserialize(itemMap);
                                        inventory.setItem(i,itemStack);
                                    }
                                }
                            }
                        }
                        // 実行メッセージを送信する
                        sendMessage.sendSender(sender,"処理は正常に実行されました。");
                        break;
                    case "red":
                        // 赤チームをテレポートさせる
                        // 対象のプレイヤーを取得する
                        target = null;
                        switch (args.length) {
                            case 1:
                                if (sender instanceof Player) {
                                    // 送信者がプレイヤーなら
                                    target = (Player) sender;
                                }
                                break;
                            case 2:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.getName().equals(args[1])) {
                                        // 引数のプレイヤーがオンラインなら
                                        target = player;
                                        break;
                                    }
                                }
                                break;
                        }
                        // 対象のプレイヤーを取得できたかを確認する
                        if (target != null) {
                            // 対象のプレイヤーを取得できているなら
                            // 対象のプレイヤーにテレポートさせる
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getScoreboardTags().contains("red") || player.getScoreboardTags().contains("RED")) {
                                    // ループ中のプレイヤーがSiegeのプレイヤーなら
                                    player.teleport(target.getLocation());
                                    Location location = target.getLocation();
                                    String locationString = Objects.requireNonNull(location.getWorld()).getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
                                    config.set("red",locationString);
                                    SiegePlugin.getPlugin().saveConfig();
                                }
                            }
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"処理は正常に実行されました。");
                        }else {
                            // 対象のプレイヤーを取得できていないなら
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"対象のプレイヤーを取得できませんでした。");
                        }
                        break;
                    case "blue":
                        // 青チームをテレポートさせる
                        // 対象のプレイヤーを取得する
                        target = null;
                        switch (args.length) {
                            case 1:
                                if (sender instanceof Player) {
                                    // 送信者がプレイヤーなら
                                    target = (Player) sender;
                                }
                                break;
                            case 2:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.getName().equals(args[1])) {
                                        // 引数のプレイヤーがオンラインなら
                                        target = player;
                                        break;
                                    }
                                }
                                break;
                        }
                        // 対象のプレイヤーを取得できたかを確認する
                        if (target != null) {
                            // 対象のプレイヤーを取得できているなら
                            // 対象のプレイヤーにテレポートさせる
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getScoreboardTags().contains("blue") || player.getScoreboardTags().contains("BLUE")) {
                                    // ループ中のプレイヤーがSiegeのプレイヤーなら
                                    player.teleport(target.getLocation());
                                    Location location = target.getLocation();
                                    String locationString = Objects.requireNonNull(location.getWorld()).getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
                                    config.set("blue",locationString);
                                    SiegePlugin.getPlugin().saveConfig();
                                }
                            }
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"処理は正常に実行されました。");
                        }else {
                            // 対象のプレイヤーを取得できていないなら
                            // 実行メッセージを送信する
                            sendMessage.sendSender(sender,"対象のプレイヤーを取得できませんでした。");
                        }
                        break;
                    default:
                        // 実行メッセージを送信する
                        sendMessage.sendSender(sender,"引数が不明です。");
                        break;
                }
            }else {
                // 引数の長さが0なら
                // オンラインプレイヤーをリスト化する
                List<Player> players = new ArrayList<>(Bukkit.getServer().getOnlinePlayers());
                Collections.shuffle(players);
                // チーム分けを行う
                for (int i=0;i<players.size();i++) {
                    Player player = players.get(i);
                    ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
                    LeatherArmorMeta meta = (LeatherArmorMeta) chestPlate.getItemMeta();
                    // 現在のチームから退出させる
                    player.getScoreboardTags().remove("siege");
                    player.getScoreboardTags().remove("red");
                    player.getScoreboardTags().remove("RED");
                    player.getScoreboardTags().remove("blue");
                    player.getScoreboardTags().remove("BLUE");
                    if (i < players.size() / 2) {
                        // 赤チームに分類する
                        // 赤色の皮防具を装備させる
                        Objects.requireNonNull(meta).addEnchant(Enchantment.BINDING_CURSE, 1, true);
                        meta.setUnbreakable(true);
                        Objects.requireNonNull(meta).setColor(Color.RED);
                        chestPlate.setItemMeta(meta);
                        player.getInventory().setItem(38,chestPlate);
                        if (i == players.size() / 2 - 1) {
                            // 赤色の大将に分類する
                            sendMessage.sendPlayer(player,"あなたは" + ChatColor.RED + " 赤 " + ChatColor.WHITE + "チームの大将です。");
                            ItemStack itemStack = new ItemStack(Material.DIAMOND_HELMET);
                            player.getInventory().setItem(39,itemStack);
                            player.setPlayerListName(ChatColor.RED + "赤大将 " + player.getName());
                            player.addScoreboardTag("RED");
                        }else {
                            // 赤色の兵士に分類する
                            sendMessage.sendPlayer(player,"あなたは" + ChatColor.RED + " 赤 " + ChatColor.WHITE + "チームです。");
                            player.setPlayerListName(ChatColor.RED + "赤 " + player.getName());
                            player.addScoreboardTag("red");
                        }
                    }else {
                        // 青チームに分類する
                        // 青色の皮防具を装備させる
                        Objects.requireNonNull(meta).addEnchant(Enchantment.BINDING_CURSE, 1, true);
                        meta.setUnbreakable(true);
                        Objects.requireNonNull(meta).setColor(Color.BLUE);
                        chestPlate.setItemMeta(meta);
                        player.getInventory().setItem(38,chestPlate);
                        if (i == players.size() - 1) {
                            // 青色の大将に分類する
                            sendMessage.sendPlayer(player,"あなたは" + ChatColor.BLUE + " 青 " + ChatColor.WHITE + "チームの大将です。");
                            ItemStack itemStack = new ItemStack(Material.DIAMOND_HELMET);
                            player.getInventory().setItem(39,itemStack);
                            player.setPlayerListName(ChatColor.BLUE + "青大将 " + player.getName());
                            player.addScoreboardTag("BLUE");
                        }else {
                            sendMessage.sendPlayer(player,"あなたは" + ChatColor.BLUE + " 青 " + ChatColor.WHITE + "チームです。");
                            player.setPlayerListName(ChatColor.BLUE + "青 " + player.getName());
                            player.addScoreboardTag("blue");
                        }
                    }
                    player.addScoreboardTag("siege");
                }
                // 実行メッセージを送信する
                sendMessage.sendSender(sender,"処理は正常に実行されました。");
                // ゲームを開始する
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getScoreboardTags().contains("siege")) {
                        // ループ中のプレイヤーがSiegeのプレイヤーなら
                        // 試合開始のメッセージを送信する
                        player.sendTitle("試合開始",null,10,40,10);
                        player.playSound(player, Sound.BLOCK_ANVIL_USE,1.0f,1.0f);
                        // 体力・空腹を設定する
                        player.setHealth(20);
                        player.setFoodLevel(20);
                        // ゲームモードを設定する
                        player.setGameMode(GameMode.ADVENTURE);
                        if (player.getScoreboardTags().contains("red") || player.getScoreboardTags().contains("RED")) {
                            String locationString = config.getString("red");
                            String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
                            Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
                            player.teleport(location);
                        }else if (player.getScoreboardTags().contains("blue") || player.getScoreboardTags().contains("BLUE")) {
                            String locationString = config.getString("blue");
                            String[] locParts = Objects.requireNonNull(locationString).split(","); // カンマで区切る
                            Location location= new Location(Bukkit.getWorld(locParts[0]), Double.parseDouble(locParts[1]), Double.parseDouble(locParts[2]), Double.parseDouble(locParts[3]), Float.parseFloat(locParts[4]), Float.parseFloat(locParts[5])); // Location型の変数を宣言
                            player.teleport(location);
                        }
                        // インベントリを設定する
                        Inventory inventory = player.getInventory();
                        for (int i=0;i<43;i++) {
                            // スロットにアイテムを設置する
                            if (config.get("inventory." + i) != null) {
                                // ループ中のスロットにアイテムが設定されているなら
                                Map<String, Object> itemMap = config.getConfigurationSection("inventory." + i).getValues(true);
                                ItemStack itemStack = ItemStack.deserialize(itemMap);
                                inventory.setItem(i,itemStack);
                            }
                        }
                    }
                }
                // 実行メッセージを送信する
                sendMessage.sendSender(sender,"処理は正常に実行されました。");
            }
        }else {
            // 権限を付与されていないなら
            sendMessage.sendSender(sender,"実行権限を確認してください。");
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // タブ補完の処理を書く
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            // 第一引数の補完候補を追加する
            list.add("run");
            list.add("inventory");
            list.add("red");
            list.add("blue");
        }
        return list;
    }
}
