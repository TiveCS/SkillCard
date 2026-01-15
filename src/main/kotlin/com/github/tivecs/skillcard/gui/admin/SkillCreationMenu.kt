package com.github.tivecs.skillcard.gui.admin

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.ConfirmSkillCreateItem
import com.github.tivecs.skillcard.gui.admin.items.OpenSelectTriggerMenuItem
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

    fun invalidate(playerId: UUID) {
        currentDraftSkills.remove(playerId)
    }

    fun open(viewer: Player) {
        val playerSkillDraft = currentDraftSkills[viewer.uniqueId] ?: SkillBuilder()
        currentDraftSkills[viewer.uniqueId] = playerSkillDraft

        val gui = Gui.normal()
            .setStructure(
                "ABCDEF..X"
            )
            .addIngredient(
                'A', OpenInputStringMenuItem(
                    displayName = "&e&lSet Identifier",
                    material = XMaterial.NAME_TAG.get() ?: Material.NAME_TAG,
                    title = "Input Skill's Ability Identifier",
                    initialValue = playerSkillDraft.identifier ?: "",
                    lores = listOf(" ", "&aCurrent Value:", "&f${playerSkillDraft.identifier ?: "&c&lNot Set"}"),
                    onRename = { input ->
                        playerSkillDraft.identifier = input
                    },
                    onConfirm = { _, _, _ -> open(viewer) }
                ))
            .addIngredient(
                'B', OpenInputStringMenuItem(
                    displayName = "&e&lSet Display Name",
                    material = XMaterial.PAPER.get() ?: Material.PAPER,
                    title = "Input Skill's Ability Display Name",
                    initialValue = playerSkillDraft.displayName ?: "",
                    lores = listOf(" ", "&aCurrent Value:", "&f${playerSkillDraft.displayName ?: "&c&lNot Set"}"),
                    onRename = { input ->
                        playerSkillDraft.displayName = input
                    },
                    onConfirm = { _, _, _ -> open(viewer) }
                ))
            .addIngredient(
                'C', OpenInputStringMenuItem(
                    displayName = "&e&lSet Description",
                    material = XMaterial.PAPER.get() ?: Material.PAPER,
                    title = "Input Skill's Ability Description",
                    initialValue = playerSkillDraft.description ?: "",
                    onRename = { input ->
                        playerSkillDraft.description = input
                    },
                    onConfirm = { _, _, _ -> open(viewer) }
                ))
            .addIngredient(
                'D', OpenSelectMaterialMenuItem(
                    "&6&lSet Material",
                    onSelect = { material ->
                        playerSkillDraft.material = material
                        open(viewer)
                    },
                ))
            .addIngredient(
                'E', OpenSelectTriggerMenuItem(
                    onSelect = { triggerIdentifier ->
                        TODO()
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