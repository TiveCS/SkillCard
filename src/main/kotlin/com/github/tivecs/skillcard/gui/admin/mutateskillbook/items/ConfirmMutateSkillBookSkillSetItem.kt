package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.TriggerSkillSetBuilder
import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmMutateSkillBookSkillSetItem(
    val displayText: String = "&aConfirm Create Trigger Skill Set",
    val skillSetBuilder: TriggerSkillSetBuilder,
    val onPostConfirm: ((
        player: Player,
        skillSetBuilder: TriggerSkillSetBuilder,
        skillSet: TriggerSkillSet
    ) -> Unit) = { _, _, _ -> }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        if (!skillSetBuilder.isValid()) {
            return
        }

        val skillBookBuilder = skillSetBuilder.skillBookBuilder
        skillBookBuilder.triggerSkillSetBuilder = null

        val skillSet = skillSetBuilder.build()
        skillBookBuilder.triggerSkillSets.add(skillSet)

        onPostConfirm(player, skillSetBuilder, skillSet)
    }

    override fun getItemProvider(): ItemProvider? {
        val validationErrors = skillSetBuilder.validate()
        val isValid = validationErrors.isEmpty()

        val mat = if (isValid) {
            XMaterial.EMERALD_BLOCK.get() ?: Material.EMERALD_BLOCK
        } else {
            XMaterial.BARRIER.get() ?: Material.BARRIER
        }

        val itemBuilder = ItemBuilder(mat)
            .setDisplayName(displayText.colorized())

        if (!isValid) {
            itemBuilder.addLoreLines(" ")

            validationErrors.forEach { error ->
                itemBuilder.addLoreLines("&7- &c$error".colorized())
            }
        }

        return itemBuilder
    }
}