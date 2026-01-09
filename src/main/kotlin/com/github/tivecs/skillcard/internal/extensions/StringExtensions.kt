package com.github.tivecs.skillcard.internal.extensions

import org.bukkit.ChatColor

fun String.colorized(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}