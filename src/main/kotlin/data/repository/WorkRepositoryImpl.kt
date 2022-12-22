package data.repository

import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Work
import domain.entity.WorkStage
import domain.repository.WorkRepository
import kotlin.random.Random

class WorkRepositoryImpl : WorkRepository {
    override fun doWork(crew: Crew, feeder: Feeder, work: Work): Work {
        var minuteCounter = 0
        var spentMoney = 0
        work.workStage = WorkStage.MEETING
        minuteCounter += Random.nextInt(5, 15)

        work.workStage = WorkStage.ON_WAY
        val timeInCar = Random.nextInt(20, 120)
        minuteCounter += timeInCar
        spentMoney += timeInCar / 60 * 500

        work.workStage = WorkStage.ON_OBJECT
        minuteCounter += Random.nextInt(5, 25)

        work.workStage = WorkStage.WORK
        minuteCounter += (feeder.normativeTime * crew.workIndex).toInt()

        work.workStage = WorkStage.SEND_RESULTS
        minuteCounter += Random.nextInt(5, 15)

        work.workStage = WorkStage.WAITING
        spentMoney += crew.hourlyRate * crew.countOfWorkers * minuteCounter / 60
        work.minuteCounter += minuteCounter
        work.spentMoney += spentMoney
        work.feedersId.add(feeder.id)
        work.receivedMoney += feeder.serviceCost
        return work
    }
}