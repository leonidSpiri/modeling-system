import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import presentation.MainScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Modeling System",
        state = rememberWindowState(width = 400.dp, height = 400.dp)
    ) {
        MainScreen().app()
    }
}