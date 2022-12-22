package domain.entity

data class Work(
    val crewId: Int,
    var minuteCounter: Int = 0,
    var feedersId: MutableList<Int> = mutableListOf(),
    var spentMoney: Int = 0,
    var receivedMoney: Int = 0,
    var workStage: WorkStage = WorkStage.WAITING
)