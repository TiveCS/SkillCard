package com.github.tivecs.skillcard.gui.admin.mutateskillbook.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.core.entities.books.SkillBook
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmMutateSkillBookItem(
    val bookBuilder: SkillBookBuilder,
    val onPostBuild: (Player, SkillBook) -> Unit = { _, _ -> }
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val validationErrors = bookBuilder.validate()

        if (validationErrors.isNotEmpty()) {
            return
        }

        val skillBook = bookBuilder.build()
        onPostBuild(player, skillBook)
    }

    override fun getItemProvider(): ItemProvider? {
        val validationErrors = bookBuilder.validate()

        val mat =
            if (validationErrors.isEmpty())
                XMaterial.DIAMOND.get() ?: Material.DIAMOND
            else
                XMaterial.BARRIER.get() ?: Material.BARRIER

        val itemBuilder = ItemBuilder(mat)
            .setDisplayName("&aConfirm Create Skill Book".colorized())

        if (validationErrors.isNotEmpty()) {
            itemBuilder.setLegacyLore(
                listOf(" ") + validationErrors.map { "&7- &c$it".colorized() }
            )
        }

        return itemBuilder
    }
}