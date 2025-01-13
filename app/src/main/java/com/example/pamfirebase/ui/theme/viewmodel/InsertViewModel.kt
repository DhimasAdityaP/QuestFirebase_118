package com.example.pamfirebase.ui.theme.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.repository.RepositoryMhs
import kotlinx.coroutines.launch

// Data class untuk menyimpan informasi mahasiswa yang diinput oleh pengguna.
data class MahasiswaEvent (
    val nim: String = "",
    val nama: String = "",
    val gender: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

// Fungsi ekstensi untuk mengonversi MahasiswaEvent menjadi objek Mahasiswa.
fun MahasiswaEvent.toMahasiswa(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    gender = gender,
    alamat = alamat,
    kelas = kelas,
    angkatan = angkatan
)

// Data class untuk menyimpan status kesalahan validasi inputan dari pengguna.
data class FormErrorState (
    val nim: String? = null,
    val nama: String? = null,
    val gender: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null
) {
    // Fungsi untuk memeriksa apakah semua inputan valid (tidak ada pesan kesalahan).
    fun isValid(): Boolean {
        return nim == null && nama == null && gender == null && alamat == null && kelas == null && angkatan == null
    }
}

// Data class untuk menyimpan status UI saat input data mahasiswa.
data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState()
)

// Sealed class untuk menggambarkan status form (Idle, Loading, Success, Error).
sealed class FormState {
    object Idle : FormState() // Form dalam keadaan tidak aktif.
    object Loading : FormState() // Form dalam keadaan sedang memuat data.
    data class Success(val message: String) : FormState() // Form berhasil menyimpan data.
    data class Error(val message: String) : FormState() // Form mengalami kesalahan.
}

// ViewModel untuk mengelola logika penyimpanan data mahasiswa.
class InsertViewModel (
    private val mhs: RepositoryMhs // Repository untuk menyimpan data mahasiswa.
): ViewModel() {

    // UI state yang menyimpan data mahasiswa dan status validasi.
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set

    // UI state untuk menyimpan status form (Idle, Loading, Success, Error).
    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    // Fungsi untuk memperbarui status inputan mahasiswa.
    fun updateUiEvent(event: MahasiswaEvent) {
        uiEvent = uiEvent.copy(insertUiEvent = event)
    }

    // Fungsi untuk melakukan validasi inputan pengguna.
    fun validateFields (): Boolean {
        // Mendapatkan data dari inputan MahasiswaEvent.
        val event = uiEvent.insertUiEvent

        // Memeriksa apakah setiap field kosong, jika kosong beri pesan kesalahan.
        val errorState = FormErrorState(
            nim = if (event.nim.isEmpty()) "NIM tidak boleh kosong" else null,
            nama = if (event.nama.isEmpty()) "Nama tidak boleh kosong" else null,
            gender = if (event.gender.isEmpty()) "Jenis Kelamin tidak boleh kosong" else null,
            alamat = if (event.alamat.isEmpty()) "Alamat tidak boleh kosong" else null,
            kelas = if (event.kelas.isEmpty()) "Kelas tidak boleh kosong" else null,
            angkatan = if (event.angkatan.isEmpty()) "Angkatan tidak boleh kosong" else null
        )

        // Memperbarui status validasi inputan mahasiswa.
        uiEvent = uiEvent.copy(isEntryValid = errorState)

        // Jika tidak ada kesalahan, data valid.
        return errorState.isValid()
    }

    // Fungsi untuk menyimpan data mahasiswa ke repository.
    fun insertMhs () {
        // Mengecek apakah data valid sebelum disimpan.
        if (validateFields()) {
            // Menggunakan coroutine untuk proses penyimpanan data secara asinkron.
            viewModelScope.launch {
                // Menampilkan status Loading saat data sedang disimpan.
                uiState = FormState.Loading
                try {
                    // Menyimpan data mahasiswa ke repository.
                    mhs.insertMhs(uiEvent.insertUiEvent.toMahasiswa())
                    // Jika berhasil, tampilkan pesan sukses.
                    uiState = FormState.Success("Berhasil Menambahkan Mahasiswa")
                } catch (e: Exception) {
                    // Jika gagal, tampilkan pesan error.
                    uiState = FormState.Error("Gagal Menambahkan Mahasiswa")
                }
            }
        } else {
            // Jika data tidak valid, tampilkan pesan error.
            uiState = FormState.Error("Data Tidak Valid")
        }
    }

    // Fungsi untuk mereset form dan status UI.
    fun resetForm() {
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    // Fungsi untuk mereset pesan snackbar atau status UI.
    fun resetSnackBarMessage() {
        uiState = FormState.Idle
    }
}
