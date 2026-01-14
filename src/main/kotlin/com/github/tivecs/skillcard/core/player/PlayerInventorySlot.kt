package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.core.books.SkillBook
import com.github.tivecs.skillcard.core.books.SkillBookExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerExecutionContext
import org.bukkit.event.Event
import java.util.UUID

class PlayerInventorySlot {

    val slotId: UUID
    val inventoryId: UUID
    val slotIndex: Int

    var locked: Boolean = false
    var bookId: UUID? = null

    var book: SkillBook? = null
    lateinit var inventory: PlayerInventory

    constructor(slotId: UUID, inventoryId: UUID, slotIndex: Int) {
        this.slotId = slotId
        this.inventoryId = inventoryId
        this.slotIndex = slotIndex
        bookId = null
    }

    fun <TEvent : Event> execute(context: TriggerExecutionContext<TEvent>, executionOrder: Int) {
        if (book == null) return

        book!!.execute(SkillBookExecutionContext(
            executionOrder = executionOrder,
            triggerContext = context
        ))
    }

}