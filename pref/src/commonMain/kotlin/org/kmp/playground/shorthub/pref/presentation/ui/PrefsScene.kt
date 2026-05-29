package org.kmp.playground.shorthub.pref.presentation.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kmp.playground.shorthub.pref.presentation.RecordingTarget
import org.kmp.playground.shorthub.pref.presentation.PrefsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrefsScene(
    viewModel: PrefsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "backgroundAnimation")
    
    val color1 by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.surface,
        targetValue = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(color1, color2)))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ShortHub",
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            ShortcutOptionItem(
                title = "Global Add Shortcut",
                description = "Hotkey to quickly add a new shortcut",
                currentShortcut = if (state.recordingTarget == RecordingTarget.AddShortcut) {
                    state.recordedShortcut ?: state.prefs.addNewShortcut
                } else state.prefs.addNewShortcut,
                isRecording = state.recordingTarget == RecordingTarget.AddShortcut,
                onToggleRecording = {
                    if (state.recordingTarget == RecordingTarget.AddShortcut) {
                        viewModel.saveRecordedShortcut()
                    } else {
                        viewModel.startRecording(RecordingTarget.AddShortcut)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ShortcutOptionItem(
                title = "Search Shortcuts",
                description = "Hotkey to open the search overlay",
                currentShortcut = if (state.recordingTarget == RecordingTarget.SearchShortcut) {
                    state.recordedShortcut ?: state.prefs.searchShortcut
                } else state.prefs.searchShortcut,
                isRecording = state.recordingTarget == RecordingTarget.SearchShortcut,
                onToggleRecording = {
                    if (state.recordingTarget == RecordingTarget.SearchShortcut) {
                        viewModel.saveRecordedShortcut()
                    } else {
                        viewModel.startRecording(RecordingTarget.SearchShortcut)
                    }
                }
            )
        }
    }
}

@Composable
private fun ShortcutOptionItem(
    title: String,
    description: String,
    currentShortcut: String,
    isRecording: Boolean,
    onToggleRecording: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = currentShortcut,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Button(
                onClick = onToggleRecording,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isRecording) "Save" else "Change")
            }
        }
    }
}
