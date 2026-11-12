package com.upchiapas.gymflow.feature.rutina.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina
import com.upchiapas.gymflow.feature.rutina.presentation.viewmodel.RutinaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaListScreen(
    viewModel: RutinaViewModel,
    onNavigateToForm: (String?) -> Unit, // Si recibe null es "Crear", si recibe ID es "Editar"
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Estado efímero para el diálogo de confirmación de eliminación (RF-09)
    var rutinaAEliminar by remember { mutableStateOf<Rutina?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Rutinas") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToForm(null) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Rutina")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.rutinas.isEmpty()) {
                Text("No tienes rutinas registradas.", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.rutinas) { rutina ->
                        RutinaCard(
                            rutina = rutina,
                            onEdit = { onNavigateToForm(rutina.id) },
                            onDelete = { rutinaAEliminar = rutina }
                        )
                    }
                }
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                )
            }
        }
    }

    // Diálogo de Confirmación (RF-09)
    rutinaAEliminar?.let { rutina ->
        AlertDialog(
            onDismissRequest = { rutinaAEliminar = null },
            title = { Text("Eliminar Rutina") },
            text = { Text("¿Estás seguro de eliminar '${rutina.nombre}'?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteRutina(rutina.id)
                    rutinaAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { rutinaAEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun RutinaCard(rutina: Rutina, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(rutina.nombre, style = MaterialTheme.typography.titleLarge)
                Text(rutina.grupoMuscular, style = MaterialTheme.typography.bodyMedium)
                Text("${rutina.series} series x ${rutina.repeticiones} reps", style = MaterialTheme.typography.bodySmall)
                if (rutina.activa) {
                    Text("Activa", color = Color(0xFF4CAF50), style = MaterialTheme.typography.labelSmall)
                }
            }
            Row {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
}