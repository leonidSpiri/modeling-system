package data.repository

import domain.entity.Feeder
import domain.repository.FeederRepository

class FeederRepositoryImpl : FeederRepository {
    override fun createFeeder(feederCount: Int): List<Feeder> {
        return listOf()
    }
}