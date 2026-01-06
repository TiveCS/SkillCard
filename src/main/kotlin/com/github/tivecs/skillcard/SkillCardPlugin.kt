package com.github.tivecs.skillcard

import com.github.tivecs.skillcard.cmds.SkillCardCommand
import com.github.tivecs.skillcard.core.triggers.TriggerEventListener
import com.github.tivecs.skillcard.internal.config.SkillCardConfig
import com.github.tivecs.skillcard.internal.data.SkillCardDatabase
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SkillCardPlugin : JavaPlugin() {

    companion object {
        lateinit var pluginConfig: SkillCardConfig
        lateinit var database: SkillCardDatabase
    }

    override fun onEnable() {
        // Plugin startup logic
        pluginConfig = SkillCardConfig(this)
        database = SkillCardDatabase(pluginConfig.storageConfig)

        getCommand("skillcard")?.setExecutor(SkillCardCommand())

        Bukkit.getPluginManager().registerEvents(TriggerEventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
