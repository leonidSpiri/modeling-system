package data.repository

import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Work
import domain.repository.WorkRepository

class WorkRepositoryImpl : WorkRepository {
    override fun doWork(crew: Crew, feeder: Feeder, work: Work): Work {
        return work
    }
}