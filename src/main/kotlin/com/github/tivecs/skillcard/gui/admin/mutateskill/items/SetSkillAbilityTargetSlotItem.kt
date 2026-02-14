package com.github.tivecs.skillcard.gui.admin.mutateskill.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.converters.TypeConverters
import com.github.tivecs.skillcard.gui.admin.mutateskill.MutateSkillAbilityAttributesMenu
import com.github.tivecs.skillcard.gui.admin.mutateskill.SkillTargetSlotListMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class SetSkillAbilityTargetSlotItem(
    val displayText: String = "&aSet Skill Ability Target Slot",
    val material: XMaterial = XMaterial.PRISMARINE_SHARD,
    val skillAbilityBuilder: SkillAbilityBuilder
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val ability = skillAbilityBuilder.ability ?: return

        SkillTargetSlotListMenu.open(
            viewer = player,
            skillBuilder = skillAbilityBuilder.skillBuilder,
            disableCreate = true,
            backItem = OpenMutateSkillAbilityAttributesMenuItem(
                displayText = "&cBack to Skill Ability Attributes Menu",
                abilityBuilder = skillAbilityBuilder
            ),
            onItemClick = { p, skillBuilder, targetSlot ->
                skillAbilityBuilder.addTargetSlot(targetSlot.identifier)
                MutateSkillAbilityAttributesMenu.open(p, skillAbilityBuilder)
            },
            slotItemLores = { targetSlot ->
                listOf(" ", "&fClick to set target slot to '${targetSlot.identifier}'".colorized())
            },
            predicate = { targetSlot ->
                targetSlot.targetType == null ||
                ability.targetRequirement.acceptedSourceTypes.contains(targetSlot.targetType)
            }
        )
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = material.get() ?: Material.PRISMARINE_SHARD

        return ItemBuilder(mat)
            .setDisplayName(displayText.colorized())
            .addLoreLines(" ", "&fCurrent: ${skillAbilityBuilder.usedTargetSlots.firstOrNull()?.identifier}".colorized())

    }
}