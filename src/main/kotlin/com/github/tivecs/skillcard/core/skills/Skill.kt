package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.abilities.Ability

class Skill {

    val identifier: String
    val abilities = arrayListOf<Ability>()

    constructor(identifier: String) {
        this.identifier = identifier
    }

    fun execute() {}
}