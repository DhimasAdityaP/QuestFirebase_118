package com.example.pamfirebase.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.repository.RepositoryMhs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// Kelas Sealed untuk menggambarkan berbagai status UI pada halaman Home.
sealed class HomeUiState {
    // Status ketika data berhasil diambil dan berisi daftar Mahasiswa.
    data class Success(val data: List<Mahasiswa>) : HomeUiState()

    // Status ketika terjadi kesalahan (misalnya kesalahan jaringan atau database).
    data class Error(val e: Throwable) : HomeUiState()

    // Status ketika data sedang dimuat (misalnya saat mengambil data dari repository).
    object Loading : HomeUiState()
}

// HomeViewModel bertanggung jawab untuk mengelola data UI dan logika bisnis.
class HomeViewModel(
    private val repoMhs: RepositoryMhs // Repository untuk berinteraksi dengan data layer.
) : ViewModel() {

    // Status UI untuk halaman Home, awalnya diatur ke Loading.
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set // Setter hanya dapat diubah di dalam ViewModel

    // Blok inisialisasi yang akan memanggil fungsi getMhs ketika ViewModel pertama kali dibuat.
    init {
        getMhs() // Mengambil data Mahasiswa saat aplikasi pertama kali dijalankan
    }

    // Fungsi untuk menghapus data Mahasiswa.
    fun deleteMhs(mhs: Mahasiswa) {
        viewModelScope.launch {
            try {
                // Mencoba menghapus data Mahasiswa menggunakan repository.
                repoMhs.deleteMhs(mhs)
            } catch (e: Exception) {
                // Jika terjadi kesalahan, status UI akan diperbarui menjadi Error.
                mhsUiState = HomeUiState.Error(e)
            }
        }
    }

    // Fungsi untuk mengambil data Mahasiswa dari repository.
    fun getMhs() {
        viewModelScope.launch {
            // Menggunakan coroutine untuk mengambil data secara asinkron.
            repoMhs.getAllMahasiswa()
                .onStart {
                    // Menampilkan status UI Loading ketika data mulai diambil.
                    mhsUiState = HomeUiState.Loading
                }
                .catch {
                    // Jika terjadi kesalahan saat mengambil data, status UI akan diperbarui menjadi Error.
                    mhsUiState = HomeUiState.Error(e = it)
                }
                .collect {
                    // Mengumpulkan data dan memperbarui status UI berdasarkan apakah data kosong atau tidak.
                    mhsUiState = if (it.isEmpty()) {
                        // Jika data Mahasiswa kosong, tampilkan status Error.
                        HomeUiState.Error(Exception("Belum ada data mahasiswa"))
                    } else {
                        // Jika ada data, status UI akan diperbarui menjadi Success.
                        HomeUiState.Success(data = it)
                    }
                }
        }
    }
}

