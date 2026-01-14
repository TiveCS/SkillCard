package com.github.tivecs.skillcard.core.books

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.skills.Skill
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.Trigger
import com.github.tivecs.skillcard.core.triggers.TriggerAttribute
import com.github.tivecs.skillcard.internal.extensions.colorized
import org.bukkit.Material
import org.bukkit.event.Event
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

    // Trigger Identifier, Skill
    val triggerSkillsMap = mutableMapOf<String, List<Skill>>()

    constructor(bookId: UUID) {
        this.bookId = bookId
    }

    fun getItem(): ItemStack {
        val mat = XMaterial.matchXMaterial(material).getOrElse { XMaterial.BOOK }
        val item = mat.parseItem() ?: ItemStack(Material.BOOK)

        val meta = item.itemMeta ?: return item

        meta.setDisplayName(displayName.colorized())
        meta.lore = description.split("\n").map { it.colorized() }
        item.itemMeta = meta

        return item
    }

    fun <TEvent : Event> execute(context: SkillBookExecutionContext<TEvent>) {
        val trigger = context.getTrigger()
        val skills = triggerSkillsMap[trigger.identifier]

        if (skills.isNullOrEmpty()) return

        skills.forEach {
            it.execute(SkillExecutionContext(
                skillBook = this,
                skillBookContext = context))
        }
    }
}