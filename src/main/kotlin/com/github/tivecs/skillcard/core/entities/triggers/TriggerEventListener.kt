package com.github.tivecs.skillcard.core.entities.triggers

import com.github.tivecs.skillcard.core.builtin.triggers.OnAttackTrigger
import com.github.tivecs.skillcard.internal.data.repositories.SkillBookRepository
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Suppress("UNCHECKED_CAST")
class TriggerEventListener : Listener {

    @EventHandler
    fun onDamageTriggerHandler(event: EntityDamageByEntityEvent) {
        val triggerResult = OnAttackTrigger.handle(event)

        val books = SkillBookRepository.getAll()

        books.forEach {
            it.execute(triggerResult)
        }
    }
}