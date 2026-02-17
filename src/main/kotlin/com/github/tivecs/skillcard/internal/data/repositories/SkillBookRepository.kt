package com.github.tivecs.skillcard.internal.data.repositories

import com.github.tivecs.skillcard.core.entities.books.SkillBook

object SkillBookRepository {

    private val cached = mutableMapOf<String, SkillBook>()

    fun save(skillBook: SkillBook) {
        cached[skillBook.identifier] = skillBook
    }

    fun getByIdentifier(identifier: String): SkillBook? {
        return cached[identifier]
    }

    fun getAll(): List<SkillBook> {
        return cached.values.sortedBy { it.identifier }.toList()
    }

    fun deleteByIdentifier(identifier: String) {
        cached.remove(identifier)
    }
}