package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.model.ContentMode
import org.example.project.model.GridSize

@Composable
fun SetupScreen(
    onStartGame: (GridSize, ContentMode) -> Unit
) {
    var selectedGridSize by remember { mutableStateOf(GridSize.FOUR_BY_FOUR) }
    var selectedContentMode by remember { mutableStateOf(ContentMode.EMOJI) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game Setup",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text("Select Grid Size:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Row(Modifier.padding(vertical = 16.dp)) {
            GridSizeOption("4x4", GridSize.FOUR_BY_FOUR, selectedGridSize) { selectedGridSize = it }
            GridSizeOption("5x5", GridSize.FIVE_BY_FIVE, selectedGridSize) { selectedGridSize = it }
            GridSizeOption("6x6", GridSize.SIX_BY_SIX, selectedGridSize) { selectedGridSize = it }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Select Content Mode:", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Row(Modifier.padding(vertical = 16.dp)) {
            ContentModeOption("Emoji", ContentMode.EMOJI, selectedContentMode) { selectedContentMode = it }
            ContentModeOption("Numbers", ContentMode.NUMBER, selectedContentMode) { selectedContentMode = it }
            ContentModeOption("Colors", ContentMode.COLOR, selectedContentMode) { selectedContentMode = it }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onStartGame(selectedGridSize, selectedContentMode) },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Start Match Up!", fontSize = 18.sp)
        }
    }
}

@Composable
fun GridSizeOption(
    text: String,
    option: GridSize,
    selectedOption: GridSize,
    onSelect: (GridSize) -> Unit
) {
    val selected = option == selectedOption
    FilterChip(
        selected = selected,
        onClick = { onSelect(option) },
        label = { Text(text) },
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
fun ContentModeOption(
    text: String,
    option: ContentMode,
    selectedOption: ContentMode,
    onSelect: (ContentMode) -> Unit
) {
    val selected = option == selectedOption
    FilterChip(
        selected = selected,
        onClick = { onSelect(option) },
        label = { Text(text) },
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}
