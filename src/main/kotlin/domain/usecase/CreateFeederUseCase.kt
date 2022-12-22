package domain.usecase

import domain.repository.FeederRepository

class CreateFeederUseCase(
    private val repository: FeederRepository
) {
    operator fun invoke(feederCount: Int) =
        repository.createFeeder(feederCount)
}