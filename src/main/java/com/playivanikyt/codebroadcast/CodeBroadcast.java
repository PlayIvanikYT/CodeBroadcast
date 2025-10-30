package com.playivanikyt.codebroadcast;

import com.playivanikyt.codebroadcast.commands.BroadcastCommand;
import com.playivanikyt.codebroadcast.commands.CodeBroadcastCommand;
import com.playivanikyt.codebroadcast.utils.HexUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CodeBroadcast extends JavaPlugin {
    public static CodeBroadcast instance;

    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate("&6» &fПлагин &6" + ((CodeBroadcast)getPlugin(CodeBroadcast.class)).getName() + " &fвключился&f!"));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate("&6» &fВерсия: &6v" + ((CodeBroadcast)getPlugin(CodeBroadcast.class)).getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate(""));
        this.getCommand("broadcast").setExecutor(new BroadcastCommand());
        this.getCommand("codebroadcast").setExecutor(new CodeBroadcastCommand());
        this.saveDefaultConfig();
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate("&6» &fПлагин &6" + ((CodeBroadcast)getPlugin(CodeBroadcast.class)).getName() + " &fвыключился&f!"));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate("&6» &fВерсия: &6v" + ((CodeBroadcast)getPlugin(CodeBroadcast.class)).getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(HexUtil.translate(""));
    }

    public static CodeBroadcast getInstance() {
        return instance;
    }
}
