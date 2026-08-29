package br.com.fiap.EcoWatts.repository

import br.com.fiap.EcoWatts.model.Appliance

interface ApplianceRepository {

    suspend fun insert(appliance: Appliance)

    suspend fun update(appliance: Appliance)

    suspend fun delete(appliance: Appliance)

    suspend fun getAppliancesByUser(userId: Int): List<Appliance>

    suspend fun getApplianceById(id: Int): Appliance?

    suspend fun getTotalMonthlyCostByUser(userId: Int): Double
}