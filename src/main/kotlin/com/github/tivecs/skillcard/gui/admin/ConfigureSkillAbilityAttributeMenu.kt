package com.github.tivecs.skillcard.gui.admin

import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.skills.SkillAbilityBuilder
import com.github.tivecs.skillcard.core.skills.SkillBuilder
import com.github.tivecs.skillcard.gui.admin.items.ConfirmConfigureAbilityItem
import com.github.tivecs.skillcard.gui.admin.items.OpenAbilityRequirementInputMenuItem
import com.github.tivecs.skillcard.gui.admin.items.OpenAddAbilityToSkillMenuItem
import com.github.tivecs.skillcard.gui.common.items.AbilityDisplayItem
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

object ConfigureSkillAbilityAttributeMenu {

    private val draft = mutableMapOf<Player, MutableMap<Ability<*>, SkillAbilityBuilder>>()

    fun open(viewer: Player, builder: SkillBuilder, ability: Ability<*>, onComplete: () -> Unit) {
        val configurableRequirements = ability.getRequirements().filter {
            it.source == RequirementSource.USER_CONFIGURED
        }

        val abilityBuilder = draft
            .computeIfAbsent(viewer) { mutableMapOf() }
            .computeIfAbsent(ability) { SkillAbilityBuilder(builder.skillId, ability) }

        val requirementItems = configurableRequirements.map {
            OpenAbilityRequirementInputMenuItem(
                requirement = it,
                skillBuilder = builder,
                abilityBuilder = abilityBuilder,
            )
        }

        val gui = PagedGui.items()
            .setStructure(
                "####I####",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "<xxxxxxx>",
                "#########",
                "#..BRS..#",
            )
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('I', AbilityDisplayItem(ability))
            .addIngredient('B', OpenAddAbilityToSkillMenuItem(builder, "&e&lCancel and Back to Ability List"))
            .addIngredient('S', ConfirmConfigureAbilityItem(builder, abilityBuilder, onComplete))
            .setContent(requirementItems)
            .build()

        val window = Window.single()
            .setGui(gui)
            .setTitle("Configure Skill Ability")
            .setViewer(viewer)
            .build()

        window.open()
    }

}