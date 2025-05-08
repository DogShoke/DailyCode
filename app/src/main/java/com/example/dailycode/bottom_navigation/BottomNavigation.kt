package com.example.dailycode.bottom_navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigation(
    navController: NavController
){
    val listItems = listOf(
        BottomItem.Screen1,
        BottomItem.Screen2,
        BottomItem.Screen3
    )
    androidx.compose.material3.NavigationBar(
        containerColor = Color.White
    ){
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        listItems.forEach { item ->
           NavigationBarItem(
               selected = currentRoute == item.route,
               onClick = {
                   navController.navigate(item.route)
               },
               icon = {
                   Icon(painter = painterResource(id = item.iconId),
                       contentDescription = "Icon" )
               },
               label = {
                   Text(
                       text = item.title,
                       fontSize = 9.sp
                   )
               }
           )
        }
    }

}

