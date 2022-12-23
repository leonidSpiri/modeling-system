package domain.usecase

import domain.entity.Crew
import domain.entity.Feeder
import domain.repository.WorkRepository

class DoWorkUseCase(
    private val repository: WorkRepository
) {
    operator fun invoke(crews: List<Crew>, feeders: List<Feeder>, simulatingDays: Int, maxWorkHours: Int) =
        repository.doWork(crews, feeders, simulatingDays, maxWorkHours)
}