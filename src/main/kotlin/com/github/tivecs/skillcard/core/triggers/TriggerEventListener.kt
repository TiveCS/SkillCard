package com.github.tivecs.skillcard.core.triggers

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core2.builtin.abilities.ThunderAbility
import com.github.tivecs.skillcard.core2.builtin.triggers.OnDamageTrigger
import com.github.tivecs.skillcard.core2.entities.abilities.Ability
import com.github.tivecs.skillcard.core2.entities.books.SkillBook
import com.github.tivecs.skillcard.core2.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.core2.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core2.entities.skills.Skill
import com.github.tivecs.skillcard.core2.entities.skills.SkillAbility
import com.github.tivecs.skillcard.core2.entities.skills.SkillTargetSlot
import com.github.tivecs.skillcard.core2.entities.triggers.Trigger
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

@Suppress("UNCHECKED_CAST")
class TriggerEventListener : Listener {

    companion object {
        val onDamageTrigger = OnDamageTrigger()

        val testSkill = Skill().apply {
            identifier = "test_skill"
            material = XMaterial.NETHER_STAR
            displayName = "Test Skill"

            val targetSlot1 = SkillTargetSlot(
                identifier = "target_1",
                skillIdentifier = "test_skill",
            ).apply {
                targetAbilities = mutableListOf()
            }

            targetSlots.add(targetSlot1)

            val thunderAbility = ThunderAbility()

            abilities.add(SkillAbility().apply {
                abilityIdentifier = thunderAbility.identifier
                ability = thunderAbility as Ability<AbilityAttribute>
                targetSlot = targetSlot1
                targetSlotIdentifier = targetSlot1.identifier
            })
        }

        val triggerTargetSlotSource = TriggerTargetSlotSource(
            skillIdentifier = testSkill.identifier,
            targetSlotIdentifier = testSkill.targetSlots[0].identifier
        ).apply {
            skill = testSkill
            targetSlot = testSkill.targetSlots[0]
            targetKey = OnDamageTrigger.AVAILABLE_TARGET_ATTACKER
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