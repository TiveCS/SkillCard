package com.github.tivecs.skillcard.internal.data

import com.github.tivecs.skillcard.internal.config.SkillCardStorageConfig
import com.github.tivecs.skillcard.internal.config.SkillCardStorageType
import com.github.tivecs.skillcard.internal.data.tables.SkillBookTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventorySlotTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventoryTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

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

    fun connect(): Database {
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
        connect()

        transaction {
            SchemaUtils.create(
                SkillBookTable,
                PlayerInventoryTable,
                PlayerInventorySlotTable,
                inBatch = true
            )
        }
    }

}