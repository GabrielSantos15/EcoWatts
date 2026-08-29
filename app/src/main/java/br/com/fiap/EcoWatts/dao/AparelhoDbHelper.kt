package br.com.fiap.EcoWatts.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AparelhoDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ecowatts_aparelhos.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_APARELHOS = "aparelhos"
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_POTENCIA_WATTS = "potencia_watts"
        const val COLUMN_HORAS_DIA = "horas_dia"
        const val COLUMN_CONSUMO_MENSAL_KWH = "consumo_mensal_kwh"
        const val COLUMN_CUSTO_MENSAL = "custo_mensal"

        @Volatile
        private var instance: AparelhoDbHelper? = null

        fun getInstance(context: Context): AparelhoDbHelper {
            return instance ?: synchronized(this) {
                instance ?: AparelhoDbHelper(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = """
            CREATE TABLE $TABLE_APARELHOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_POTENCIA_WATTS REAL NOT NULL,
                $COLUMN_HORAS_DIA REAL NOT NULL,
                $COLUMN_CONSUMO_MENSAL_KWH REAL NOT NULL,
                $COLUMN_CUSTO_MENSAL REAL NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableSql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APARELHOS")
        onCreate(db)
    }
}