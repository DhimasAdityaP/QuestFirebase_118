package com.example.pamfirebase.navigation

interface DestinasiNavigasi {
    val route: String
    val titleRes: String
}

object DestinasiHome: DestinasiNavigasi {
    override val route = "home"
    override val titleRes = "Home"
}

object DestinasiInsert: DestinasiNavigasi {
    override val route = "insert"
    override val titleRes = "Insert"

}

