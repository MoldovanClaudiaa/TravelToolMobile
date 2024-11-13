package com.example.traveltool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traveltool.pages.LoginPage
import com.example.traveltool.pages.SignupPage
import com.example.traveltool.pages.HomePage
import com.example.traveltool.pages.ProfilePage
import com.example.traveltool.pages.ShareJourneyPage
import com.example.traveltool.pages.TravelBuddyPage
import com.example.traveltool.pages.WishlistPage
import kotlinx.coroutines.delay

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val authState = authViewModel.authState.observeAsState()

//    NavHost(navController = navController, startDestination = "login", builder = {
//        composable("login") {
//            LoginPage(modifier, navController, authViewModel)
//        }
//        composable("signup") {
//            SignupPage(modifier, navController, authViewModel)
//        }
//        composable("home") {
//            HomePage(modifier, authViewModel)
//        }
//        composable("profile") {
//            ProfilePage(modifier,navController,authViewModel)
//        }
//        composable("travelBuddy") {
//            TravelBuddyPage(modifier,navController,authViewModel)
//        }
//        composable("shareYourJourney") {
//            ShareJourneyPage(modifier,navController,authViewModel)
//        }
//        composable("wishlist") {
//            WishlistPage(modifier,navController,authViewModel)
//        }
//    })

    LaunchedEffect(authState.value) {
        authState.value?.let { state ->
            when (state) {
                is AuthState.Unauthenticated -> {
                    // Delay to ensure the state has settled
                    delay(500)
                    if (navController.currentDestination?.route != "login") {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true } // Clears the back stack
                            launchSingleTop = true
                        }
                    }
                }
                is AuthState.Authenticated -> {
                    // Navigate to home after successful authentication
                    if (navController.currentDestination?.route != "home") {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            HomePage(modifier, navController, authViewModel)
        }
        composable("profile") {
            ProfilePage(modifier, navController, authViewModel)
        }
        composable("travelBuddy") {
            TravelBuddyPage(modifier, navController, authViewModel)
        }
        composable("shareYourJourney") {
            ShareJourneyPage(modifier, navController, authViewModel)
        }
        composable("wishlist") {
            WishlistPage(modifier, navController, authViewModel)
        }
    }
}