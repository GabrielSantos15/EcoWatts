package br.com.fiap.EcoWatts.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "appliances",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class Appliance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val powerWatts: Double,
    val hoursOfUsePerDay: Double,
    val monthlyConsumptionKwh: Double,
    val monthlyCost: Double,
    val userId: Int
)