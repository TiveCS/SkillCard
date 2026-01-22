package com.github.tivecs.skillcard.core.builders.book

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.entities.books.SkillBook
import com.github.tivecs.skillcard.core.entities.books.TriggerSkillSet

class SkillBookBuilder {

    var identifier: String = ""
    var displayName: String = ""
    var description: String = ""
    var material: XMaterial = XMaterial.BOOK

    val triggerSkillSets = mutableListOf<TriggerSkillSet>()
    var triggerSkillSetBuilder: TriggerSkillSetBuilder? = null

    fun setIdentifier(identifier: String): SkillBookBuilder {
        this.identifier = identifier
        return this
    }

    fun setDisplayName(displayName: String): SkillBookBuilder {
        this.displayName = displayName
        return this
    }

    fun setDescription(description: String): SkillBookBuilder {
        this.description = description
        return this
    }

    fun setMaterial(material: XMaterial): SkillBookBuilder {
        this.material = material
        return this
    }

    fun newTriggerSkillSetBuilder(): TriggerSkillSetBuilder {
        val triggerSkillSetBuilder = TriggerSkillSetBuilder()
        return triggerSkillSetBuilder
    }

    fun build(): SkillBook {
        TODO()
    }
}