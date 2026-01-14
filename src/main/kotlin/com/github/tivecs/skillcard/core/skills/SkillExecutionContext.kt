package com.github.tivecs.skillcard.core.skills

import com.github.tivecs.skillcard.core.books.SkillBook
import com.github.tivecs.skillcard.core.books.SkillBookExecutionContext
import org.bukkit.event.Event

data class SkillExecutionContext<TEvent : Event>(
    val skillBook: SkillBook,
    val skillBookContext: SkillBookExecutionContext<TEvent>
)