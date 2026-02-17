package com.github.tivecs.skillcard.gui.admin.mutateskillbook

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.book.SkillBookBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.ConfirmMutateSkillBookItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.ConfirmMutateSkillBookSkillSetItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenMutateSkillBookSkillSetMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenSkillBookSkillSetMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskillbook.items.OpenSkillListMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.gui.common.items.OpenSelectMaterialMenuItem
import com.github.tivecs.skillcard.internal.data.repositories.SkillBookRepository
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object MutateSkillBookMenu {

    val drafts = mutableMapOf<Player, SkillBookBuilder>()

    fun invalidate(player: Player) {
        drafts.remove(player)
    }

    fun open(player: Player) {
        val builder = drafts.getOrPut(player) { SkillBookBuilder() }

        val gui = Gui.normal()
            .setStructure(
                "123456789",
                "#########",
                "--------C",
            )
            .addIngredient(
                '1', OpenInputStringMenuItem(
                    displayName = "&aSet Skill Book Identifier",
                    material = XMaterial.NAME_TAG.get() ?: Material.NAME_TAG,
                    title = "Set Skill Book Identifier",
                    initialValue = builder.identifier,
                    onRename = { input -> builder.identifier = input },
                    onConfirm = { _, _, _ -> open(player) },
                    lores = listOf(" ", "&fCurrent: ${builder.identifier}".colorized())
                )
            )
            .addIngredient(
                '2', OpenInputStringMenuItem(
                    displayName = "&aSet Skill Book Display Name",
                    material = XMaterial.WRITABLE_BOOK.get() ?: Material.WRITABLE_BOOK,
                    title = "Set Skill Book Display Name",
                    initialValue = builder.displayName,
                    onRename = { input -> builder.displayName = input },
                    onConfirm = { _, _, _ -> open(player) },
                    lores = listOf(" ", "&fCurrent: ${builder.displayName}".colorized())
                )
            )
            .addIngredient(
                '3', OpenSelectMaterialMenuItem(
                    material = XMaterial.ENCHANTED_BOOK,
                    lores = listOf(" ", "&fCurrent: ${builder.material.name}".colorized()),
                    displayText = "&aSet Skill Book Material",
                    onSelect = { input ->
                        builder.material = input
                        open(player)
                    }
                )
            )
            .addIngredient(
                '4', OpenSkillBookSkillSetMenuItem(
                    skillBookBuilder = builder,
                    onSkillSetItemClick = { player, skillSet ->

                    }
                )
            )
            .addIngredient(
                'C', ConfirmMutateSkillBookItem(
                    bookBuilder = builder,
                    onPostBuild = { player, skillBook ->
                        invalidate(player)

                        SkillBookRepository.save(skillBook)
                        player.closeInventory()

                        player.sendMessage("Skill Book &b${skillBook.displayName}&f has been saved successfully!".colorized())
                    }
                )
            )
            .build()

        val window = Window.single()
            .setTitle("Skill Book Editor")
            .setGui(gui)
            .setViewer(player)
            .build()

        window.open()
    }

}