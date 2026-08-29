package br.com.fiap.EcoWatts.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.EcoWatts.model.User

@Database(entities = [User::class], version = 1)
abstract class EcoWattsDataBase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object{
        private lateinit var instance: EcoWattsDataBase

        fun getDatabase(context: Context): EcoWattsDataBase{
            if (!::instance.isInitialized){
                instance = Room
                    .databaseBuilder(
                        context,
                        EcoWattsDataBase::class.java,
                        "ecowatts_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration(true)
                    .build()
            }
            return instance
        }
    }
}