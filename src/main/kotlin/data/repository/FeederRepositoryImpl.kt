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
                    workMinutes = Random.nextInt(20, 120),
                    serviceCost = Random.nextInt(2500, 10000),
                )
            )
        }
        return feeders
    }
}