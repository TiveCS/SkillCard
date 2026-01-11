package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.SkillListMenu
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenSkillListMenuItem : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        SkillListMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = XMaterial.NETHER_STAR.get() ?: Material.NETHER_STAR
        val skillsCount  = SkillRepository.getAllIdentifiers().size

        return ItemBuilder(mat)
            .setDisplayName("&b&lSkill List Menu".colorized())
            .addLoreLines(" ")
            .addLoreLines("&7Registered &e$skillsCount skill(s)".colorized())
    }
}