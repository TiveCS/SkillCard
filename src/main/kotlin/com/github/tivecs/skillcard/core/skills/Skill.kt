package com.github.tivecs.skillcard.core.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.data.tables.SkillTable
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.*

class Skill(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Skill>(SkillTable)

    var identifier = SkillTable.identifier
    var displayName = SkillTable.displayName
    var description = SkillTable.description
    var material = SkillTable.material

    val item: ItemStack? get() {
        val getMaterial = XMaterial.matchXMaterial(readValues[material])

        if (getMaterial.isEmpty) return null

        val mat = getMaterial.get()

        val itemStack = mat.parseItem() ?: return null

        val meta = itemStack.itemMeta

        meta?.setDisplayName(ChatColor.translateAlternateColorCodes('&', readValues[displayName]))

        val desc = readValues[description]
        val descriptionList: List<String> = desc?.split("\n") ?: emptyList()

        meta?.lore = descriptionList.map { str -> ChatColor.translateAlternateColorCodes('&', str) }

        itemStack.itemMeta = meta

        return itemStack
    }
}