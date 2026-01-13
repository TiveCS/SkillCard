package com.github.tivecs.skillcard.gui.admin

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.ConfirmSkillCreateItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenSelectMaterialMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window
import java.util.UUID

object SkillCreationMenu {
    private val currentDraftSkills = mutableMapOf<UUID, SkillBuilder>()

    fun open(viewer: Player) {
        val playerSkillDraft = currentDraftSkills[viewer.uniqueId] ?: SkillBuilder()
        currentDraftSkills[viewer.uniqueId] = playerSkillDraft

        val gui = Gui.normal()
            .setStructure(
                "ABCD....X"
            )
            .addIngredient('A', OpenInputStringMenuItem(
                displayName = "&e&lSet Identifier",
                material = XMaterial.NAME_TAG.get() ?: Material.NAME_TAG,
                title =  "Input Skill's Ability Identifier",
                lores = listOf("&a"),
                onRename = { input ->
                    playerSkillDraft.identifier = input
                    open(viewer)
                }))
            .addIngredient('B', OpenInputStringMenuItem(
                displayName = "&e&lSet Display Name",
                material = XMaterial.PAPER.get() ?: Material.PAPER,
                title =  "Input Skill's Ability Display Name",
                onRename = { input ->
                    playerSkillDraft.displayName = input
                    open(viewer)
                }
            ))
            .addIngredient('C', OpenInputStringMenuItem(
                displayName = "&e&lSet Description",
                material = XMaterial.PAPER.get() ?: Material.PAPER,
                title =  "Input Skill's Ability Description",
                onRename = { input ->
                    playerSkillDraft.description = input
                    open(viewer)
                }
            ))
            .addIngredient('D', OpenSelectMaterialMenuItem(
                "&6&lSet Material",
                onSelect = { material ->
                    playerSkillDraft.material = material
                    open(viewer)
                }
            ))
            .addIngredient('X', ConfirmSkillCreateItem(playerSkillDraft))
            .build()

        val window = Window.single()
            .setViewer(viewer)
            .setGui(gui)
            .build()

        window.open()
    }

}