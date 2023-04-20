package amedouhu.siegeplugin;

import amedouhu.siegeplugin.commands.Siege;
import amedouhu.siegeplugin.events.PlayerDeathEvent;
import amedouhu.siegeplugin.events.PlayerJoinEvent;
import amedouhu.siegeplugin.events.PlayerRespawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public final class SiegePlugin extends JavaPlugin {
    private static JavaPlugin plugin;
    @Override
    public void onEnable() {
        // プラグイン起動時の処理
        super.onEnable();
        saveDefaultConfig();
        plugin = this;
        // 権限の登録
        Permission permission = new Permission("siege");
        permission.setDefault(PermissionDefault.OP);
        Bukkit.getServer().getPluginManager().addPermission(permission);
        // コマンドの登録
        getCommand("siege").setExecutor(new Siege());
        // イベントの労六
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinEvent(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEvent(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerRespawnEvent(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static JavaPlugin getPlugin() {return plugin;}
}
