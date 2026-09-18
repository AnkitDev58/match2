package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.di.appModule
import org.example.project.navigation.Screen
import org.example.project.presenter.home.HomeScreen
import org.example.project.presenter.home.HomeViewModel
import org.example.project.presenter.matchup.MatchUpScreen
import org.example.project.presenter.matchup.MatchUpViewModel
import org.example.project.presenter.trapgrid.TrapGridScreen
import org.example.project.presenter.trapgrid.TrapGridViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin

@Composable
@Preview
fun App() {
    startKoin {
        modules(
            appModule
        )
    }

    val navController = rememberNavController()

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home
            ) {
                composable<Screen.Home> {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    HomeScreen(
                        viewModel = homeViewModel,
                        onNavigateToMatchUp = {
                            navController.navigate(Screen.MatchUp)
                        },
                        onNavigateToTrapGrid = {
                            navController.navigate(Screen.TrapGrid)
                        }
                    )
                }
                composable<Screen.MatchUp> {
                    val matchUpViewModel: MatchUpViewModel = koinViewModel()
                    MatchUpScreen(
                        viewModel = matchUpViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<Screen.TrapGrid> {
                    val trapGridViewModel: TrapGridViewModel = koinViewModel()
                    TrapGridScreen(
                        viewModel = trapGridViewModel,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
