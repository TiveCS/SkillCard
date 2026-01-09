package com.github.tivecs.skillcard.core.books

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.inventory.ItemStack
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

class SkillBook {

    val bookId: UUID
    lateinit var identifier: String
    lateinit var name: String
    lateinit var material: String
    lateinit var displayName: String
    lateinit var description: String

    val skills = mutableMapOf<String, Any>()

    constructor(bookId: UUID) {
        this.bookId = bookId
    }

    fun getItem(): ItemStack? {
        val xmat = XMaterial.matchXMaterial(material)
        val mat = xmat.getOrElse { XMaterial.BOOK }

        val item = mat.parseItem()

        if (item == null) return null

        val meta = item.itemMeta

        if (meta == null) return item

        meta.setDisplayName(displayName.colorized())
        meta.lore = description.split("\n").map { it.colorized() }
        item.itemMeta = meta

        return item
    }

    fun execute() {

    }
}