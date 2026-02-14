package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.SkillTargetSlotMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillTargetSlotMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

object SkillTargetSlotListMenu {

    fun open(
        viewer: Player,
        skillBuilder: SkillBuilder,
        searchText: String = "",
        predicate: (SkillTargetSlot) -> Boolean = { true },
        disableCreate: Boolean = false,
        slotItemLores: (targetSlot: SkillTargetSlot) -> List<String> = { emptyList() },
        backItem: Item = OpenMutateSkillMenuItem("&eBack to Mutate Skill Menu"),
        onItemClick: ((player: Player, skillBuilder: SkillBuilder, targetSlot: SkillTargetSlot) -> Unit) = { _, _, _ -> }
    ) {
        var search = searchText

        val items = skillBuilder.targetSlots.filter(predicate).map {
            SkillTargetSlotMenuItem(
                targetSlot = it,
                skillBuilder = skillBuilder,
                onClick = onItemClick,
                lores = slotItemLores(it)
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#B##S##C#"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'S', OpenInputStringMenuItem(
                    displayName = "&aSearch Target Slot",
                    material = XMaterial.COMPASS.get() ?: Material.COMPASS,
                    title = "Search Target Slot",
                    initialValue = search,
                    onRename = { input -> search = input },
                    onConfirm = { _, _, _ -> open(viewer, skillBuilder, search) },
                )
            )
            .addIngredient('B', backItem)
            .addIngredient(
                'C', when (disableCreate) {
                    true -> SimpleItem(ItemBuilder(Material.BARRIER).setDisplayName("&cCannot Create New Target Slot"))
                    false -> OpenMutateSkillTargetSlotMenuItem(
                        displayText = "&aCreate New Target Slot",
                        builder = skillBuilder,
                    )
                }
            )
            .setContent(items)
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}