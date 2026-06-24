package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun AchievementsScreen(
    navController: NavHostController,
    listAwards: String
) {
    Scaffold() { innerPadding ->
        Spacer(Modifier.fillMaxSize().padding(innerPadding))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Экран с наградами!"
            )
        }
    }
}

//// region AwardsFields
//Column(
//modifier = Modifier.padding(10.dp),
//verticalArrangement = Arrangement.Bottom
//) {
//    TextAwardsFields(listAwards)
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (listAwards.isNotEmpty()) {
//            val newListAwards = listAwards.split(",")
//            for (i in newListAwards) {
//                Image(
//                    painter = painterResource(id = i.toInt()),
//                    contentDescription = null,
//                    modifier = Modifier.height(70.dp)
//                )
//            }
//        }
//    }
//}
//// endregion
