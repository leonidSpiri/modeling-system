package domain.repository

import domain.entity.Feeder

interface FeederRepository {
    fun createFeeder(feederCount: Int): List<Feeder>
}