package com.example.pamfirebase.ui.theme.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pamfirebase.ui.theme.viewmodel.FormErrorState
import com.example.pamfirebase.ui.theme.viewmodel.FormState
import com.example.pamfirebase.ui.theme.viewmodel.InsertUiState
import com.example.pamfirebase.ui.theme.viewmodel.InsertViewModel
import com.example.pamfirebase.ui.theme.viewmodel.MahasiswaEvent
import com.example.pamfirebase.ui.theme.viewmodel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    onBack: () -> Unit, // Navigasi kembali
    onNavigate: () -> Unit, // Navigasi setelah sukses
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory) // ViewModel
) {
    val uiState = viewModel.uiState
    val uiEvent = viewModel.uiEvent
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Efek samping untuk menangani state perubahan
    LaunchedEffect(uiState) {
        when (uiState) {
            is FormState.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
                delay(700)
                onNavigate() // Navigasi setelah sukses
                viewModel.resetSnackBarMessage()
            }
            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
            else -> Unit
        }
    }

    // Scaffold dengan top bar dan snackbar
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insert Mahasiswa",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Body for insert mahasiswa
            InserBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updatedEvent ->
                    viewModel.updateUiEvent(updatedEvent)
                },
                onClick = {
                    if (viewModel.validateFields()) {
                        viewModel.insertMhs()
                    }
                }
            )
        }
    }
}

@Composable
fun InserBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState, // State dari proses insert
    onClick: () -> Unit = {}, // Aksi saat tombol diklik
    homeUiState: FormState // State dari form
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Form for mahasiswa
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )

        // Insert button with loading indicator
        Spacer(modifier = Modifier.height(16.dp)) // Added space between form and button
        Button(
            onClick = onClick,
            enabled = homeUiState !is FormState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (homeUiState is FormState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp).padding(end = 8.dp)
                )
                Text("Loading")
            } else {
                Text("Insert")
            }
        }
    }
}

@Composable
fun FormMahasiswa(
    mahasiswaEvent: MahasiswaEvent,
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState,
    modifier: Modifier = Modifier
) {
    val gender = listOf("Laki-laki", "Perempuan") // Pilihan gender
    val kelas = listOf("A", "B", "C", "D", "E") // Pilihan kelas

    // Layout kolom untuk form input
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Input nama
        OutlinedTextField(
            value = mahasiswaEvent.nama,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nama = it)) },
            label = { Text("Nama") },
            isError = errorState.nama != null, // Validasi error
            placeholder = { Text("Masukkan Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = errorState.nama ?: "", color = Color.Red) // Menampilkan pesan error

        // Input NIM
        OutlinedTextField(
            value = mahasiswaEvent.nim,
            onValueChange = { onValueChange(mahasiswaEvent.copy(nim = it)) },
            label = { Text("NIM") },
            isError = errorState.nim != null,
            placeholder = { Text("Masukkan NIM") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = errorState.nim ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp)) // Jarak antar elemen

        // Pilihan gender menggunakan RadioButton
        Text("Jenis Kelamin")
        Row(modifier = Modifier.fillMaxWidth()) {
            gender.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mahasiswaEvent.gender == item,
                        onClick = { onValueChange(mahasiswaEvent.copy(gender = item)) }
                    )
                    Text(text = item)
                }
            }
        }
        Text(text = errorState.gender ?: "", color = Color.Red)

        // Input alamat
        OutlinedTextField(
            value = mahasiswaEvent.alamat,
            onValueChange = { onValueChange(mahasiswaEvent.copy(alamat = it)) },
            label = { Text("Alamat") },
            isError = errorState.alamat != null,
            placeholder = { Text("Masukkan Alamat") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = errorState.alamat ?: "", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        // Pilihan kelas
        Row(modifier = Modifier.fillMaxWidth()) {
            kelas.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = mahasiswaEvent.kelas == item,
                        onClick = { onValueChange(mahasiswaEvent.copy(kelas = item)) }
                    )
                    Text(text = item)
                }
            }
        }
        Text(text = errorState.kelas ?: "", color = Color.Red)

        // Input angkatan
        OutlinedTextField(
            value = mahasiswaEvent.angkatan,
            onValueChange = { onValueChange(mahasiswaEvent.copy(angkatan = it)) },
            label = { Text("Angkatan") },
            isError = errorState.angkatan != null,
            placeholder = { Text("Masukkan Angkatan") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = errorState.angkatan ?: "", color = Color.Red)
    }
}
