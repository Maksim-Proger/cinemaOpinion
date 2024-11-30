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

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE users ADD COLUMN awards TEXT NOT NULL DEFAULT ''")
            db.execSQL("ALTER TABLE users ADD COLUMN professionalPoints INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE users ADD COLUMN seasonalEventPoints INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Создаем новую таблицу
            db.execSQL(
                """
            CREATE TABLE users_new (
                id TEXT NOT NULL PRIMARY KEY, 
                firstName TEXT NOT NULL, 
                lastName TEXT NOT NULL, 
                awards TEXT NOT NULL, 
                professionalPoints INTEGER NOT NULL, 
                seasonalEventPoints INTEGER NOT NULL
            )
            """.trimIndent()
            )

            // Копируем данные из старой таблицы, создавая UID
            db.execSQL(
                """
            INSERT INTO users_new (id, firstName, lastName, awards, professionalPoints, seasonalEventPoints)
            SELECT 
                (random() || '-' || random() || '-' || random()) AS id, -- Генерация уникального идентификатора
                firstName, 
                lastName, 
                awards, 
                professionalPoints, 
                seasonalEventPoints
            FROM users
            """.trimIndent()
            )

            // Удаляем старую таблицу
            db.execSQL("DROP TABLE users")

            // Переименовываем новую таблицу
            db.execSQL("ALTER TABLE users_new RENAME TO users")
        }
    }

}