package com.github.tivecs.skillcard.internal.data

import com.github.tivecs.skillcard.internal.config.SkillCardStorageConfig
import com.github.tivecs.skillcard.internal.config.SkillCardStorageType
import org.jetbrains.exposed.v1.jdbc.Database

class SkillCardDatabase {

    val config: SkillCardStorageConfig

    constructor(storageConfig: SkillCardStorageConfig) {
        config = storageConfig
    }

    fun getDatabaseUrl(): String {
        return when (config.type) {
            SkillCardStorageType.SQLITE -> "jdbc:sqlite:./plugins/SkillCard/data/${config.database}"
            SkillCardStorageType.MYSQL -> "jdbc:mysql://${config.host}:${config.port}/${config.database}"
        }
    }

    fun get(): Database {
        return when (config.type) {
            SkillCardStorageType.SQLITE -> Database.connect(getDatabaseUrl(), "org.sqlite.JDBC")
            SkillCardStorageType.MYSQL -> Database.connect(
                getDatabaseUrl(),
                driver = "com.mysql.cj.jdbc.Driver",
                user = config.user as String,
                password = config.password as String
            )
        }
    }

    fun migrate() {

    }

}