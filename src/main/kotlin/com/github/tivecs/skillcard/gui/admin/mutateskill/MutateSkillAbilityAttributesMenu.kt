package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.ConfirmAddSkillAbilityItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.EditSkillAbilityAttributeValueItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillAbilityListMenuItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object MutateSkillAbilityAttributesMenu {

    fun open(viewer: Player, abilityBuilder: SkillAbilityBuilder) {

        val attributeItems = abilityBuilder.ability?.configurableRequirements?.map {
            EditSkillAbilityAttributeValueItem(
                abilityBuilder = abilityBuilder,
                requirement = it,
                initialValue = abilityBuilder.attributes[it.key] ?: it.defaultValue,
                onValueChange = { input -> abilityBuilder.setAttributeValue(it, input) },
                onConfirm = {
                    open(viewer, abilityBuilder)
                }
            )
        } ?: emptyList()

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#B##R##S#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'B', OpenSkillAbilityListMenuItem(
                    displayText = "&cBack to Ability List",
                    skillBuilder = abilityBuilder.skillBuilder
                )
            )
            .addIngredient('S', ConfirmAddSkillAbilityItem(abilityBuilder))
            .setContent(attributeItems)
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}