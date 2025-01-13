package com.example.pamfirebase.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.pamfirebase.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// Class repository untuk operasi CRUD pada koleksi "Mahasiswa" di Firestore
class NetworkRepositoryMhs(
    private val firestore: FirebaseFirestore
) : RepositoryMhs {

    // Fungsi untuk menambahkan data Mahasiswa ke Firestore
    override suspend fun insertMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (e: Exception) {
            throw Exception("Error saat menambahkan Mahasiswa: ${e.message}")
        }
    }

    // Fungsi untuk mengambil semua data Mahasiswa dari Firestore secara real-time
    override fun getAllMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow {
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING) // Mengurutkan data berdasarkan nim
            .addSnapshotListener { value, error ->
                if (value != null) {
                    // Mengonversi dokumen Firestore ke daftar data kelas Mahasiswa
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)
                    }
                    trySend(mhsList) // Mengirimkan daftar Mahasiswa melalui flow
                }
            }
        awaitClose {
            // Menutup listener Firestore ketika flow ditutup
            mhsCollection.remove()
        }
    }

    // Fungsi untuk mengambil data Mahasiswa berdasarkan NIM (real-time)
    override fun getMhs(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsDocument = firestore.collection("Mahasiswa")
            .document(nim)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    // Mengonversi dokumen Firestore ke data kelas Mahasiswa
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs) // Mengirimkan data Mahasiswa melalui flow
                }
            }
        awaitClose {
            // Menutup listener Firestore ketika flow ditutup
            mhsDocument.remove()
        }
    }

    // Fungsi untuk menghapus data Mahasiswa berdasarkan NIM
    override suspend fun deleteMhs(mahasiswa: Mahasiswa) {
        try {
            val querySnapshot = firestore.collection("Mahasiswa")
                .whereEqualTo("nim", mahasiswa.nim) // Mencari dokumen berdasarkan nim
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                Log.e("NetworkRepositoryMhs", "Dokumen dengan nim ${mahasiswa.nim} tidak ditemukan")
                return
            }

            val document = querySnapshot.documents.first() // Mengambil dokumen pertama
            val documentId = document.id // Mendapatkan ID dokumen

            // Menghapus dokumen dari Firestore berdasarkan ID
            firestore.collection("Mahasiswa")
                .document(documentId)
                .delete()
                .addOnSuccessListener { Log.d(TAG, "Dokumen berhasil dihapus!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error saat menghapus dokumen", e) }
                .await()

            Log.d("NetworkRepositoryMhs", "Berhasil menghapus data Mahasiswa: ${mahasiswa.nim}")
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data Mahasiswa: ${e.message}")
        }
    }

    // Fungsi untuk memperbarui data Mahasiswa berdasarkan NIM
    override suspend fun updateMhs(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim) // Menentukan dokumen berdasarkan NIM
                .set(mahasiswa) // Memperbarui data Mahasiswa
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal memperbarui data Mahasiswa: ${e.message}")
        }
    }
}