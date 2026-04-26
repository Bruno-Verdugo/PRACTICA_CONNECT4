package com.example.practica.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.practica.R
val RighteousFont = FontFamily(
    Font(R.font.righteous_regular, FontWeight.Normal)
)

val MontserratFont = FontFamily(
    Font(R.font.montserrat_variablefont_wght, FontWeight.Normal),
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        letterSpacing = 2.sp
    ),

    titleLarge = TextStyle(
        fontFamily = RighteousFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        letterSpacing = 1.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = MontserratFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )
)