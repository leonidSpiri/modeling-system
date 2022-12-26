package data.repository

import domain.entity.Feeder
import domain.repository.FeederRepository
import kotlin.random.Random

class FeederRepositoryImpl : FeederRepository {
    override fun createFeeder(feederCount: Int): List<Feeder> {
        val feeders = mutableListOf<Feeder>()
        for (i in 0 until feederCount) {
            feeders.add(
                Feeder(
                    id = i,
                    normativeTime = Random.nextInt(20, 150),
                    serviceCost = Random.nextInt(1500, 8000),
                )
            )
        }
        return feeders
    }
}