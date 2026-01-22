package com.github.tivecs.skillcard.core.builders.book

import com.github.tivecs.skillcard.core.entities.books.TriggerTargetSlotSource
import com.github.tivecs.skillcard.core.entities.skills.Skill
import com.github.tivecs.skillcard.core.entities.triggers.Trigger

class TriggerSkillSetBuilder {

    var trigger: Trigger<*>? = null
    val skills = mutableListOf<Skill>()

    val targetSlotSources = mutableListOf<TriggerTargetSlotSource>()


    fun setTrigger(trigger: Trigger<*>): TriggerSkillSetBuilder {
        this.trigger = trigger
        return this
    }


}