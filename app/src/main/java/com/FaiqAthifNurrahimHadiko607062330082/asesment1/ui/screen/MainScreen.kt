package com.FaiqAthifNurrahimHadiko607062330082.asesment1.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.FaiqAthifNurrahimHadiko607062330082.asesment1.R
import com.FaiqAthifNurrahimHadiko607062330082.asesment1.navigation.Screen
import com.FaiqAthifNurrahimHadiko607062330082.asesment1.ui.theme.Asesment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    var suhuInput by rememberSaveable { mutableStateOf("") }
    var inputError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    val units = listOf(
        stringResource(R.string.celsius),
        stringResource(R.string.fahrenheit),
        stringResource(R.string.kelvin)
    )

    var fromUnit by rememberSaveable { mutableStateOf(units[0]) }
    var toUnit by rememberSaveable { mutableStateOf(units[1]) }

    var result by rememberSaveable { mutableFloatStateOf(0f) }
    var hasConverted by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.suhu_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = suhuInput,
            onValueChange = { suhuInput = it },
            label = { Text(stringResource(R.string.masukkan_suhu)) },
            trailingIcon = { IconPicker(inputError, "°") },
            supportingText = { ErrorHint(inputError) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = stringResource(R.string.dari_satuan), style = MaterialTheme.typography.labelLarge)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            units.forEach { unit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (unit == fromUnit),
                            onClick = {
                                if (unit == toUnit) {
                                    toUnit = fromUnit
                                }
                                fromUnit = unit
                                hasConverted = false
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (unit == fromUnit),
                        onClick = null
                    )
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Text(text = stringResource(R.string.ke_satuan), style = MaterialTheme.typography.labelLarge)
        UnitDropdownPicker(
            label = stringResource(R.string.ke_satuan),
            options = units,
            selectedOption = toUnit,
            onSelect = { newValue ->
                if (newValue == fromUnit) {
                    fromUnit = toUnit
                }
                toUnit = newValue
                hasConverted = false
            }
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    inputError = suhuInput.isBlank()
                    if (inputError) return@Button

                    val input = suhuInput.toFloatOrNull()
                    if (input != null) {
                        result = convertTemperature(input, fromUnit, toUnit)
                        hasConverted = true
                    }
                },
                modifier = Modifier.padding(top = 4.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(stringResource(R.string.konversi))
            }
            Button(
                onClick = {
                    suhuInput = ""
                    result = 0F
                    hasConverted = false
                },
                modifier = Modifier.padding(top = 4.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(stringResource(R.string.reset))
            }
        }


        if (hasConverted) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 1.dp)
            Text(
                text = stringResource(R.string.hasil_konversi, result, toUnit),
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = {
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.bagikan_template,
                            suhuInput.toFloat(),
                            fromUnit,
                            toUnit,
                            result
                        )
                    )
                },
                modifier = Modifier.padding(top = 4.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(stringResource(R.string.bagikan))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdownPicker(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onSelect(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun convertTemperature(value: Float, from: String, to: String): Float {
    return when (from to to) {
        "Celsius" to "Fahrenheit" -> value * 9 / 5 + 32
        "Celsius" to "Kelvin" -> value + 273.15f
        "Fahrenheit" to "Celsius" -> (value - 32) * 5 / 9
        "Fahrenheit" to "Kelvin" -> (value - 32) * 5 / 9 + 273.15f
        "Kelvin" to "Celsius" -> value - 273.15f
        "Kelvin" to "Fahrenheit" -> (value - 273.15f) * 9 / 5 + 32
        else -> value
    }
}


@SuppressLint("QueryPermissionsNeeded")
private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment1Theme {
        MainScreen(rememberNavController())
    }
}
