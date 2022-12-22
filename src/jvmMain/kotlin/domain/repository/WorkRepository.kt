package domain.repository

import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Work

interface WorkRepository {
    fun doWork(crew: Crew, feeder: Feeder, work: Work): Work
}