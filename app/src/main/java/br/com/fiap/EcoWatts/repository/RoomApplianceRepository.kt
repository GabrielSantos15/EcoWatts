package br.com.fiap.EcoWatts.repository

import android.content.Context
import br.com.fiap.EcoWatts.dao.EcoWattsDataBase
import br.com.fiap.EcoWatts.model.Appliance

class RoomApplianceRepository(context: Context) : ApplianceRepository {

    private val applianceDao = EcoWattsDataBase.getDatabase(context).applianceDao()

    override suspend fun insert(appliance: Appliance) {
        applianceDao.insert(appliance)
    }

    override suspend fun update(appliance: Appliance) {
        applianceDao.update(appliance)
    }

    override suspend fun delete(appliance: Appliance) {
        applianceDao.delete(appliance)
    }

    override suspend fun getAppliancesByUser(userId: Int): List<Appliance> {
        return applianceDao.getAppliancesByUser(userId)
    }

    override suspend fun getApplianceById(id: Int): Appliance? {
        return applianceDao.getApplianceById(id)
    }

    override suspend fun getTotalMonthlyCostByUser(userId: Int): Double {
        return applianceDao.getTotalMonthlyCostByUser(userId) ?: 0.0
    }
}