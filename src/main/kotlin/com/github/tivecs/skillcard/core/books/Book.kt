package com.github.tivecs.skillcard.core.books

import com.github.tivecs.skillcard.core.skills.Skill
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Book {

    lateinit var name: String
    lateinit var description: String

    val identifier: String
    val skills = arrayListOf<Skill>()

    constructor(identifier: String) {
        this.identifier = identifier
    }

    fun execute() {
        skills.forEach { skill -> skill.execute() }
    }

    fun getItem(): ItemStack {
        return ItemStack(Material.AIR)
    }

}