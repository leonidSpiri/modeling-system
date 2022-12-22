package data.repository

import domain.entity.Crew
import domain.repository.CrewRepository

class CrewRepositoryImpl:CrewRepository {
    override fun createCrew(name: String, countOfWorkers: Int, hourlyRate: Int, workIndex: Int): Crew {
        return Crew(0, name, countOfWorkers, hourlyRate, workIndex)
    }
}