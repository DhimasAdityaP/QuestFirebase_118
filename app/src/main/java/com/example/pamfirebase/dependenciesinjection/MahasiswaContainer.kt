package com.example.pamfirebase.dependenciesinjection

import com.example.pamfirebase.MahasiswaApp
import com.example.pamfirebase.repository.NetworkRepositoryMhs
import com.example.pamfirebase.repository.RepositoryMhs
import com.google.firebase.firestore.FirebaseFirestore



interface InterfaceContainerApp {
    val repositoryMhs: RepositoryMhs
}

class ContainerApp(private val context: MahasiswaApp): InterfaceContainerApp {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    override val repositoryMhs: RepositoryMhs by lazy {
        NetworkRepositoryMhs(firestore)
    }
}