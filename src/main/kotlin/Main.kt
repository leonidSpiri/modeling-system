import data.repository.FeederRepositoryImpl
import data.repository.WorkRepositoryImpl
import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Summary
import domain.entity.Work
import domain.usecase.CreateFeederUseCase
import domain.usecase.DoWorkUseCase
import java.util.*
import kotlin.random.Random

fun main() {
    val scanner = Scanner(System.`in`)
    println("--- Start of the Program ---")
    println("--- Initializing the GUI ---")


    println("--- First Stage. Data entry ---")
    println("--- How many crews should work? ---")
    val crewCount = scanner.nextInt()
    //val crews = createCrews(crewCount)
    val crews = debugCreateCrew(crewCount)

    println("--- How many days does the simulation last? ---")
    val simulatingDays = scanner.nextInt()

    println("--- How many feeders do the teams have to service all the time? ---")
    val feedersCount = scanner.nextInt()
    val feeders = CreateFeederUseCase(FeederRepositoryImpl()).invoke(feedersCount)



    println("--- Second Stage. Data processing ---")


    val summaryStatistics = doWork(simulatingDays, crews, feeders)


    println("--- Third Stage. Data output ---")

    println("Total operations: ${summaryStatistics.operationCounter}")
    println("Average of spent money for one day for one team: ${summaryStatistics.totalSpentMoney}")
    println("Average of received money for one day for one team: ${summaryStatistics.totalReceivedMoney}")
    println("The average work time of the team in one day: ${summaryStatistics.totalMinuteCounter / 60}")
    println("Average number of devices serviced by one team in one day: ${summaryStatistics.totalFeederCounter}")
    println("--- End of the Program ---")
}

private fun doWork(simulatingDays: Int, crews: List<Crew>, feeders: List<Feeder>): Summary {
    val summaryStatistics = Summary()
    val doWorkUseCase = DoWorkUseCase(WorkRepositoryImpl())
    for (i in 0 until simulatingDays) {
        val servicedDevices = mutableListOf<Int>()
        for (crew in crews) {
            var work = Work(crewId = crew.id)
            while (work.minuteCounter < 7 * 60) {
                val feeder = feeders[Random.nextInt(0, feeders.size)]
                if (servicedDevices.contains(feeder.id))
                    continue
                servicedDevices.add(feeder.id)
                work = doWorkUseCase.invoke(crew, feeder, work)
            }
            summaryStatistics.operationCounter++
            summaryStatistics.totalSpentMoney += work.spentMoney
            summaryStatistics.totalReceivedMoney += work.receivedMoney
            summaryStatistics.totalMinuteCounter += work.minuteCounter
            summaryStatistics.totalFeederCounter += work.feedersId.size
        }
    }
    summaryStatistics.totalSpentMoney /= summaryStatistics.operationCounter
    summaryStatistics.totalReceivedMoney /= summaryStatistics.operationCounter
    summaryStatistics.totalMinuteCounter /= summaryStatistics.operationCounter
    summaryStatistics.totalFeederCounter /= summaryStatistics.operationCounter
    return summaryStatistics
}

private fun debugCreateCrew(crewCount: Int): List<Crew> {
    val crews = mutableListOf<Crew>()
    for (i in 0 until crewCount) {
        crews.add(
            Crew(
                id = i,
                name = "Crew $i",
                countOfWorkers = Random.nextInt(1, 2),
                hourlyRate = Random.nextInt(1000, 3000),
                workIndex = Random.nextDouble(0.85, 1.5)
            )
        )
    }
    println(crews)
    return crews
}

private fun createCrews(crewCount: Int): List<Crew> {
    val scanner = Scanner(System.`in`)
    val crews = mutableListOf<Crew>()
    for (i in 0 until crewCount) {
        println("------ Enter the data for the crew number $i ------")
        println("------ Crew name: ------")
        val crewName = scanner.next()
        println("------ Crew size: ------")
        val countOfWorkers = scanner.nextInt()
        println("------ Crew hourly rate: ------")
        val hourlyRate = scanner.nextInt()
        println("------ Crew work index: ------")
        val workIndex = scanner.nextDouble()
        crews.add(
            Crew(
                id = i,
                name = crewName,
                countOfWorkers = countOfWorkers,
                hourlyRate = hourlyRate,
                workIndex = workIndex,
            )
        )
        println("------ Crew successfully created ------")
    }
    println(crews)
    println()
    return crews
}
