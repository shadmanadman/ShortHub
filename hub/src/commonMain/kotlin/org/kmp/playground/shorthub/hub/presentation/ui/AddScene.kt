package org.kmp.playground.shorthub.hub.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kmp.playground.shorthub.hub.presentation.add.AddIntent
import org.kmp.playground.shorthub.hub.presentation.add.AddViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddShortcutScene(
    viewModel: AddViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    AddShortcutPopup(
        isVisible = state.isVisible,
        recordedShortcut = state.recordedShortcut,
        isRecording = state.isRecording,
        onToggleRecording = {
            if (state.isRecording) {
                viewModel.stopRecording()
            } else {
                viewModel.startRecording()
            }
        },
        onDismiss = { viewModel.onIntent(AddIntent.Dismiss) },
        onSave = { title, shortcut -> 
            viewModel.onIntent(AddIntent.SaveShortcut(title, shortcut))
        }
    )
}

@Composable
fun AddShortcutPopup(
    isVisible: Boolean,
    recordedShortcut: String,
    isRecording: Boolean,
    onToggleRecording: () -> Unit,
    onDismiss: () -> Unit,
    onSave: (title: String, action: String) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(
            initialScale = 0.85f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        exit = fadeOut() + scaleOut(targetScale = 0.85f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "New Shortcut",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    var title by remember { mutableStateOf("") }
                    var action by remember(recordedShortcut) { mutableStateOf(recordedShortcut) }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        placeholder = { Text("e.g. Open Terminal") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = action,
                        onValueChange = { action = it },
                        label = { Text("Action / Shortcut") },
                        placeholder = { Text("e.g. CMD + SHIFT + T") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        trailingIcon = {
                            Button(
                                onClick = onToggleRecording,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text(if (isRecording) "Save" else "Record")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        
                        Button(
                            onClick = { 
                                if (title.isNotBlank() && action.isNotBlank()) {
                                    onSave(title, action)
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Text("Save Shortcut")
                        }
                    }
                }
            }
        }
    }
}
