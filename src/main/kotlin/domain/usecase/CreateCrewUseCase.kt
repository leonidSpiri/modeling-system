package domain.usecase

import domain.repository.CrewRepository

class CreateCrewUseCase(
    private val repository: CrewRepository
) {
    operator fun invoke(name: String, countOfWorkers: Int, hourlyRate: Int, workIndex: Int) =
        repository.createCrew(name, countOfWorkers, hourlyRate, workIndex)
}