package com.github.tivecs.skillcard.core.triggers

import com.github.tivecs.skillcard.builtin.triggers.OnAttackTrigger
import com.github.tivecs.skillcard.builtin.triggers.OnAttackTriggerAttribute
import com.github.tivecs.skillcard.builtin.triggers.OnAttackedTrigger
import com.github.tivecs.skillcard.builtin.triggers.OnAttackedTriggerAttribute
import com.github.tivecs.skillcard.internal.data.repositories.PlayerInventoryRepository
import com.github.tivecs.skillcard.internal.data.repositories.TriggerRepository
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class TriggerEventListener : Listener {

    @EventHandler
    fun handleOnAttackTrigger(event: EntityDamageByEntityEvent) {
        val attacker = event.entity
        if (attacker !is Player) return

        val onAttackTrigger =
            TriggerRepository.get<EntityDamageByEntityEvent, OnAttackTriggerAttribute>(OnAttackTrigger.identifier)

        val result = onAttackTrigger.execute(event)

        if (result.state != TriggerExecuteResultState.EXECUTED ||
            result.attributes == null) return

        val playerInventory = PlayerInventoryRepository.getOrCreate(attacker.uniqueId)

        val targetSkillBooks = playerInventory.slots.values.filter { slot ->
            slot.bookId != null && slot.book != null && slot.book!!.triggerSkillsMap.containsKey(onAttackTrigger.identifier)
        }.map { it.book!! }

        targetSkillBooks.forEach {
            it.execute(onAttackTrigger, result.attributes)
        }
    }

    @EventHandler
    fun handleOnAttackedTrigger(event: EntityDamageByEntityEvent) {
        val defender = event.entity
        if (defender !is Player) return

        val onAttackedTrigger =
            TriggerRepository.get<EntityDamageByEntityEvent, OnAttackedTriggerAttribute>(OnAttackedTrigger.identifier)

        val result = onAttackedTrigger.execute(event)

        if (result.state != TriggerExecuteResultState.EXECUTED ||
            result.attributes == null) return

        val playerInventory = PlayerInventoryRepository.getOrCreate(defender.uniqueId)

        val targetSkillBooks = playerInventory.slots.values.filter { slot ->
            slot.bookId != null && slot.book != null && slot.book!!.triggerSkillsMap.containsKey(onAttackedTrigger.identifier)
        }.map { it.book!! }

        targetSkillBooks.forEach {
            it.execute(onAttackedTrigger, result.attributes)
        }
    }

}