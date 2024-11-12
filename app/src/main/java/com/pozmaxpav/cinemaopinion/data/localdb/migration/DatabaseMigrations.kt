package com.pozmaxpav.cinemaopinion.data.localdb.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {

            // Создание таблицы series_control
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS series_control (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "title TEXT NOT NULL, " +
                        "season INTEGER NOT NULL DEFAULT 0, " +
                        "series INTEGER NOT NULL DEFAULT 0)"
            )

            // Создание таблицы comments
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS comments (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "movieId REAL NOT NULL, " +
                        "commentText TEXT NOT NULL, " +
                        "timestamp INTEGER NOT NULL)"
            )
        }
    }
}