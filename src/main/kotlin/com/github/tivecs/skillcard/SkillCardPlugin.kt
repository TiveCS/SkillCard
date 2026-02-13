package com.github.tivecs.skillcard

import com.github.tivecs.skillcard.cmds.SkillCardAdminCommand
import com.github.tivecs.skillcard.cmds.SkillCardAdminCommandTabCompleter
import com.github.tivecs.skillcard.cmds.SkillCardCommand
import com.github.tivecs.skillcard.core.builtin.abilities.*
import com.github.tivecs.skillcard.core.builtin.triggers.OnAttackTrigger
import com.github.tivecs.skillcard.core.entities.triggers.TriggerEventListener
import com.github.tivecs.skillcard.gui.common.items.BorderGuiItem
import com.github.tivecs.skillcard.gui.common.items.NextPageGuiItem
import com.github.tivecs.skillcard.gui.common.items.PreviousPageGuiItem
import com.github.tivecs.skillcard.gui.common.items.ScrollDownItem
import com.github.tivecs.skillcard.gui.common.items.ScrollUpItem
import com.github.tivecs.skillcard.internal.config.SkillCardConfig
import com.github.tivecs.skillcard.internal.data.SkillCardDatabase
import com.github.tivecs.skillcard.internal.data.repositories.AbilityRepository
import com.github.tivecs.skillcard.internal.data.repositories.TriggerRepository
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.xenondevs.invui.gui.structure.Structure

class SkillCardPlugin : JavaPlugin() {

    companion object {
        lateinit var pluginConfig: SkillCardConfig
        lateinit var database: SkillCardDatabase

        const val COMMAND_SKILLCARD = "skillcard"
        const val COMMAND_SKILLCARD_ADMIN = "skillcardadmin"
    }

    override fun onEnable() {
        registerInBuiltTriggers()
        registerInBuiltAbilities()

        pluginConfig = SkillCardConfig(this)
        database = SkillCardDatabase(pluginConfig.storageConfig)

        database.migrate()

        getCommand(COMMAND_SKILLCARD)?.setExecutor(SkillCardCommand())

        getCommand(COMMAND_SKILLCARD_ADMIN)?.setExecutor(SkillCardAdminCommand())
        getCommand(COMMAND_SKILLCARD_ADMIN)?.tabCompleter = SkillCardAdminCommandTabCompleter()

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(TriggerEventListener(), this)

        Structure.addGlobalIngredient('#', BorderGuiItem)
        Structure.addGlobalIngredient('>', ::NextPageGuiItem)
        Structure.addGlobalIngredient('<', ::PreviousPageGuiItem)
        Structure.addGlobalIngredient('^', ::ScrollUpItem)
        Structure.addGlobalIngredient('v', ::ScrollDownItem)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun registerInBuiltTriggers() {
        TriggerRepository.register(
            OnAttackTrigger
        )
    }

    private fun registerInBuiltAbilities() {
        AbilityRepository.register(
            this,
            HealAbility,
            DamageAbility,
            DashAbility,
            ThunderAbility,
            PotionEffectAbility,
            IgniteAbility
        )
    }
}
