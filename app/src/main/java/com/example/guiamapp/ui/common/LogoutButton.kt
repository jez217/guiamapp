package com.example.guiamapp.ui.common

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.ui.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun LogoutButton(
    tokenStore: TokenStore,
    nav: NavController
) {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            tokenStore.clear()
            nav.navigate(Routes.Login) {
                popUpTo(0)
            }
        }
    }) {
        Text("Cerrar sesión")
    }
}