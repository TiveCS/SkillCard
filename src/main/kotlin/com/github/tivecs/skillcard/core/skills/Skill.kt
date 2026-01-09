package com.github.tivecs.skillcard.core.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.UUID

class Skill {

    val skillId: UUID
    lateinit var identifier: String
    lateinit var material: XMaterial
    lateinit var displayName: String
    lateinit var description: String

    val displayDescription get() = description.split("\n").map { it.trim().colorized() }

    // List of Abilities
    val abilities = mutableListOf<SkillAbility>()

    constructor(skillId: UUID) {
        this.skillId = skillId
    }

    fun execute() {
        abilities.sortBy { ab -> ab.executionOrder }
        abilities.forEach { ab -> ab.execute(ab) }
    }

    fun getItem(): ItemStack {
        val mat = material.get() ?: Material.BOOK
        val item = ItemStack(mat)
        val meta = item.itemMeta

        meta?.setDisplayName(displayName.colorized())
        meta?.lore = displayDescription

        item.itemMeta = meta

        return item
    }

    companion object {
        fun create(
            identifier: String,
            abilities: List<SkillAbility>,
            material: XMaterial = XMaterial.BOOK,
            displayName: String = identifier.replace("_", " ").trim(),
            description: String = "",
        ): Skill {

            val skillId = UUID.randomUUID()
            return Skill(skillId).apply {
                this.identifier = identifier
                this.abilities.addAll(abilities)
                this.displayName = displayName
                this.description = description
                this.material = material
            }
        }
    }
}