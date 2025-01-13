package com.example.pamfirebase.repository

import com.example.pamfirebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface RepositoryMhs {
    // operasi CRUD
    suspend fun insertMhs(mahasiswa: Mahasiswa)
    fun getAllMahasiswa(): Flow<List<Mahasiswa>>
    fun getMhs (nim: String): Flow<Mahasiswa>
    suspend fun deleteMhs(mahasiswa: Mahasiswa)
    suspend fun updateMhs(mahasiswa: Mahasiswa)
}