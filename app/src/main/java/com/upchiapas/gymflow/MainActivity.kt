package com.upchiapas.gymflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upchiapas.gymflow.core.di.GymApplication
import com.upchiapas.gymflow.feature.auth.presentation.screen.LoginScreen
import com.upchiapas.gymflow.feature.auth.presentation.screen.RegisterScreen
import com.upchiapas.gymflow.feature.auth.presentation.viewmodel.AuthViewModel
import com.upchiapas.gymflow.feature.auth.presentation.viewmodel.AuthViewModelFactory
import com.upchiapas.gymflow.feature.rutina.presentation.screen.RutinaFormScreen
import com.upchiapas.gymflow.feature.rutina.presentation.screen.RutinaListScreen
import com.upchiapas.gymflow.feature.rutina.presentation.viewmodel.RutinaViewModel
import com.upchiapas.gymflow.feature.rutina.presentation.viewmodel.RutinaViewModelFactory
import com.upchiapas.gymflow.ui.theme.GymFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Obtenemos el contenedor manual de dependencias desde la Application
        val appContainer = (application as GymApplication).container

        setContent {
            GymFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Configuramos el controlador de navegación
                    val navController = rememberNavController()

                    // Inyectamos el AuthViewModel usando nuestro Factory manual y el Repositorio del contenedor
                    val authViewModel: AuthViewModel = viewModel(
                        factory = AuthViewModelFactory(appContainer.authRepository)
                    )
                    val rutinaViewModel: RutinaViewModel = viewModel(
                        factory = RutinaViewModelFactory(appContainer.rutinaRepository))

                    // Verificamos sesión para decidir qué pantalla abrir primero (por el requisito 3)
                    val startDestination = if (authViewModel.checkSession()) "home" else "login"

                    // Definimos las rutas
                    NavHost(navController = navController, startDestination = startDestination) {
                        //Rutas de Autentificación
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    rutinaViewModel.loadRutinas() //Forzamos carga al entrar
                                    // Limpiamos el historial para que Atrás no vuelva al login (RF-04)
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onNavigateToLogin = { navController.navigate("login") },
                                onRegisterSuccess = {
                                    rutinaViewModel.loadRutinas() //Forzamos carga al entrar
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("home") {
                            RutinaListScreen(
                                viewModel = rutinaViewModel,
                                onNavigateToForm = { rutinaId ->
                                    if (rutinaId == null){
                                        navController.navigate("rutina_form") //crear
                                    } else {
                                        navController.navigate("rutina_form?rutinaId=$rutinaId") //editar
                                    }
                                },
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") { popUpTo("home"){inclusive = true} }
                                }
                            )
                        }

                        composable (
                            route = "rutina_form?rutinaId={rutinaId}",
                            arguments = listOf(navArgument("rutinaId") {type = NavType.StringType; nullable = true })
                        ) { backStackEntry ->
                            val rutinaId = backStackEntry.arguments?.getString("rutinaId")
                            RutinaFormScreen(
                                viewModel = rutinaViewModel,
                                rutinaId = rutinaId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}