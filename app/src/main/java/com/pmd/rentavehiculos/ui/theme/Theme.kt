package com.pmd.rentavehiculos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
//
//@Composable
//fun RentaVehiculosTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}

private val DarkColorScheme = darkColorScheme(
    primary = Yellow,        // Color principal (botones, acentos)
    secondary = LightGray,   // Usado en detalles secundarios
    tertiary = DarkGray,     // Fondo de tarjetas

    background = Black,      // Fondo oscuro principal
    surface = DarkGray,      // Superficie de tarjetas y contenedores
    onPrimary = DarkText,    // Texto sobre amarillo
    onSecondary = DarkText,  // Texto sobre secundarios
    onTertiary = White,      // Texto sobre fondo oscuro
    onBackground = White,    // Texto principal sobre fondo oscuro
    onSurface = LightText    // Texto sobre tarjetas
)

private val LightColorScheme = lightColorScheme(
    primary = Yellow,        // Color principal (botones, acentos)
    secondary = DarkGray,    // Usado en detalles secundarios
    tertiary = LightGray,    // Fondo de tarjetas

    background = White,      // Fondo claro principal
    surface = LightGray,     // Superficie de tarjetas y contenedores
    onPrimary = DarkText,    // Texto sobre amarillo
    onSecondary = White,     // Texto sobre secundarios
    onTertiary = Black,      // Texto sobre fondo claro
    onBackground = DarkText, // Texto principal sobre fondo claro
    onSurface = Black        // Texto sobre tarjetas
)



@Composable
fun RentaVehiculosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,  // Aquí aplicamos la nueva tipografía
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
