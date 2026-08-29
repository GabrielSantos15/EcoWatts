package br.com.fiap.EcoWatts.repository

import br.com.fiap.EcoWatts.model.Aparelho

interface AparelhoRepository {
    fun saveAparelho(aparelho: Aparelho): Long
    fun getAparelho(id: Int): Aparelho?
    fun getAllAparelhos(): List<Aparelho>
    fun updateAparelho(aparelho: Aparelho): Int
    fun deleteAparelho(id: Int): Int
}