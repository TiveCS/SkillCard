package com.github.tivecs.skillcard.core.entities.skills

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResult
import com.github.tivecs.skillcard.core.entities.triggers.TriggerExecutionResultStatus
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Skill {

    lateinit var identifier: String
    lateinit var displayName: String
    lateinit var material: XMaterial
    var description: String = ""

    val abilities = arrayListOf<SkillAbility>()
    val targetSlots = arrayListOf<SkillTargetSlot>()

    fun execute(sourceTargetSlots: List<TriggerTargetSlotSource>, triggerResult: TriggerExecutionResult) {
        if (triggerResult.status != TriggerExecutionResultStatus.EXECUTED) return

        abilities.forEach {
            it.execute(sourceTargetSlots, triggerResult)
        }
    }

    fun getDescription(): List<String> {
        if (description.isBlank()) return emptyList()

        return description.split("\n").map { it.trim().colorized() }
    }

    fun getItem(): ItemStack {
        val mat = material.get() ?: Material.BOOK
        val item = ItemStack(mat)
        val meta = item.itemMeta

        meta?.setDisplayName(displayName.trim().colorized())
        meta?.lore = getDescription()

        item.itemMeta = meta

        return item
    }

}