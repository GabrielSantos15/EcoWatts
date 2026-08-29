package br.com.fiap.EcoWatts.repository

import android.content.Context
import android.content.SharedPreferences

class SessionRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("ecowatts_session", Context.MODE_PRIVATE)

    fun saveUserId(id: Int) {
        prefs.edit().putInt("USER_ID", id).apply()
    }

    fun logout() {
        prefs.edit().putInt("USER_ID", 0).apply()
    }


    fun getUserId(): Int {
        return prefs.getInt("USER_ID", 0)
    }
}