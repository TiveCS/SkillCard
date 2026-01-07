package com.github.tivecs.skillcard.internal.config

import org.bukkit.configuration.file.FileConfiguration
import java.io.File

enum class SkillCardStorageType {
    SQLITE,
    MYSQL;
}

class SkillCardStorageConfig {
    val type: SkillCardStorageType

    var host: String? = null
    var port: String? = null
    var user: String? = null
    var password: String? = null
    var database: String? = null

    constructor(config: FileConfiguration) {
        val storageConfig = config.getConfigurationSection("storage")
            ?: throw IllegalArgumentException("config at path 'storage' is missing")

        storageConfig.let {
            val typeStr = it.getString("type") ?: throw IllegalArgumentException("config path 'storage.type' is missing")

            type = SkillCardStorageType.valueOf(typeStr.uppercase())

            when (type) {
                SkillCardStorageType.SQLITE -> {
                    database = it.getString("sqlite.database")
                        ?: throw IllegalArgumentException("config path 'storage.sqlite.database' is missing")
                }

                SkillCardStorageType.MYSQL -> {
                    host = it.getString("mysql.host") ?: throw IllegalArgumentException("config path 'storage.mysql.host' is missing")
                    port = it.getString("mysql.port") ?: throw IllegalArgumentException("config path 'storage.mysql.port' is missing")
                    user = it.getString("mysql.user") ?: throw IllegalArgumentException("config path 'storage.mysql.user' is missing")
                    password = it.getString("mysql.password")
                        ?: throw IllegalArgumentException("config path 'storage.mysql.password' is missing")
                    database = it.getString("mysql.database")
                        ?: throw IllegalArgumentException("config path 'storage.mysql.database' is missing")
                }
            }
        }
    }
}