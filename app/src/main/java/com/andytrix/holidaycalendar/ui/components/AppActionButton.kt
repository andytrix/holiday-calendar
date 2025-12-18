package com.andytrix.holidaycalendar.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.size

@Composable
fun AppActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    spinIconOnClick: Boolean = false
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val pressColor = containerColor.copy(
        red = containerColor.red * 0.8f,
        green = containerColor.green * 0.8f,
        blue = containerColor.blue * 0.8f
    )

    val animatedContainerColor = animateColorAsState(
        targetValue = when {
            isLoading -> pressColor
            isPressed.value -> pressColor
            else -> containerColor
        },
    )

    var spinTrigger by remember { mutableIntStateOf(0) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(spinTrigger) {
        if (!spinIconOnClick || spinTrigger == 0) return@LaunchedEffect

        rotation.snapTo(0f)
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 450)
        )
        rotation.snapTo(0f)
    }

    Button(
        onClick = {
            if (spinIconOnClick) {
                spinTrigger += 1
            }
            onClick()
        },
        enabled = enabled && !isLoading,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor.value,
            contentColor = contentColor
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = loadingText)
        } else {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.graphicsLayer(rotationZ = rotation.value)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = text)
        }
    }
}
