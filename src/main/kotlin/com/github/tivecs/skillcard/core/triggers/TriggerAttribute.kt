package com.github.tivecs.skillcard.core.triggers

interface TriggerAttribute {

    fun toMutableMap(): MutableMap<String, Any>
}