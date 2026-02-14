package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builders.skill.SkillAbilityBuilder
import com.github.tivecs.skillcard.gui.admin.common.items.OpenSelectAbilityMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.ConfirmAddSkillAbilityItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.EditSkillAbilityAttributeValueItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillAbilityListMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.ResetEditSkillAttributeItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.SetSkillAbilityTargetSlotItem
import com.github.tivecs.skillcard.gui.common.InputStringMenu
import com.github.tivecs.skillcard.gui.common.items.OpenInputStringMenuItem
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
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

        val currentAbility = abilityBuilder.ability;

        val gui = PagedGui.items()
            .setStructure(
                "####I####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####R####",
                "#B12345S#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                'I', when (currentAbility) {
                    null -> SimpleItem(
                        ItemBuilder(Material.BARRIER)
                            .setDisplayName("&cInvalid Ability (NULL)".colorized())
                    )

                    else -> SimpleItem(
                        ItemBuilder(currentAbility.material)
                            .setDisplayName(currentAbility.displayName.colorized())
                            .addLoreLines(" ", *currentAbility.getDescriptionDisplay().toTypedArray())
                    )
                }
            )
            .addIngredient(
                'B', OpenSkillAbilityListMenuItem(
                    displayText = "&cBack to Ability List",
                    skillBuilder = abilityBuilder.skillBuilder
                )
            )
            .addIngredient(
                '1', SetSkillAbilityTargetSlotItem(
                    skillAbilityBuilder = abilityBuilder
                )
            )
            .addIngredient(
                'R', ResetEditSkillAttributeItem(
                    skillAbilityBuilder = abilityBuilder,
                    onReset = { player, skillAbilityBuilder, skillAbility ->
                        open(player, abilityBuilder)
                    }
                )
            )
            .addIngredient('S', ConfirmAddSkillAbilityItem(abilityBuilder))
            .setContent(attributeItems)
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}