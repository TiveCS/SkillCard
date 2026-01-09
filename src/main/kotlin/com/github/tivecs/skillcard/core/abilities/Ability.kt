package com.github.tivecs.skillcard.core.abilities

interface Ability<TAttribute> where TAttribute : Class<*> {

    val identifier: String

    fun execute(attribute: TAttribute): AbilityExecuteResult

}