package com.github.tivecs.skillcard

import com.github.tivecs.skillcard.builtin.abilities.DamageAbility
import com.github.tivecs.skillcard.builtin.abilities.HealAbility
import com.github.tivecs.skillcard.builtin.abilities.IgniteAbility
import com.github.tivecs.skillcard.builtin.abilities.PotionEffectAbility
import com.github.tivecs.skillcard.builtin.abilities.ShootProjectileAbility
import com.github.tivecs.skillcard.builtin.abilities.ThunderAbility
import com.github.tivecs.skillcard.builtin.triggers.OnAttackTrigger
import com.github.tivecs.skillcard.builtin.triggers.OnAttackedTrigger
import com.github.tivecs.skillcard.cmds.SkillCardCommand
import com.github.tivecs.skillcard.core.triggers.TriggerEventListener
import com.github.tivecs.skillcard.internal.config.SkillCardConfig
import com.github.tivecs.skillcard.internal.data.SkillCardDatabase
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import com.github.tivecs.skillcard.internal.data.repositories.TriggerRepository
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.v1.jdbc.Database

class SkillCardPlugin : JavaPlugin() {

    companion object {
        lateinit var pluginConfig: SkillCardConfig
        lateinit var database: SkillCardDatabase
    }

    override fun onEnable() {
        registerInBuiltTriggers()
        registerInBuiltAbilities()

        pluginConfig = SkillCardConfig(this)
        database = SkillCardDatabase(pluginConfig.storageConfig)

        database.migrate()

        getCommand("skillcard")?.setExecutor(SkillCardCommand())

        Bukkit.getPluginManager().registerEvents(TriggerEventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerInBuiltTriggers() {
        TriggerRepository.register(
            OnAttackedTrigger,
            OnAttackTrigger
        )
    }

    private fun registerInBuiltAbilities() {
        AbilityRepository.register(
            HealAbility,
            DamageAbility,
            IgniteAbility,
            ShootProjectileAbility,
            PotionEffectAbility,
            ThunderAbility
        )
    }
}
