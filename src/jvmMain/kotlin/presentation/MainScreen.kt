package presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.DarkDefaultContextMenuRepresentation
import androidx.compose.foundation.LightDefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.repository.FeederRepositoryImpl
import data.repository.WorkRepositoryImpl
import domain.entity.Crew
import domain.entity.Summary
import domain.usecase.CreateFeederUseCase
import domain.usecase.DoWorkUseCase
import java.awt.FlowLayout
import javax.swing.*
import kotlin.random.Random


class MainScreen {
    @Composable
    @Preview
    fun app() {
        val crewCount = remember { mutableStateOf("2") }
        val simulatingDays = remember { mutableStateOf("100") }
        val maxWorkHours = remember { mutableStateOf("8") }
        MaterialTheme(
            colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
        ) {
            val contextMenuRepresentation = if (isSystemInDarkTheme())
                DarkDefaultContextMenuRepresentation
            else
                LightDefaultContextMenuRepresentation
            CompositionLocalProvider(LocalContextMenuRepresentation provides contextMenuRepresentation) {
                Surface(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxSize()) {
                        TextField(
                            value = crewCount.value,
                            modifier = Modifier.padding(start = 16.dp),
                            singleLine = true,
                            onValueChange = { crewCount.value = it },
                            label = { Text(text = "How many crews should work?") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        TextField(
                            value = simulatingDays.value,
                            modifier = Modifier.padding(start = 16.dp),
                            singleLine = true,
                            onValueChange = { simulatingDays.value = it },
                            label = { Text(text = "How many days does the simulation last?") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        TextField(
                            value = maxWorkHours.value,
                            modifier = Modifier.padding(start = 16.dp),
                            singleLine = true,
                            onValueChange = { maxWorkHours.value = it },
                            label = { Text(text = "The maximum working time of the team per day") }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = {
                                startCalculation(
                                    crewCount = crewCount.value.toInt(),
                                    simulatingDays = simulatingDays.value.toInt(),
                                    maxWorkHours = maxWorkHours.value.toInt()
                                )

                            }) { Text(text = "Расчет") }

                    }
                }
            }
        }
    }

    private fun startCalculation(
        crewCount: Int,
        simulatingDays: Int,
        maxWorkHours: Int
    ) {
        createCrews(crewCount) { crews ->
            val feeders = CreateFeederUseCase(FeederRepositoryImpl()).invoke(100000)
            outputResult(DoWorkUseCase(WorkRepositoryImpl()).invoke(crews, feeders, simulatingDays, maxWorkHours))
        }
    }

    private fun outputResult(summaryStatistics: Summary) {
        val feederCount = summaryStatistics.feederCounter
        val frame = JFrame("Results")
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        val chart = BarChart()
        val useColorList = mutableListOf<Int>()
        for (i in feederCount.indices) {
            if (feederCount[i] > 0) {
                var colorNumber = Random.nextInt(0, BarChart.colors.lastIndex)
                while (useColorList.contains(colorNumber)) {
                    if (useColorList.size == BarChart.colors.size)
                        throw Exception("Too many colors")
                    colorNumber = Random.nextInt(0, BarChart.colors.lastIndex)
                }
                useColorList.add(colorNumber)
                chart.addBar(BarChart.colors[colorNumber], listOf(feederCount[i], i + 1))
            }
        }
        panel.add(chart)
        panel.add(JLabel("Total operations: ${summaryStatistics.operationCounter}"))
        panel.add(JLabel("Average of spent money for one day for one team: ${summaryStatistics.totalSpentMoney} ₽"))
        panel.add(JLabel("Average of received money for one day for one team: ${summaryStatistics.totalReceivedMoney} ₽"))
        panel.add(JLabel("The average work time of the team in one day: ${summaryStatistics.totalMinuteCounter / 60} hours"))
        panel.add(JLabel("Total amount of money earned: ${summaryStatistics.totalMoney} ₽"))
        frame.add(panel)
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.pack()
        frame.isVisible = true
    }

    private fun createCrews(crewCount: Int, callback: (List<Crew>) -> Unit) {
        val crews = mutableListOf<Crew>()
        for (i in 0 until crewCount) {
            crewCreateDialog(i) {
                crews.add(it)
                if (crews.size == crewCount)
                    callback(crews)
            }
        }
    }

    private fun crewCreateDialog(i: Int, callback: (Crew) -> Unit) {
        val frame = JFrame("Add new crew")
        val panel = JPanel()
        panel.layout = FlowLayout()
        val label = JLabel("Add ${i + 1} crew")

        val crewNameField = JTextField(10)
        val countOfWorkersField = JTextField(10)
        val hourlyRateField = JTextField(10)
        val workIndexField = JTextField(10)

        val button = JButton()
        button.text = "Ready"
        button.addActionListener {
            val crewName = crewNameField.text
            val countOfWorkers = countOfWorkersField.text.toInt()
            val hourlyRate = hourlyRateField.text.toInt()
            val workIndex = workIndexField.text.toDouble()
            if (crewName.isNotEmpty() && countOfWorkers > 0 && hourlyRate > 0 && workIndex > 0) {
                frame.dispose()
                callback(
                    Crew(
                        id = i,
                        name = crewName,
                        countOfWorkers = countOfWorkers,
                        hourlyRate = hourlyRate,
                        workIndex = workIndex,
                    )
                )
            }
        }
        panel.add(label)
        panel.add(button)
        panel.add(JLabel("Crew name"))
        panel.add(crewNameField)
        panel.add(JLabel("Count of workers"))
        panel.add(countOfWorkersField)
        panel.add(JLabel("Hourly rate"))
        panel.add(hourlyRateField)
        panel.add(JLabel("Work index"))
        panel.add(workIndexField)
        frame.add(panel)
        frame.setSize(200, 300)
        frame.setLocationRelativeTo(null)
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.isVisible = true
    }
}