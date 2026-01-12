package com.github.tivecs.skillcard.gui.admin.items

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.SkillListMenu
import com.github.tivecs.skillcard.internal.data.repositories.SkillRepository
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class ConfirmSkillCreateItem(val builder: SkillBuilder) : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        val isValid = builder.validate().isEmpty()

        if (!isValid) return

        val newSkill = builder.build(player) ?: return

        SkillRepository.create(newSkill)

        SkillListMenu.open(player)
    }

    override fun getItemProvider(): ItemProvider {
        val errors = builder.validate()
        val isValid = errors.isNotEmpty()

        val mat: Material = when (isValid) {
            true -> builder.material?.get()
            false -> XMaterial.BARRIER.get()
        } ?: Material.BARRIER

        val displayName = when (isValid) {
            true -> builder.displayName ?: "???"
            false -> builder.displayName + " &c[Missing Required Fields]"
        }
        val description: MutableList<String> = when (isValid) {
            true -> builder.description?.split("\n")?.map { it.colorized() }?.toMutableList() ?: mutableListOf()
            false -> errors.map { "&7- $it".colorized() }.toMutableList()
        }

        if (isValid) {
            description.add(" ")
            description.add("&aClick to Confirm Create Skill")
        }

        return ItemBuilder(mat)
            .setDisplayName(displayName.colorized())
            .setLegacyLore(description)
    }
}