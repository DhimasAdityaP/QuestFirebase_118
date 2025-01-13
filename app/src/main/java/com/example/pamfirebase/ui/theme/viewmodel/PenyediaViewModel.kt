package com.example.pamfirebase.ui.theme.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pamfirebase.MahasiswaApp

fun CreationExtras.aplikasiMahasiswa(): MahasiswaApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApp)

object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer { HomeViewModel(aplikasiMahasiswa().containerApp.repositoryMhs)}
        initializer { InsertViewModel(aplikasiMahasiswa().containerApp.repositoryMhs)}
    }
}