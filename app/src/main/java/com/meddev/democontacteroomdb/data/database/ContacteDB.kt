package com.meddev.democontacteroomdb.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.meddev.democontacteroomdb.data.dao.ContacteDao
import com.meddev.democontacteroomdb.data.entity.ContacteModel


@Database(
    entities = [ContacteModel::class],
    version = 1,
    exportSchema = false
)
abstract class ContacteDB : RoomDatabase() {
    abstract fun contacteDao(): ContacteDao

    //remplacer un singletan dans java
    companion object {
        @Volatile
        private var INSTANCE: ContacteDB? = null
        fun getDatabase(context: Context): ContacteDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContacteDB::class.java,
                    "contactsDatabase.db"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}