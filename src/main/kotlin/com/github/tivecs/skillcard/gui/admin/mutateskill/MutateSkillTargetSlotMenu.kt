package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.ConfirmCreateSkillTargetSlotItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillTargetSlotListMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object MutateSkillTargetSlotMenu {

    fun open(player: Player, skillBuilder: SkillBuilder) {
        val builder = skillBuilder.targetSlotBuilder ?: skillBuilder.newTargetSlotBuilder()

        val gui = Gui.normal()
            .setStructure(
                "123456789",
                "#########",
                "-B--R--C-"
            )
            .addIngredient('B', OpenSkillTargetSlotListMenuItem(
                displayText = "&eBack to Skill Target Slot List",
                skillBuilder = skillBuilder
            ))
            .addIngredient('1', OpenInputStringMenuItem(
                displayName = "&eSet Slot Identifier",
                material = XMaterial.ARROW.get() ?: Material.ARROW,
                title = "Slot Identifier",
                initialValue = builder.slotIdentifier,
                onRename = { input -> builder.setSlotIdentifier(input) },
                onConfirm = { _, _, _ ->
                    open(player, skillBuilder)
                },
            ))
            .addIngredient('C', ConfirmCreateSkillTargetSlotItem(builder, {
                open(player, skillBuilder)
            }))
            .build()

        val window = Window.single().setGui(gui).setViewer(player).build()

        window.open()
    }

}