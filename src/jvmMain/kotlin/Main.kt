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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.repository.FeederRepositoryImpl
import data.repository.WorkRepositoryImpl
import domain.entity.Crew
import domain.usecase.CreateFeederUseCase
import domain.usecase.DoWorkUseCase
import java.awt.FlowLayout
import javax.swing.*
import kotlin.random.Random


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

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 400.dp, height = 400.dp)
    ) {
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
        val summaryStatistics =
            DoWorkUseCase(WorkRepositoryImpl()).invoke(crews, feeders, simulatingDays, maxWorkHours)
        val feederCount = summaryStatistics.feederCounter
        val frame = JFrame("Results")
        val chart = BarChart()
        for (i in feederCount.indices) {
            if (feederCount[i] > 0)
                chart.addBar(BarChart.colors[Random.nextInt(BarChart.colors.lastIndex)], listOf(feederCount[i], i + 1))
        }
        frame.contentPane.add(chart)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.pack()
        frame.isVisible = true

        /*println("Total operations: ${summaryStatistics.operationCounter}")
        println("Average of spent money for one day for one team: ${summaryStatistics.totalSpentMoney}")
        println("Average of received money for one day for one team: ${summaryStatistics.totalReceivedMoney}")
        println("The average work time of the team in one day: ${summaryStatistics.totalMinuteCounter / 60}")
        println("Average number of devices serviced by one team in one day: ${summaryStatistics.totalFeederCounter}")
        println("Total amount of money earned: ${summaryStatistics.totalMoney}")
        println("--- End of the Program ---")*/
    }
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