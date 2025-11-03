package com.playivanikyt.codebroadcast.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern X_PATTERN = Pattern.compile("&x&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])");

    public static String translate(String message) {
        if (message == null) return "";

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        if (version.compareTo("v1_16") >= 0) {
            // &#rrggbb
            Matcher matcher = HEX_PATTERN.matcher(message);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String hex = matcher.group(1);
                matcher.appendReplacement(buffer, ChatColor.of("#" + hex).toString());
            }
            matcher.appendTail(buffer);
            message = buffer.toString();

            // &x&r&r&g&g&b&b
            matcher = X_PATTERN.matcher(message);
            buffer = new StringBuffer();
            while (matcher.find()) {
                StringBuilder hex = new StringBuilder("#");
                for (int i = 1; i <= 6; i++) {
                    hex.append(matcher.group(i));
                }
                matcher.appendReplacement(buffer, ChatColor.of(hex.toString()).toString());
            }
            matcher.appendTail(buffer);
            message = buffer.toString();
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String stripColors(String message) {
        if (message == null) return "";
        message = HEX_PATTERN.matcher(message).replaceAll("");
        message = X_PATTERN.matcher(message).replaceAll("");
        return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
    }
}