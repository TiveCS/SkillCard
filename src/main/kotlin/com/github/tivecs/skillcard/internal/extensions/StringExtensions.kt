package com.github.tivecs.skillcard.internal.extensions

import org.bukkit.ChatColor

fun String.colorized(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
}