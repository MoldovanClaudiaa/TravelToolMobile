package com.example.traveltool

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//        val authViewModel: AuthViewModel by viewModels()
//        setContent {
//            TravelToolTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MyAppNavigation(
//                        modifier = Modifier.padding(innerPadding),
//                        authViewModel = authViewModel
//                    )
//                }
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MyAppNavigation(modifier = Modifier, authViewModel = AuthViewModel(), navController = navController)
        }
    }
}