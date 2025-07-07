package com.bodakesatish.kmm.dhansanchay

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bodakesatish.kmm.dhansanchay.app.screens.detail.DetailScreen
import kotlinx.serialization.Serializable
import com.bodakesatish.kmm.dhansanchay.app.screens.list.ListScreen
import org.koin.compose.KoinContext

@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)

@Composable
fun App() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
//        KoinContext {
            Surface {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = ListDestination) {
                    composable<ListDestination> {
                        ListScreen(navigateToDetails = { objectId ->
                            navController.navigate(DetailDestination(objectId))
                        })
                    }
                    composable<DetailDestination> { backStackEntry ->
                        DetailScreen(
//                        objectId = backStackEntry.toRoute<DetailDestination>().objectId,
//                        navigateBack = {
//                            navController.popBackStack()
//                        }
                        )
                    }
                }
            }
//        }
    }
}


//@Composable
//@Preview
//fun App() {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}