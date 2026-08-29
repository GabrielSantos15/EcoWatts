package br.com.fiap.EcoWatts.repository

import br.com.fiap.EcoWatts.model.User

interface UserRepository {
    fun saveUser(user: User)
    fun getUser(): User

    fun getUser(id: Int): User
    fun getUserByEmail(email: String): User?
    fun login(email: String, password: String): User?

    fun update(user: User): Int

    fun delete(user: User): Int
}