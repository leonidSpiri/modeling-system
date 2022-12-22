package domain.entity

data class Summary(
    var operationCounter: Int = 0,
    var totalSpentMoney: Int = 0,
    var totalMoney: Int = 0,
    var totalReceivedMoney: Int = 0,
    var totalMinuteCounter: Int = 0,
    var totalFeederCounter: Int = 0,
)
