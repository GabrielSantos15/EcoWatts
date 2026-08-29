package br.com.fiap.EcoWatts.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ApplianceDao {

    @Insert
    suspend fun insert(appliance: Appliance)

    @Update
    suspend fun update(appliance: Appliance)

    @Delete
    suspend fun delete(appliance: Appliance)

    @Query("SELECT * FROM appliances WHERE userId = :userId")
    suspend fun getAppliancesByUser(userId: Int): List<Appliance>

    @Query("SELECT * FROM appliances WHERE id = :id")
    suspend fun getApplianceById(id: Int): Appliance?

    @Query("SELECT SUM(monthlyCost) FROM appliances WHERE userId = :userId")
    suspend fun getTotalMonthlyCostByUser(userId: Int): Double?
}