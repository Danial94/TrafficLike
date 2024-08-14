package com.dev.trafficlike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.trafficlike.ui.theme.TrafficLikeTheme

class MainActivity : ComponentActivity() {
    private val zDefendManager : ZDefendManager = ZDefendManager.shared
    private val databaseConnectionString: String = "Server=10.10.0.27;Database=main;User Id=danial;Password=1234;"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrafficLikeTheme {
                AppNavigation(zDefendManager)
            }
        }

        zDefendManager.initializeZDefendApi()
    }

    override fun onDestroy() {
        super.onDestroy()
        zDefendManager.deregisterZDefendApi()
    }
}

@Composable
fun AppNavigation(zDefendManager: ZDefendManager) {
    if (zDefendManager.isLoaded.value) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "main") {
            composable("main") { MainScreen(zDefendManager, navController) }
            composable("threats") { ThreatsScreen(zDefendManager, navController) }
            composable("policies") { PolicyScreen(zDefendManager, navController) }
            composable("troubleshoot") { TroubleshootScreen(zDefendManager, navController) }
            composable("simulate") { SimulateScreen(zDefendManager, navController) }
            composable("audit") { AuditScreen(zDefendManager, navController) }
            composable("linked") { LinkedScreen(zDefendManager, navController) }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            for (audit in zDefendManager.auditLogs) {
                Text(text = audit)
            }
        }
    }
}

@Composable
fun MainScreen(zDefendManager: ZDefendManager, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header(zDefendManager)
        NavigationGrid(navController)
    }
}

@Composable
fun Header(zDefendManager: ZDefendManager) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { zDefendManager.checkForUpdates() }) {
            Icon(Icons.Default.Refresh, contentDescription = "updates")
        }
        Text(
            text = "ZDefend",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun NavigationGrid(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationCard(text = "Threats", modifier = Modifier.weight(1f)) {
                navController.navigate("threats")
            }
            NavigationCard(text = "Policies", modifier = Modifier.weight(1f)) {
                navController.navigate("policies")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationCard(text = "Troubleshoot", modifier = Modifier.weight(1f)) {
                navController.navigate("troubleshoot")
            }
            NavigationCard(text = "Simulate", modifier = Modifier.weight(1f)) {
                navController.navigate("simulate")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            NavigationCard(text = "Audit", modifier = Modifier.weight(1f)) {
                navController.navigate("audit")
            }
            NavigationCard(text = "Linked", modifier = Modifier.weight(1f)) {
                navController.navigate("linked")
            }
        }
    }
}

@Composable
fun NavigationCard(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Blue),
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(1f),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TrafficLikeTheme {
        AppNavigation(zDefendManager = ZDefendManager.shared)
    }
}