package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.AbilityListMenu
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenAbilityListMenuItem : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        AbilityListMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider? {
        val mat = XMaterial.BLAZE_POWDER.get() ?: Material.BLAZE_POWDER
        val abilitiesCount = AbilityRepository.count()

        return ItemBuilder(mat)
            .setDisplayName("&c&lAbility List Menu".colorized())
            .addLoreLines(" ")
            .addLoreLines("&7Registered &e$abilitiesCount abilities".colorized())
    }

}