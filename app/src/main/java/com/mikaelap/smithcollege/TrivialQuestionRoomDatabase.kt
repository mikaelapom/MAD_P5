package com.mikaelap.smithcollege

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(
    entities = [
        TrivialQuestion::class,
        Course::class
    ],
    version = 3
)
abstract class TrivialQuestionRoomDatabase : RoomDatabase() {

    abstract fun TrivialQuestionDao(): TrivialQuestionDao
    abstract fun courseDao(): CourseDao

    companion object {
        private var INSTANCE: TrivialQuestionRoomDatabase? = null

        fun getInstance(context: Context): TrivialQuestionRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TrivialQuestionRoomDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}