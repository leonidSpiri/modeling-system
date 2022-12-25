package data.repository

import domain.entity.*
import domain.repository.WorkRepository
import kotlin.random.Random

class WorkRepositoryImpl : WorkRepository {
    override fun doWork(crews: List<Crew>, feeders: List<Feeder>, simulatingDays: Int, maxWorkHours: Int): Summary {
        val summaryStatistics = Summary()
        val countFeeders = mutableListOf<Int>()
        for (i in 0 until simulatingDays) {
            val servicedDevices = mutableListOf<Int>()
            for (crew in crews) {
                val work = Work(crewId = crew.id)
                while (work.minuteCounter < (maxWorkHours - 1) * 60) {
                    val feeder = feeders[Random.nextInt(0, feeders.size)]
                    if (servicedDevices.contains(feeder.id))
                        continue
                    servicedDevices.add(feeder.id)
                    var minuteCounter = 0
                    var spentMoney = 0
                    work.workStage = WorkStage.MEETING
                    minuteCounter += Random.nextInt(5, 15)

                    work.workStage = WorkStage.ON_WAY
                    val timeInCar = Random.nextInt(20, 100)
                    minuteCounter += timeInCar
                    spentMoney += timeInCar / 60 * 500

                    work.workStage = WorkStage.ON_OBJECT
                    minuteCounter += Random.nextInt(5, 25)

                    work.workStage = WorkStage.WORK
                    minuteCounter += (feeder.normativeTime / (crew.workIndex * crew.countOfWorkers * 0.7)).toInt()

                    work.workStage = WorkStage.SEND_RESULTS
                    minuteCounter += Random.nextInt(5, 15)

                    work.workStage = WorkStage.WAITING
                    spentMoney += crew.hourlyRate * crew.countOfWorkers * minuteCounter / 60
                    work.minuteCounter += minuteCounter
                    work.spentMoney += spentMoney
                    work.feedersId.add(feeder.id)
                    work.receivedMoney += feeder.serviceCost
                }
                summaryStatistics.operationCounter++
                summaryStatistics.totalSpentMoney += work.spentMoney
                summaryStatistics.totalReceivedMoney += work.receivedMoney
                summaryStatistics.totalMinuteCounter += work.minuteCounter
                while (countFeeders.size <= work.feedersId.size)
                    countFeeders.add(0)
                countFeeders[work.feedersId.size] += 1
            }
        }
        summaryStatistics.totalMoney = summaryStatistics.totalReceivedMoney - summaryStatistics.totalSpentMoney
        summaryStatistics.totalSpentMoney /= summaryStatistics.operationCounter
        summaryStatistics.totalReceivedMoney /= summaryStatistics.operationCounter
        summaryStatistics.totalMinuteCounter /= summaryStatistics.operationCounter
        summaryStatistics.feederCounter = countFeeders
        return summaryStatistics
    }
}