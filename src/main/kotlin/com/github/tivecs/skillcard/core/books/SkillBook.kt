package com.github.tivecs.skillcard.core.books

import com.github.tivecs.skillcard.internal.data.tables.SkillBookTable
import com.github.tivecs.skillcard.internal.data.tables.SkillTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.UUID

class SkillBook(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SkillBook>(SkillBookTable)
}