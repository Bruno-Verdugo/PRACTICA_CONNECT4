package com.example.practica

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue

class HelpViewModel : ViewModel() {

    val instructions by mutableStateOf(
        "Benvingut al joc del CONNECT 4!\n\n" +
                "El joc consisteix en anar introduint fitxes en un tauler vertical amb l'objectiu d'alinear 4 consecutives del color assignat al jugador abans que el teu oponent.\n\n" +
                "A cada torn, el jugador introdueix una fitxa a la columna triada (sempre i quan no estigui completa) i aquesta cau a la posició més baixa possible.\n\n" +
                "Guanya la partida el jugador que primer aconsegueixi alinear 4 fitxes consecutives del seu color en horitzontal, vertical o diagonal.\n\n" +
                "Si totes les columnes estan plenes i ningú ha aconseguit alinear 4 fitxes, hi ha empat."
    )
}