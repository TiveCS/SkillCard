package com.github.tivecs.skillcard.internal.data

import com.github.tivecs.skillcard.internal.config.SkillCardStorageConfig
import com.github.tivecs.skillcard.internal.config.SkillCardStorageType
import com.github.tivecs.skillcard.internal.data.tables.SkillBookTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventorySlotTable
import com.github.tivecs.skillcard.internal.data.tables.PlayerInventoryTable
import com.github.tivecs.skillcard.internal.data.tables.SkillAbilityTable
import com.github.tivecs.skillcard.internal.data.tables.SkillBookExecutionGroupSkillTable
import com.github.tivecs.skillcard.internal.data.tables.SkillBookExecutionGroupTable
import com.github.tivecs.skillcard.internal.data.tables.SkillTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

class SkillCardDatabase {

    val config: SkillCardStorageConfig
    private lateinit var database: Database

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
        if (::database.isInitialized) {
            return database
        }

        database = when (config.type) {
            SkillCardStorageType.SQLITE -> Database.connect(getDatabaseUrl(), "org.sqlite.JDBC")
            SkillCardStorageType.MYSQL -> Database.connect(
                getDatabaseUrl(),
                driver = "com.mysql.cj.jdbc.Driver",
                user = config.user as String,
                password = config.password as String
            )
        }

        return database
    }

    fun migrate() {
        connect()

        transaction {
            SchemaUtils.create(
                SkillTable,
                SkillAbilityTable,
                SkillBookTable,
                SkillBookExecutionGroupTable,
                SkillBookExecutionGroupSkillTable,
                PlayerInventoryTable,
                PlayerInventorySlotTable
            )
        }
    }

}