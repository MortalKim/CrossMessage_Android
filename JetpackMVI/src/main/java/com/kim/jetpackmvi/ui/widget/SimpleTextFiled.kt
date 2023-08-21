package com.kim.jetpackmvi.ui.widget

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @ClassName: SimpleTextFiled
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 2/19/23 12:10 PM
 */
@Composable
fun SimpleTextFiled(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    indicatorWidth: Dp = 1.dp,
    focusedIndicatorColor: Color =
        MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.high),
    unfocusedIndicatorColor: Color =
        MaterialTheme.colorScheme.onSurface.copy(alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity),
    disabledIndicatorColor: Color = unfocusedIndicatorColor.copy(alpha = ContentAlpha.disabled),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: SolidColor = SolidColor(Color.Black),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val focused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        cursorBrush = cursorBrush,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center
            ) {
                if (value.isEmpty()) {
                    val color =
                        if (enabled)
                            MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium)
                        else
                            MaterialTheme.colorScheme.onSurface.copy(
                                ContentAlpha.disabled
                            )
                    ProvideTextStyle(
                        value = MaterialTheme.typography.titleMedium.copy(color = color)
                    ) {
                        placeholder?.let { it() }
                    }
                }
                innerTextField()
            }
        },
        modifier = modifier.drawBehind {
            val targetValue = when {
                !enabled -> disabledIndicatorColor
                focused -> focusedIndicatorColor
                else -> unfocusedIndicatorColor
            }
            val strokeWidth = indicatorWidth.value * density
            val y = size.height - strokeWidth / 2
            drawLine(
                targetValue,
                Offset(0f, y),
                Offset(size.width, y),
                strokeWidth
            )
        }
    )
}
