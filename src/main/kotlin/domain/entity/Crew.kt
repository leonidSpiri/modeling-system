package domain.entity

data class Crew(
    val id: Int,
    val name: String,
    val countOfWorkers: Int,
    val hourlyRate: Int,
    val workIndex: Int
)