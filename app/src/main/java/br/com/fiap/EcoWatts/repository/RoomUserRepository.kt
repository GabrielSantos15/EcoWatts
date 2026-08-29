package br.com.fiap.EcoWatts.repository

import android.content.Context
import br.com.fiap.EcoWatts.dao.EcoWattsDataBase
import br.com.fiap.EcoWatts.dao.UserDao
import br.com.fiap.EcoWatts.model.User

class RoomUserRepository(context: Context): UserRepository {

    private val ecoWattsDataBase = EcoWattsDataBase.getDatabase(context).userDao()
    override fun saveUser(user: User) {
        ecoWattsDataBase.save(user);
    }

    override fun getUser(id: Int): User {
        return ecoWattsDataBase.getUserById(id) ?: User()
    }

    override fun getUser(): User {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): User? {
        return ecoWattsDataBase.getUserByEmail(email)
    }

    override fun login(email: String, password: String): User? {
        return ecoWattsDataBase.login(email, password)
    }

    override fun update(user: User): Int {
        return ecoWattsDataBase.update(user)
    }

    override fun delete(user: User): Int {
        return ecoWattsDataBase.delete(user)
    }

}