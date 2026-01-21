package com.github.tivecs.skillcard.core.entities.abilities

import com.github.tivecs.skillcard.core.converters.TypeConverters
import kotlin.reflect.KClass

data class AbilityRequirement(
    val key: String,
    val targetType: KClass<*>,
    val source: RequirementSource,
    val converterName: String? = null,  // Optional named converter
    val required: Boolean = true,
    val defaultValue: Any? = null,
    val choices: List<Any>? = null,
    val description: String? = null
) {
    /**
     * Auto-resolved from TypeConverters registry
     */
    val acceptedSourceTypes: List<KClass<*>>
        get() = if (source == RequirementSource.TRIGGER) {
            TypeConverters.getAcceptableSourceTypes(targetType, converterName)
        } else {
            listOf(targetType)
        }
}