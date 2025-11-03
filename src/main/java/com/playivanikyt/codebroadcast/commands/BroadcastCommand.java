package com.playivanikyt.codebroadcast.commands;

import com.playivanikyt.codebroadcast.CodeBroadcast;
import com.playivanikyt.codebroadcast.utils.HexUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BroadcastCommand implements CommandExecutor {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("codebroadcast.use")) {
            player.sendMessage(HexUtil.translate(papi(player, getString("messages.no-permission",
                    "&6&l▶ &fУ вас нету данной возможности, приобретите донат выше для данной возможности!"))));
            return true;
        }

        int cooldownTime = CodeBroadcast.getInstance().getConfig().getInt("broadcast-cooldown", 60);

        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastUse = cooldowns.get(player.getUniqueId());
            long timeLeft = (lastUse + cooldownTime * 1000L) - System.currentTimeMillis();

            if (timeLeft > 0) {
                long totalSeconds = timeLeft / 1000;
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                String rawMessage = getString("cooldown.message",
                        "&6&l▶ &fПодождите, вам осталось ещё %minutes%м. %seconds% сек!");

                String message = rawMessage
                        .replace("%hours%", hours > 0 ? hours + "ч " : "")
                        .replace("%minutes%", minutes > 0 || hours > 0 ? String.format("%02d", minutes) : "0")
                        .replace("%seconds%", String.format("%02d", seconds))
                        .replace("%time%", formatTime(hours, minutes, seconds));

                player.sendMessage(HexUtil.translate(papi(player, message)));
                return true;
            }
        }

        if (args.length > 0) {
            StringBuilder userMsgBuilder = new StringBuilder();
            for (String arg : args) userMsgBuilder.append(arg).append(" ");
            String rawUserMessage = userMsgBuilder.toString().trim();

            String userMessage = rawUserMessage;

            if (!player.hasPermission("codebroadcast.color")) {
                userMessage = HexUtil.stripColors(userMessage);
            }

            if (!player.hasPermission("codebroadcast.placeholders")) {
                userMessage = userMessage.replaceAll("%[^%]*%", ""); // Удаляет %что-угодно%
            }

            String template = getString("messages.broadcast",
                    "&f[&6Обьявление&f] &f%message% &7(Автор: &e%player_name%&7)");

            String fullMessage = template.replace("%message%", userMessage);
            String withPapi = papi(player, fullMessage);
            String finalMessage = HexUtil.translate(withPapi);

            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendMessage(finalMessage);
            }

            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            return true;
        }

        for (String line : CodeBroadcast.getInstance().getConfig().getStringList("messages.no-args")) {
            if (line == null || line.isEmpty()) continue;
            player.sendMessage(HexUtil.translate(papi(player, line)));
        }
        return true;
    }

    private String getString(String path, String fallback) {
        String value = CodeBroadcast.getInstance().getConfig().getString(path);
        return value != null && !value.isEmpty() ? value : fallback;
    }

    private String papi(Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    private String formatTime(long hours, long minutes, long seconds) {
        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append("ч ");
        if (minutes > 0 || hours > 0) sb.append(String.format("%02d", minutes)).append("м ");
        sb.append(String.format("%02d", seconds)).append("с");
        return sb.toString().trim();
    }
}