import data.repository.FeederRepositoryImpl
import domain.entity.Crew
import domain.entity.Feeder
import domain.usecase.CreateFeederUseCase
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
    println(feeders)



    println("--- Second Stage. Data processing ---")


    doWork(simulatingDays, crews, feeders)


    println("--- Third Stage. Data output ---")
}

private fun doWork(simulatingDays: Int, crews: List<Crew>, feeders: List<Feeder>) {
    for (i in 0 until simulatingDays) {

    }
}

private fun debugCreateCrew(crewCount: Int): List<Crew> {
    val crews = mutableListOf<Crew>()
    for (i in 0 until crewCount) {
        crews.add(
            Crew(
                id = i,
                name = "Crew $i",
                countOfWorkers = Random.nextInt(1, 5),
                hourlyRate = Random.nextInt(2500, 10000),
                workIndex = Random.nextDouble(0.5, 1.5)
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
