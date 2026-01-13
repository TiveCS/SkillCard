package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.triggers.Trigger

@Suppress("UNCHECKED_CAST")
data class SkillExecutionContext(
    val trigger: Trigger<*, *>,
    private val triggerAttributes: MutableMap<String, Any>,
    private val skillAttributes: MutableMap<String, Any>) {

    fun <TValue> getTriggerAttribute(key: String): TValue? {
        return triggerAttributes[key] as TValue
    }

    fun <TValue> getSkillAttribute(key: String): TValue? {
        return skillAttributes[key] as TValue
    }

}