package org.proven.rutesdeviatge.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import org.proven.rutesdeviatge.model.Difficulty
import org.proven.rutesdeviatge.model.TravelDiaryUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val DATE_PATTERN = "dd/MM/yyyy"

/**
 * Primera pantalla: formulari per crear una ruta.
 *
 * Tots els camps són controlats pel ViewModel. La pantalla només envia els
 * canvis cap amunt i pinta l'estat que rep (patró unidireccional de Compose).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    uiState: TravelDiaryUiState,
    onRouteNameChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onBudgetChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onSoloChange: (Boolean) -> Unit,
    onNotesChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Diari de Viatges",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Crònica de ruta",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeroCard()

            SectionTitle("Detalls de la ruta")

            OutlinedTextField(
                value = uiState.routeName,
                onValueChange = onRouteNameChange,
                label = { Text("Nom de la ruta") },
                placeholder = { Text("Ex.: Camí de Ronda") },
                leadingIcon = { Icon(Icons.Outlined.Map, contentDescription = null) },
                isError = uiState.routeNameError != null,
                supportingText = { Text(uiState.routeNameError ?: "Obligatori") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.destination,
                onValueChange = onDestinationChange,
                label = { Text("Destí") },
                placeholder = { Text("Ex.: Cadaqués, Girona") },
                leadingIcon = { Icon(Icons.Outlined.Place, contentDescription = null) },
                isError = uiState.destinationError != null,
                supportingText = { Text(uiState.destinationError ?: "Obligatori") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Nivell de dificultat",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.entries.forEach { difficulty ->
                    FilterChip(
                        selected = uiState.difficulty == difficulty,
                        onClick = { onDifficultyChange(difficulty) },
                        label = {
                            Text(
                                text = difficulty.label,
                                textAlign = TextAlign.Center
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            OutlinedTextField(
                value = uiState.estimatedBudget,
                onValueChange = onBudgetChange,
                label = { Text("Pressupost estimat (€)") },
                placeholder = { Text("Ex.: 250") },
                leadingIcon = { Icon(Icons.Outlined.Payments, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.budgetError != null,
                supportingText = { Text(uiState.budgetError ?: "Ha de ser més gran que 0") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text(
                    text = "Data de la ruta",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.date.ifBlank { "Selecciona una data" },
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                }
                if (uiState.dateError != null) {
                    Text(
                        text = uiState.dateError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ruta feta en solitari",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Viatjaré sense companyia",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = uiState.isSolo,
                        onCheckedChange = onSoloChange
                    )
                }
            }

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes de l'experiència") },
                placeholder = { Text("Què has vist? Què t'ha agradat més?") },
                leadingIcon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
                minLines = 4,
                maxLines = 7,
                supportingText = { Text("Opcional: pots escriure el relat de la jornada") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(2.dp))

            Button(
                onClick = onGenerate,
                enabled = uiState.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Generar Postal / Crònica",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedButton(
                onClick = onOpenHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Outlined.History, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Veure històric (${uiState.history.size})")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // DatePickerDialog de Material 3: només es crea quan l'usuari prem el botó.
    if (showDatePicker) {
        val initialDateMillis = remember(uiState.date) {
            if (uiState.date.isBlank()) {
                System.currentTimeMillis()
            } else {
                runCatching {
                    dateFormatter().parse(uiState.date)?.time
                }.getOrNull() ?: System.currentTimeMillis()
            }
        }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateChange(dateFormatter().format(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Acceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel·lar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}

@Composable
private fun HeroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "On serà la propera aventura?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Completa el formulari i converteix la teva ruta en una postal per recordar-la.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "🧭", fontSize = 42.sp)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * El DatePicker treballa amb mil·lisegons en UTC. Utilitzem el mateix fus
 * horari per convertir-los a text i evitar que la data es desplaci un dia.
 */
private fun dateFormatter(): SimpleDateFormat =
    SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
