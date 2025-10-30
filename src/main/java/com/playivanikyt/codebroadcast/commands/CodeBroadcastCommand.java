package com.playivanikyt.codebroadcast.commands;

import com.playivanikyt.codebroadcast.CodeBroadcast;
import com.playivanikyt.codebroadcast.utils.HexUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CodeBroadcastCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("codebroadcast.reload")) {
                    CodeBroadcast.getInstance().reloadConfig();
                    sender.sendMessage(HexUtil.translate(CodeBroadcast.getInstance().getConfig().getString("messages.reload")));
                } else {
                    sender.sendMessage(HexUtil.translate(CodeBroadcast.getInstance().getConfig().getString("messages.no-permission")));
                }
            } else {
                sender.sendMessage(HexUtil.translate(CodeBroadcast.getInstance().getConfig().getString("messages.no-args")));
            }

            return true;
        } else {
            sender.sendMessage(HexUtil.translate(CodeBroadcast.getInstance().getConfig().getString("messages.no-args")));
            return false;
        }
    }
}
