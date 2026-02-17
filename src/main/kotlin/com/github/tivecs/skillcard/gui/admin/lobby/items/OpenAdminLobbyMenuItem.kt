package com.github.tivecs.skillcard.gui.admin.lobby.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.gui.admin.lobby.AdminLobbyMenu
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class OpenAdminLobbyMenuItem(
    val displayText: String = "&eOpen Admin Lobby Menu",
) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        AdminLobbyMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider? {
        return ItemBuilder(XMaterial.CLOCK.get() ?: Material.CLOCK)
            .setDisplayName(displayText.colorized())
    }
}