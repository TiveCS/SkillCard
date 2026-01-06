package com.github.tivecs.skillcard.core.player

import com.github.tivecs.skillcard.core.books.Book

class PlayerInventory {

    val profile: PlayerProfile
    val slots = mutableMapOf<Int, PlayerInventorySlot>()

    constructor(profile: PlayerProfile) {
        this.profile = profile
        // Load player inventory from DB by playerId
    }

    // TODO: replace skill book with proper type. Prevent equip if slot has skill book equipped
    fun equipSlot(slotIndex: Int, skillBook: Book) {

    }

    // TODO: unequip the skill book, then return the skill book to the player minecraft inventory.
    //  Prevent equip if slot is locked or player doesn't have space in minecraft inventory
    fun unequipSlot(slotIndex: Int) {
        val slot = slots[slotIndex] ?: return

        if (slot.locked) return

        slot.skillBook = null

        // TODO: return skill book to player minecraft inventory
    }

    // TODO: fetch player profile from database (inventories, etc)
    fun fetchFromDatabase()  {

    }

    // TODO: persist current player profile (inventories, etc) to database
    fun saveToDatabase() {

    }

}