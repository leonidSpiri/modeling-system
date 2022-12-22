package domain.entity

data class Work(
    val id: Int,
    val crewId: Int,
    val hourCounter: Int = 0,
    val feeders: List<Feeder> = emptyList(),
    val spentMoney: Int = 0,
    val receivedMoney: Int = 0,
    val workStage: WorkStage = WorkStage.WAITING
)