package com.github.tivecs.skillcard.core.builders.book

import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet
import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.core.entities.triggers.Trigger
import org.bukkit.event.Event
import java.util.UUID

class TriggerSkillSetBuilder(val skillBookBuilder: SkillBookBuilder) {

    var trigger: Trigger<*>? = null
    val skills = mutableListOf<Skill>()

    val targetSlotSources = mutableListOf<TriggerTargetSlotSource>()
    var targetSlotSourceBuilder: TriggerTargetSlotSourceBuilder? = null

    fun registerSkills(vararg skills: Skill) {
        this.skills.addAll(skills)
    }

    fun setTrigger(trigger: Trigger<*>): TriggerSkillSetBuilder {
        this.trigger = trigger
        return this
    }

    fun newTriggerTargetSlotSourceBuilder(): TriggerTargetSlotSourceBuilder {
        val newBuilder = TriggerTargetSlotSourceBuilder(this)
        targetSlotSourceBuilder = newBuilder
        return newBuilder
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (trigger == null) {
            errors.add("Trigger is required")
        }

        if (skills.isEmpty()) {
            errors.add("At least one skill is required")
        }

        if (targetSlotSources.isEmpty()) {
            errors.add("At least one target slot source is required")
        }

        return errors
    }

    fun isValid(): Boolean {
        return validate().isEmpty()
    }

    @Suppress("UNCHECKED_CAST")
    fun build(): TriggerSkillSet {
        val validationErrors = validate()
        if (validationErrors.isNotEmpty()) {
            throw IllegalStateException(
                "Cannot build TriggerSkillSet due to validation errors: ${
                    validationErrors.joinToString(
                        "; "
                    )
                }"
            )
        }

        return TriggerSkillSet(UUID.randomUUID()).apply {
            this.trigger = this@TriggerSkillSetBuilder.trigger as Trigger<Event>
            this.triggerIdentifier = trigger.identifier
            this.skills.addAll(this@TriggerSkillSetBuilder.skills)

            this.targetSlotSources.addAll(this@TriggerSkillSetBuilder.targetSlotSources.map { source ->
                source.triggerSkillSet = this
                return@map source
            })

        }
    }

}