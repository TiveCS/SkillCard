package com.github.tivecs.skillcard.gui.inventory

import com.github.tivecs.skillcard.gui.inventory.items.SkillBookEquippedSlotItem
import com.github.tivecs.skillcard.gui.inventory.items.SkillBookFillableSlotItem
import com.github.tivecs.skillcard.internal.data.repositories.PlayerInventoryRepository
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object PlayerSkillInventoryMenu {

    fun open(owner: Player, viewer: Player) {
        val inventory = PlayerInventoryRepository.getOrCreate(owner.uniqueId)

        val items = inventory.slots
            .map { slot -> slot.key to slot.value }
            .sortedBy { it.first }
            .map { (_, slot) ->
                when (slot.bookId) {
                    null -> SkillBookFillableSlotItem(slot)
                    else -> SkillBookEquippedSlotItem(slot)
                }
            }

        val gui = PagedGui.items()
            .setStructure(
                "# # # # # # # # #",
                "# x x x x x x x #",
                "# x x x x x x x #",
                "# # # < # > # # #")
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .setContent(items)
            .build()

        Window.single()
            .setGui(gui)
            .setTitle("${owner.name}'s inventory")
            .build(viewer)
            .open()
    }
}