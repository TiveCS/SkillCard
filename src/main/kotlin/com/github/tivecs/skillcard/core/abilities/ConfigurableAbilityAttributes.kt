package com.github.tivecs.skillcard.core.abilities

import com.google.gson.Gson

interface ConfigurableAbilityAttributes {

    fun fromMapAttributes(triggerAttributes: Map<String, Any>, skillAttributes: Map<String, Any>): AbilityAttribute?

}