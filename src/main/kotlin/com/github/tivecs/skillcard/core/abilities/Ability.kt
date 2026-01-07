package com.github.tivecs.skillcard.core.abilities

interface Ability<TAttribute> {

    val identifier: String

    fun execute(attribute: TAttribute): AbilityExecuteResult

}