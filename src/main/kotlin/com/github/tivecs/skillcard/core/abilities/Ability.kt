package com.github.tivecs.skillcard.core.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.extensions.capitalizeWords
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface Ability<TAttribute> where TAttribute : AbilityAttribute {

    val identifier: String

    val displayName: String
        get() = identifier.replace("_", " ").trim().capitalizeWords()

    val material: Material
        get() = XMaterial.ENCHANTED_BOOK.get() ?: Material.ENCHANTED_BOOK

    val description: String
        get() = "&fNo description provided."

    fun execute(attribute: TAttribute): AbilityExecuteResultState

    fun displayItem(): ItemStack {
        val mat = material
        val item = ItemStack(mat)
        val meta = item.itemMeta

        meta?.setDisplayName("&e$displayName".colorized())
        meta?.lore = listOf(description.colorized())

        item.itemMeta = meta

        return item
    }

    fun getAttributePath(key: String): String {
        return identifier + "_" + key
    }
}