package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.core.books.Book
import org.bukkit.inventory.ItemStack

class PlayerInventorySlot {

    val index: Int
    var skillBook: Book? = null
    val locked: Boolean

    constructor(slotIndex: Int, locked: Boolean, skillBook: Book?) {
        this.index = slotIndex
        this.skillBook = skillBook
        this.locked = locked
    }

    // TODO: return skill book item if exists / equipped
    fun getSkillBookItem(): ItemStack? {
        return skillBook?.getItem()
    }
}