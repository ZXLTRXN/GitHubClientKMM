package com.example.githubclientkmm.android.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.githubclientkmm.android.R
import com.example.githubclientkmm.data.KeyValueStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.main_activity) {
    @Inject
    lateinit var storage: KeyValueStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

            val destinationId = if (storage.authToken != null) {
                R.id.repositoriesListFragment
            } else {
                R.id.authFragment
            }
            navGraph.setStartDestination(destinationId)
            navController.graph = navGraph
        }
    }
}