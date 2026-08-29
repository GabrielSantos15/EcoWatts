package br.com.fiap.EcoWatts.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import br.com.fiap.EcoWatts.model.Aparelho

class AparelhoDao(context: Context) {

    private val dbHelper = AparelhoDbHelper.getInstance(context)

    fun save(aparelho: Aparelho): Long {
        val db = dbHelper.writableDatabase
        val values = toContentValues(aparelho)
        return db.insert(AparelhoDbHelper.TABLE_APARELHOS, null, values)
    }

    fun update(aparelho: Aparelho): Int {
        val db = dbHelper.writableDatabase
        val values = toContentValues(aparelho)
        return db.update(
            AparelhoDbHelper.TABLE_APARELHOS,
            values,
            "${AparelhoDbHelper.COLUMN_ID} = ?",
            arrayOf(aparelho.id.toString())
        )
    }

    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AparelhoDbHelper.TABLE_APARELHOS,
            "${AparelhoDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun getById(id: Int): Aparelho? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AparelhoDbHelper.TABLE_APARELHOS,
            null,
            "${AparelhoDbHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToAparelho(it)
            }
        }
        return null
    }

    fun getAll(): List<Aparelho> {
        val lista = mutableListOf<Aparelho>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            AparelhoDbHelper.TABLE_APARELHOS,
            null, null, null, null, null,
            "${AparelhoDbHelper.COLUMN_NOME} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(cursorToAparelho(it))
            }
        }
        return lista
    }

    private fun toContentValues(aparelho: Aparelho): ContentValues {
        return ContentValues().apply {
            put(AparelhoDbHelper.COLUMN_NOME, aparelho.nome)
            put(AparelhoDbHelper.COLUMN_POTENCIA_WATTS, aparelho.potenciaWatts)
            put(AparelhoDbHelper.COLUMN_HORAS_DIA, aparelho.horasDia)
            put(AparelhoDbHelper.COLUMN_CONSUMO_MENSAL_KWH, aparelho.consumoMensalKwh)
            put(AparelhoDbHelper.COLUMN_CUSTO_MENSAL, aparelho.custoMensal)
        }
    }

    private fun cursorToAparelho(cursor: Cursor): Aparelho {
        return Aparelho(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_ID)),
            nome = cursor.getString(cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_NOME)),
            potenciaWatts = cursor.getDouble(
                cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_POTENCIA_WATTS)
            ),
            horasDia = cursor.getDouble(
                cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_HORAS_DIA)
            ),
            consumoMensalKwh = cursor.getDouble(
                cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_CONSUMO_MENSAL_KWH)
            ),
            custoMensal = cursor.getDouble(
                cursor.getColumnIndexOrThrow(AparelhoDbHelper.COLUMN_CUSTO_MENSAL)
            )
        )
    }
}