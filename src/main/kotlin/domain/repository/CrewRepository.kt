package domain.repository

import domain.entity.Crew

interface CrewRepository {
    fun createCrew(name: String, countOfWorkers: Int, hourlyRate: Int, workIndex: Int): Crew
}