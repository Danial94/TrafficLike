package com.dev.trafficlike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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

data class ThreatModel(
    var id: String,
    val name: String,
    val severity: String,
    val status: Boolean,
    val description: String,
    val resolution: String,
)

class MainActivity : ComponentActivity() {
    private val zDefendManager : ZDefendManager = ZDefendManager()
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
    val navController = rememberNavController()
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("threats") { ThreatsScreen(zDefendManager, navController) }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Header()
        NavigationGrid(navController)
    }
}

@Composable
fun Header() {
    Text(
        text = "ZDefend",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
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
            NavigationCard(text = "Policies", modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationCard(text = "Troubleshoot", modifier = Modifier.weight(1f))
            NavigationCard(text = "Simulate", modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            NavigationCard(text = "Audit", modifier = Modifier.weight(1f))
            NavigationCard(text = "Linked", modifier = Modifier.weight(1f))
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

@Composable
fun ThreatsScreen(zDefendManager: ZDefendManager, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Threats",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ThreatAccordion(zDefendManager)
    }
}

@Composable
fun ThreatAccordion(zDefendManager: ZDefendManager) {
    val threats = zDefendManager.threats
    val expandedState = remember { mutableStateMapOf<String, Boolean>().apply { threats.forEach { this[it.id] = false } } }

    LazyColumn {
        items(threats) { threat ->
            ExpandableCard(
                threat = threat,
                expanded = expandedState[threat.id] ?: false,
                onCardArrowClick = { expandedState[threat.id] = !(expandedState[threat.id] ?: false) }
            )
        }
    }
}

@Composable
fun ExpandableCard(
    threat: ThreatModel,
    expanded: Boolean,
    onCardArrowClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onCardArrowClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = threat.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onCardArrowClick) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
            if (expanded) {
                Row {
                    Text(
                        text = threat.severity,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .padding(end = 16.dp)
                    )
                    Text(
                        text = if (threat.status) "FIXED" else "PENDING",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Text(
                    text = threat.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = threat.resolution,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TrafficLikeTheme {
        val zDefendManager = ZDefendManager()
        AppNavigation(zDefendManager = zDefendManager)
    }
}