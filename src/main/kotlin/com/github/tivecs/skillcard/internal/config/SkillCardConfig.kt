package com.github.tivecs.skillcard.internal.config

import com.github.tivecs.skillcard.SkillCardPlugin

class SkillCardConfig {
    val storageConfig: SkillCardStorageConfig

    constructor(plugin: SkillCardPlugin) {
        plugin.saveDefaultConfig()

        storageConfig = SkillCardStorageConfig(plugin.config)
    }
}