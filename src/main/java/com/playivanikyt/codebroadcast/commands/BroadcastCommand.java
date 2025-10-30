package com.playivanikyt.codebroadcast.commands;

import com.playivanikyt.codebroadcast.CodeBroadcast;
import com.playivanikyt.codebroadcast.utils.HexUtil;
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

    // Храним время последнего использования для каждого игрока
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("codebroadcast.use")) {
            player.sendMessage(HexUtil.translate(CodeBroadcast.getInstance().getConfig().getString("messages.no-permission")));
            return true;
        }

        // Получаем время кулдауна из конфигурации или ставим стандарт 60 сек
        int cooldownTime = CodeBroadcast.getInstance().getConfig().getInt("broadcast-cooldown", 60);

        // Проверяем, есть ли активный кулдаун
        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastUse = cooldowns.get(player.getUniqueId());
            long timeLeft = (lastUse + cooldownTime * 1000L) - System.currentTimeMillis();

            if (timeLeft > 0) {
                long secondsLeft = timeLeft / 1000;

                // Получаем сообщение из конфига или используем стандартное
                String cooldownMessage = CodeBroadcast.getInstance().getConfig().getString(
                        "cooldown.message",
                        "&6&l▶ &fПодождите, вам осталось ещё %time% секунд!"
                );

                cooldownMessage = cooldownMessage.replace("%time%", String.valueOf(secondsLeft));

                player.sendMessage(HexUtil.translate(cooldownMessage));
                return true;
            }
        }

        if (args.length > 0) {
            StringBuilder s = new StringBuilder();
            for (String arg : args) {
                s.append(arg).append(" ");
            }

            String messageTemplate = CodeBroadcast.getInstance().getConfig().getString("messages.broadcast");
            String message = HexUtil.translate(
                    messageTemplate
                            .replace("%player%", player.getName())
                            .replace("%message%", s.toString().trim())
            );

            for (Player pp : Bukkit.getOnlinePlayers()) {
                pp.sendMessage(message);
            }

            // Сохраняем время использования команды
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        } else {
            for (String help : CodeBroadcast.getInstance().getConfig().getStringList("messages.no-args")) {
                player.sendMessage(HexUtil.translate(help));
            }
        }

        return true;
    }
}
