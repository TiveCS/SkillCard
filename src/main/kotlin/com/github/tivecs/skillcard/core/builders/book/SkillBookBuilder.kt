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
        val newBuilder = TriggerSkillSetBuilder(this)
        triggerSkillSetBuilder = newBuilder
        return newBuilder
    }

    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (identifier.isBlank()) {
            errors.add("Identifier is required")
        }

        if (displayName.isBlank()) {
            errors.add("Display name is required")
        }

        if (triggerSkillSets.isEmpty()) {
            errors.add("At least one skill set is required")
        }

        return errors
    }

    fun build(): SkillBook {
        val errors = validate()

        if (errors.isNotEmpty()) {
            throw IllegalStateException("Cannot build SkillBook due to validation errors: ${errors.joinToString("; ")}")
        }

        return SkillBook().apply {
            this.identifier = this@SkillBookBuilder.identifier
            this.displayName = this@SkillBookBuilder.displayName
            this.description = this@SkillBookBuilder.description
            this.material = this@SkillBookBuilder.material

            this.skillSets.clear()
            this.skillSets.addAll(this@SkillBookBuilder.triggerSkillSets)
        }
    }
}