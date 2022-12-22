package domain.entity

data class Feeder(
    val id: Int,
    val minWorkHours: Int,
    val maxWorkHours: Int,
    val serviceCost: Int
)
