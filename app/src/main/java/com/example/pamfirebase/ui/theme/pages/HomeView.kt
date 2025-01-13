package com.example.pamfirebase.ui.theme.pages


// Import statement untuk dependensi yang diperlukan
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pamfirebase.R
import com.example.pamfirebase.model.Mahasiswa
import com.example.pamfirebase.ui.theme.viewmodel.HomeUiState
import com.example.pamfirebase.ui.theme.viewmodel.HomeViewModel
import com.example.pamfirebase.ui.theme.viewmodel.PenyediaViewModel

// Dialog konfirmasi untuk penghapusan data mahasiswa
@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, // Fungsi callback saat konfirmasi penghapusan
    onDeleteCancel: () -> Unit, // Fungsi callback saat pembatalan penghapusan
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Tidak ada aksi */ }, // Dialog tidak bisa ditutup dengan klik di luar
        title = { Text("Delete Data") }, // Judul dialog
        text = { Text("Apakah anda yakin ingin menghapus data?") }, // Pesan dialog
        modifier = modifier,
        dismissButton = { // Tombol untuk membatalkan penghapusan
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = { // Tombol untuk mengonfirmasi penghapusan
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}

// Kartu tampilan data mahasiswa dengan desain yang diperbarui
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMhs(
    mhs: Mahasiswa,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    onDeleteClick: (Mahasiswa) -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = mhs.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = mhs.nim,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(mhs) }
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "", tint = MaterialTheme.colorScheme.tertiary)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.kelas,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.gender,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.alamat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "", tint = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = mhs.angkatan,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

// Daftar mahasiswa dengan styling baru
@Composable
fun ListMahasiswa(
    listMhs: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = { },
    onDeleteClick: (Mahasiswa) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = listMhs,
            itemContent = { mhs ->
                CardMhs(
                    mhs = mhs,
                    onClick = { onClick(mhs.nim) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        )
    }
}


// Tampilan error
@Composable
fun OnError(
    retryAction: () -> Unit, // Aksi untuk mencoba lagi
    modifier: Modifier = Modifier,
    message: String // Pesan error
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.network_error), contentDescription = ""
        )
        Text(text = message, modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("Coba Lagi") // Tombol untuk retry
        }
    }
}

// Tampilan loading
@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading), // Gambar loading
        contentDescription = ""
    )
}

// Status halaman Home
@Composable
fun HomeStatus(
    homeUiState: HomeUiState, // Status UI dari halaman Home
    retryAction: () -> Unit, // Aksi retry jika terjadi error
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {}, // Callback untuk klik detail
    onDeleteClick: (Mahasiswa) -> Unit = {} // Callback untuk hapus mahasiswa
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf<Mahasiswa?>(null) }
    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeUiState.Success -> {
            ListMahasiswa(
                listMhs = homeUiState.data,
                onClick = { onDetailClick(it) },
                onDeleteClick = {
                    deleteConfirmationRequired = it // Menampilkan dialog konfirmasi hapus
                }
            )
            deleteConfirmationRequired?.let { data ->
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        onDeleteClick(data)
                        deleteConfirmationRequired = null // Menutup dialog
                    },
                    onDeleteCancel = {
                        deleteConfirmationRequired = null
                    }
                )
            }
        }
        is HomeUiState.Error -> OnError(
            message = homeUiState.e.localizedMessage?: "error",
            retryAction = retryAction,
            modifier = modifier.fillMaxWidth()
        )
    }
}

// Halaman utama Home
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit, // Navigate to item entry screen
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {}, // Callback for detail click
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    Text(text = "Home View", color = MaterialTheme.colorScheme.onPrimary)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior // Apply scroll behavior for top app bar
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Mahasiswa") // Button to add item
            }
        }
    ) { innerPadding ->
        // Add padding for cards to be below the TopAppBar
        Column(
            modifier = Modifier
                .padding(innerPadding) // Apply padding to account for the TopAppBar
                .fillMaxSize()
        ) {
            HomeStatus(
                homeUiState = viewModel.mhsUiState,
                retryAction = { viewModel.getMhs() }, // Reload data action
                modifier = Modifier.fillMaxSize(),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    viewModel.deleteMhs(it) // Delete student action in ViewModel
                }
            )
        }
    }
}

