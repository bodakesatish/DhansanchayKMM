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
import androidx.navigation.toRoute
import com.bodakesatish.kmm.dhansanchay.app.screens.detail.DetailScreen
import kotlinx.serialization.Serializable
import com.bodakesatish.kmm.dhansanchay.app.screens.list.ListScreen

@Serializable
object ListDestination

@Serializable
data class DetailDestination(val schemeCode: Long, val schemeName: String)

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
                        ListScreen(navigateToDetails = { objectId, scheme ->
                            navController.navigate(DetailDestination(scheme.schemeCode, scheme.schemeName))
                        })
                    }
                    composable<DetailDestination> { backStackEntry ->
                        val detailArgs = backStackEntry.toRoute<DetailDestination>()
                        DetailScreen(
//                            scheme = detailArgs.scheme,
                            schemeCode = detailArgs.schemeCode,
                            schemeName = detailArgs.schemeName,
                            onNavigateUp = {
                                navController.popBackStack()
                            }
                            //                        objectId = backStackEntry.toRoute<DetailDestination>().objectId,

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