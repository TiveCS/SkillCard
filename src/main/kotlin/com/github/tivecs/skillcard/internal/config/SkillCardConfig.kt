package com.github.tivecs.skillcard.internal.config

import com.github.tivecs.skillcard.SkillCardPlugin
import java.io.File
import kotlin.io.path.Path

class SkillCardConfig {
    val storageConfig: SkillCardStorageConfig

    constructor(plugin: SkillCardPlugin) {
        plugin.saveDefaultConfig()

        val storageDataDirectory = File(Path(plugin.dataFolder.path, "data").toString())
        if (!storageDataDirectory.exists())
            storageDataDirectory.mkdir()

        storageConfig = SkillCardStorageConfig(plugin.config)
    }
}