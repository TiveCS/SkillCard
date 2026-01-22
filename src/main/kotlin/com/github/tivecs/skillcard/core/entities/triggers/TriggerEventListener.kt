package com.github.tivecs.skillcard.core.entities.triggers

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.builtin.abilities.DashAbility
import com.github.tivecs.skillcard.core.builtin.abilities.DashAbilityDirection
import com.github.tivecs.skillcard.core.builtin.triggers.OnAttackTrigger
import com.github.tivecs.skillcard.core.entities.abilities.Ability
import com.github.tivecs.skillcard.core.entities.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.entities.books.SkillBook
import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.core.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core.entities.skills.SkillTargetSlot
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

@Suppress("UNCHECKED_CAST")
class TriggerEventListener : Listener {

    companion object {
        val onDamageTrigger = OnAttackTrigger

        val testSkill = Skill().apply {
            identifier = "test_skill"
            material = XMaterial.NETHER_STAR
            displayName = "Test Skill"

            val targetSlot1 = SkillTargetSlot(
                identifier = "target_1",
            ).apply {
                targetAbilities = mutableListOf()
            }

            targetSlots.add(targetSlot1)

            val testAbility = DashAbility

            val testSkillAbility = SkillAbility().apply {
                abilityIdentifier = testAbility.identifier
                ability = testAbility as Ability<AbilityAttribute>
                targetSlot = targetSlot1
                targetSlotIdentifier = targetSlot1.identifier
            }

            testSkillAbility.attributes[DashAbility.REQUIREMENT_FORCE.key] = 10.0
            testSkillAbility.attributes[DashAbility.REQUIREMENT_DIRECTION.key] = DashAbilityDirection.FORWARD.name

            abilities.add(testSkillAbility)
        }

        val triggerTargetSlotSource = TriggerTargetSlotSource(
            skillIdentifier = testSkill.identifier,
            targetSlotIdentifier = testSkill.targetSlots[0].identifier
        ).apply {
            skill = testSkill
            targetSlot = testSkill.targetSlots[0]
            targetKey = OnAttackTrigger.AVAILABLE_TARGET_ATTACKER
        }

        val testSkillSet = TriggerSkillSet(UUID.randomUUID()).apply {
            triggerIdentifier = onDamageTrigger.identifier
            trigger = onDamageTrigger as Trigger<Event>

            skills.add(testSkill)

            targetSlotSources.add(triggerTargetSlotSource)
            triggerTargetSlotSource.triggerSkillSet = this
        }

        val skillBook = SkillBook().apply {
            name = "test_skill_book"
            displayName = "Test"
            material = XMaterial.BOOK
            description = "A test skill book."

            skillSets.add(testSkillSet)
        }
    }

    @EventHandler
    fun onDamageTriggerHandler(event: EntityDamageByEntityEvent) {
        val triggerResult = onDamageTrigger.handle(event)

        skillBook.execute(triggerResult)
    }
}