package domain.repository

import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Summary

interface WorkRepository {
    fun doWork(crews: List<Crew>, feeders: List<Feeder>, simulatingDays: Int, maxWorkHours: Int): Summary
}