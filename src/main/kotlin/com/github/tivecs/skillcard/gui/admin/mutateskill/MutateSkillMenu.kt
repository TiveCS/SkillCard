package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.common.items.OpenSelectAbilityMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillAbilityListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillTargetSlotListMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenSelectMaterialMenuItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object MutateSkillMenu {

    val playerDraftBuilders = mutableMapOf<Player, SkillBuilder>()

    fun invalidate(player: Player) {
        playerDraftBuilders.remove(player)
    }

    fun open(viewer: Player) {
        val builder = playerDraftBuilders.getOrPut(viewer) { SkillBuilder() }

        val gui = Gui.normal()
            .setStructure(
                "123456789",
                "#########",
                "C...B...S"
            )
            .addIngredient(
                '1', OpenInputStringMenuItem(
                    displayName = "&eSet Identifier",
                    material = XMaterial.OAK_SIGN.get() ?: Material.OAK_SIGN,
                    title = "Skill Identifier",
                    lores = listOf(
                        " ",
                        "&7Must be unique among all skills.",
                        " ",
                        "&eCurrent: &f${builder.identifier}"
                    ),
                    initialValue = builder.identifier,
                    onRename = { input -> builder.setIdentifier(input) },
                    onConfirm = { _, _, _ ->
                        open(viewer)
                    },
                )
            )
            .addIngredient(
                '2', OpenInputStringMenuItem(
                    displayName = "&eSet Display Name",
                    material = XMaterial.NAME_TAG.get() ?: Material.NAME_TAG,
                    title = "Skill Display Name",
                    initialValue = builder.displayName,
                    onRename = { input -> builder.setDisplayName(input) },
                    onConfirm = { _, _, _ ->
                        open(viewer)
                    },
                )
            )
            .addIngredient(
                '3', OpenInputStringMenuItem(
                    displayName = "&eSet Description",
                    material = XMaterial.WRITTEN_BOOK.get() ?: Material.WRITTEN_BOOK,
                    title = "Skill Description",
                    initialValue = builder.description ?: "",
                    lores = listOf(" ") + builder.getDescriptionDisplay(),
                    onRename = { input -> builder.setDescription(input) },
                    onConfirm = { _, _, _ ->
                        open(viewer)
                    },
                )
            )
            .addIngredient(
                '4', OpenSelectMaterialMenuItem(
                displayText = "&bSkill Material",
                material = builder.material,
                lores = listOf(
                    " ",
                    "&7Select the material as display item.",
                    " ",
                    "&bCurrent: &f${builder.material.name}"
                ),
                onSelect = { newMaterial ->
                    builder.setMaterial(newMaterial)
                    open(viewer)
                }
            ))
            .addIngredient('5', OpenSkillTargetSlotListMenuItem(skillBuilder = builder))
            .addIngredient(
                '6', OpenSkillAbilityListMenuItem(
                    skillBuilder = builder
                )
            )

            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}