package com.upchiapas.gymflow.feature.rutina.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.upchiapas.gymflow.feature.rutina.domain.model.Rutina
import com.upchiapas.gymflow.feature.rutina.presentation.viewmodel.RutinaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaFormScreen(
    viewModel: RutinaViewModel,
    rutinaId: String?, // Si es nulo = Crear; Si trae ID = Editar
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Buscamos la rutina actual de la lista en memoria si estamos editando (RF-08)
    val rutinaActual = uiState.rutinas.find { it.id == rutinaId }

    // Estados efímeros precargados o vacíos
    var nombre by remember { mutableStateOf(rutinaActual?.nombre ?: "") }
    var grupoMuscular by remember { mutableStateOf(rutinaActual?.grupoMuscular ?: "") }
    var series by remember { mutableStateOf(rutinaActual?.series?.toString() ?: "") }
    var repeticiones by remember { mutableStateOf(rutinaActual?.repeticiones?.toString() ?: "") }
    var activa by remember { mutableStateOf(rutinaActual?.activa ?: true) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    // Si la operación fue exitosa (se guardó en API), regresamos automáticamente
    LaunchedEffect(uiState.operacionExitosa) {
        if (uiState.operacionExitosa) {
            viewModel.resetOperacion()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (rutinaId == null) "Nueva Rutina" else "Editar Rutina") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre de la rutina") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = grupoMuscular, onValueChange = { grupoMuscular = it },
                label = { Text("Grupo Muscular (Ej. Pecho, Pierna)") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = series, onValueChange = { series = it },
                    label = { Text("Series") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = repeticiones, onValueChange = { repeticiones = it },
                    label = { Text("Reps") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = activa, onCheckedChange = { activa = it })
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rutina Activa")
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        errorLocal = null
                        val s = series.toIntOrNull() ?: 0
                        val r = repeticiones.toIntOrNull() ?: 0

                        // Validaciones solicitadas en el dominio
                        if (nombre.isBlank() || grupoMuscular.isBlank() || s <= 0 || r <= 0) {
                            errorLocal = "Llena todos los campos correctamente con números válidos"
                        } else {
                            val nuevaRutina = Rutina(
                                id = rutinaId ?: "", // Si es nueva va vacío, si edita lleva el ID
                                nombre = nombre,
                                grupoMuscular = grupoMuscular,
                                series = s,
                                repeticiones = r,
                                activa = activa,
                                fechaCreacion = rutinaActual?.fechaCreacion ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            )
                            if (rutinaId == null) viewModel.createRutina(nuevaRutina)
                            else viewModel.updateRutina(nuevaRutina)
                        }
                    }
                ) {
                    Text("Guardar Rutina")
                }
            }

            errorLocal?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp)) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp)) }
        }
    }
}