package com.example.homeofhopeapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homeofhopeapp.data.AppDatabase
import com.example.homeofhopeapp.data.UserRepository
import com.example.homeofhopeapp.ui.auth.AuthViewModel
import com.example.homeofhopeapp.ui.auth.AuthViewModelFactory
import com.example.homeofhopeapp.ui.screens.HomeScreen
import com.example.homeofhopeapp.ui.screens.LoginScreen
import com.example.homeofhopeapp.ui.screens.RegistrationScreen

@Composable
fun MainApp() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val userRepository = UserRepository(database.userDao())
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(userRepository))
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { 
                    viewModel.resetState()
                    navController.navigate("register") 
                },
                onLoginSuccess = { 
                    val user = (viewModel.authState.value as? com.example.homeofhopeapp.ui.auth.AuthState.Authenticated)?.user
                    user?.let {
                        navController.navigate("home/${it.username}") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
        composable("register") {
            RegistrationScreen(
                viewModel = viewModel,
                onNavigateToLogin = { 
                    viewModel.resetState()
                    navController.navigate("login") 
                }
            )
        }
        composable(
            route = "home/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(
                username = username,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home/{username}") { inclusive = true }
                    }
                }
            )
        }
    }
}
