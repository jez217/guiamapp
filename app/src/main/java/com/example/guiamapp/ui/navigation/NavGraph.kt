package com.example.guiamapp.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.guiamapp.ui.student.ImageViewerScreen
import com.example.guiamapp.ui.student.VideoPlayerScreen

object Routes {
    const val Login = "login"
    const val AdminHome = "admin_home"
    const val ProfesorHome = "profesor_home"
    const val StudentHome = "student_home"
    const val StudentNiveles = "student_niveles/{cursoId}"
    const val StudentContenido = "student_contenido/{folderLevelId}"

    const val ImageViewer = "image_viewer/{url}"
    const val VideoPlayer = "video_player/{url}"

}
@Composable
fun AppNavGraph(
    nav: NavHostController,
    loginContent: @Composable () -> Unit,
    adminContent: @Composable () -> Unit,
    profesorContent: @Composable () -> Unit,
    studentHome: @Composable () -> Unit,
    studentNiveles: @Composable (cursoId: Int) -> Unit,
    studentContenido: @Composable (folderLevelId: Int) -> Unit
) {
    NavHost(navController = nav, startDestination = Routes.Login) {
        composable(Routes.Login) { loginContent() }
        composable(Routes.AdminHome) { adminContent() }
        composable(Routes.ProfesorHome) { profesorContent() }
        composable(Routes.StudentHome) { studentHome() }

        composable(Routes.StudentNiveles) { backStack ->
            val cursoId = backStack.arguments?.getString("cursoId")?.toIntOrNull() ?: 0
            studentNiveles(cursoId)
        }

        composable(Routes.StudentContenido) { backStack ->
            val folderLevelId = backStack.arguments?.getString("folderLevelId")?.toIntOrNull() ?: 0
            studentContenido(folderLevelId)
        }
        composable(Routes.ImageViewer) { backStack ->
            val url = backStack.arguments?.getString("url") ?: return@composable
            ImageViewerScreen(imageUrl = Uri.decode(url))
        }

        composable(Routes.VideoPlayer) { backStack ->
            val url = backStack.arguments?.getString("url") ?: return@composable
            VideoPlayerScreen(videoUrl = Uri.decode(url))
        }
    }
}