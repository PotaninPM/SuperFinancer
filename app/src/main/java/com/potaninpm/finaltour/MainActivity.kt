package com.potaninpm.finaltour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.potaninpm.finaltour.navigation.RootNavigation
import com.potaninpm.finaltour.theme.DraftForProdTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                DraftForProdTheme {
                    RootNavigation()
                }
            }
        }
    }
}
