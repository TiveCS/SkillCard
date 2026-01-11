package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.gui.admin.items.OpenAbilityListMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenAdminManageMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSkillBookListMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

object SkillListMenu {
    fun open(viewer: Player) {
        val skillItems = SkillRepository.getAllSkillsFromCache().map { skill ->
            SimpleItem(skill.getItem())
        }

        val gui = PagedGui.items()
            .setStructure(
                "###ABC###",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#########",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_VERTICAL)
            .addIngredient('A', OpenAdminManageMenuItem())
            .addIngredient('B', OpenAbilityListMenuItem())
            .addIngredient('C', OpenSkillBookListMenuItem())
            .setContent(skillItems)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setViewer(viewer)
            .build()

        window.open()
    }
}