package br.com.fiap.EcoWatts.repository

import android.content.Context
import br.com.fiap.EcoWatts.dao.AparelhoDao
import br.com.fiap.EcoWatts.model.Aparelho

class SQLiteAparelhoRepository(context: Context) : AparelhoRepository {

    private val aparelhoDao = AparelhoDao(context)

    override fun saveAparelho(aparelho: Aparelho): Long {
        return aparelhoDao.save(aparelho)
    }

    override fun getAparelho(id: Int): Aparelho? {
        return aparelhoDao.getById(id)
    }

    override fun getAllAparelhos(): List<Aparelho> {
        return aparelhoDao.getAll()
    }

    override fun updateAparelho(aparelho: Aparelho): Int {
        return aparelhoDao.update(aparelho)
    }

    override fun deleteAparelho(id: Int): Int {
        return aparelhoDao.delete(id)
    }
}