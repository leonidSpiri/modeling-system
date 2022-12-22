package domain.usecase

import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Work
import domain.repository.WorkRepository

class DoWorkUseCase(
    private val repository: WorkRepository
) {
    operator fun invoke(crew: Crew, feeder: Feeder, work: Work) =
        repository.doWork(crew, feeder, work)
}