import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.repository.FeederRepositoryImpl
import data.repository.WorkRepositoryImpl
import domain.entity.Crew
import domain.entity.Feeder
import domain.entity.Summary
import domain.entity.Work
import domain.usecase.CreateFeederUseCase
import domain.usecase.DoWorkUseCase
import kotlinx.coroutines.*
import java.awt.FlowLayout
import java.util.*
import javax.swing.*
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
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


                    TooltipArea(
                        tooltip = {
                            // composable tooltip content
                            Surface(
                                modifier = Modifier.shadow(4.dp),
                                color = Color(255, 255, 210),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Tooltip for me",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        },
                        modifier = Modifier.padding(start = 16.dp),
                        delayMillis = 300, // in milliseconds
                        tooltipPlacement = TooltipPlacement.CursorPoint(
                            alignment = Alignment.BottomEnd,
                            offset = DpOffset((-16).dp, 0.dp) // tooltip offset
                        )
                    ) {

                        Button(onClick = {
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
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        app()
    }
}


private fun startCalculation(
    crewCount: Int,
    simulatingDays: Int,
    maxWorkHours: Int
) {
    createCrews(crewCount) {
        val crews = it
        val feeders = CreateFeederUseCase(FeederRepositoryImpl()).invoke(100000)
        val summaryStatistics = doWork(simulatingDays, maxWorkHours, crews, feeders)

        println("Total operations: ${summaryStatistics.operationCounter}")
        println("Average of spent money for one day for one team: ${summaryStatistics.totalSpentMoney}")
        println("Average of received money for one day for one team: ${summaryStatistics.totalReceivedMoney}")
        println("The average work time of the team in one day: ${summaryStatistics.totalMinuteCounter / 60}")
        println("Average number of devices serviced by one team in one day: ${summaryStatistics.totalFeederCounter}")
        println("Total amount of money earned: ${summaryStatistics.totalMoney}")
        println("--- End of the Program ---")
    }
}

private fun createCrews(crewCount: Int, callback: (List<Crew>) -> Unit) {
    val crews = mutableListOf<Crew>()
    for (i in 0 until crewCount) {
        extracted(i) {
            crews.add(it)
            if (crews.size == crewCount)
                callback(crews)
        }
    }
}

private fun extracted(i: Int, callback: (Crew) -> Unit) {
    val frame = JFrame("Add new crew")
    val panel = JPanel()
    panel.layout = FlowLayout()
    val label = JLabel("Add ${i + 1} crew")

    val crewNameField = JTextField(10)
    val countOfWorkersField = JTextField(10)
    val hourlyRateField = JTextField(10)
    val workIndexField = JTextField(10)

    val button = JButton()
    button.text = "Button"
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
    panel.add(crewNameField)
    panel.add(countOfWorkersField)
    panel.add(hourlyRateField)
    panel.add(workIndexField)
    frame.add(panel)
    frame.setSize(200, 300)
    frame.setLocationRelativeTo(null)
    frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
    frame.isVisible = true
}

private fun doWork(simulatingDays: Int, maxWorkHours: Int, crews: List<Crew>, feeders: List<Feeder>): Summary {
    val summaryStatistics = Summary()
    val doWorkUseCase = DoWorkUseCase(WorkRepositoryImpl())
    for (i in 0 until simulatingDays) {
        val servicedDevices = mutableListOf<Int>()
        for (crew in crews) {
            var work = Work(crewId = crew.id)
            while (work.minuteCounter < maxWorkHours * 60) {
                val feeder = feeders[Random.nextInt(0, feeders.size)]
                if (servicedDevices.contains(feeder.id))
                    continue
                servicedDevices.add(feeder.id)
                work = doWorkUseCase.invoke(crew, feeder, work)
            }
            summaryStatistics.operationCounter++
            summaryStatistics.totalSpentMoney += work.spentMoney
            summaryStatistics.totalReceivedMoney += work.receivedMoney
            summaryStatistics.totalMinuteCounter += work.minuteCounter
            summaryStatistics.totalFeederCounter += work.feedersId.size
        }
    }
    summaryStatistics.totalMoney = summaryStatistics.totalReceivedMoney - summaryStatistics.totalSpentMoney
    summaryStatistics.totalSpentMoney /= summaryStatistics.operationCounter
    summaryStatistics.totalReceivedMoney /= summaryStatistics.operationCounter
    summaryStatistics.totalMinuteCounter /= summaryStatistics.operationCounter
    summaryStatistics.totalFeederCounter /= summaryStatistics.operationCounter
    return summaryStatistics
}