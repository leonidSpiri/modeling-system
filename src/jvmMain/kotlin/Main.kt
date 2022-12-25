import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import presentation.MainScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = " Моделирование систем",
        state = rememberWindowState(width = 350.dp, height = 320.dp)
    ) {
        MainScreen().app()
    }
}