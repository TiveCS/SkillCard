package com.github.tivecs.skillcard.gui.admin.mutateskill

import com.github.tivecs.skillcard.core.builders.skill.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.common.items.OpenSelectAbilityMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillAbilityAttributesMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenMutateSkillMenuItem
import com.github.tivecs.skillcard.gui.admin.mutateskill.items.OpenSkillAbilityListMenuItem
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import xyz.xenondevs.invui.window.Window

object SkillAbilityListMenu {

    fun open(viewer: Player, skillBuilder: SkillBuilder) {
        val abilityBuilder = skillBuilder.abilityBuilder ?: skillBuilder.newAbilityBuilder()

        val registeredAbilities = skillBuilder.abilities.map {
            OpenMutateSkillAbilityAttributesMenuItem(
                displayText = "&aEdit ${it.ability.displayName} Attributes",
                abilityBuilder = skillBuilder.newUnattachedAbilityBuilder(it.ability)
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "#########",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "####+####",
                "-B--R--C-"
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient(
                '+', when (abilityBuilder.ability) {
                    null -> OpenSelectAbilityMenuItem(
                        displayText = "&aAdd New Ability",
                        onSelect = { ability ->
                            abilityBuilder.setAbility(ability)
                            MutateSkillAbilityAttributesMenu.open(viewer, abilityBuilder)
                        },
                        backItem = OpenSkillAbilityListMenuItem(
                            displayText = "&aBack to Skill Ability List",
                            skillBuilder = skillBuilder
                        )
                    )

                    else -> OpenMutateSkillAbilityAttributesMenuItem(
                        abilityBuilder = abilityBuilder
                    )
                }
            )
            .addIngredient('B', OpenMutateSkillMenuItem("&eBack to Skill Manage Menu"))
            .setContent(registeredAbilities)
            .build()

        val window = Window.single().setGui(gui).setViewer(viewer).build()

        window.open()
    }

}