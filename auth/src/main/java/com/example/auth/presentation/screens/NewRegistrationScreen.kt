package com.example.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.viewmodel.AuthViewModel

@Composable
fun NewRegistrationScreen(
    authViewModel: AuthViewModel,
    fraction: Float,
    onClose: () -> Unit
) {

    // region Переменные

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    var triggerOnClick by remember { mutableStateOf(false) }

    // endregion

    LaunchedEffect(triggerOnClick) {
        if (triggerOnClick) {
            authViewModel.toastMessage.collect {
                onClose()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
            .background(NewColors.NeuBg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .padding(24.dp)
                .fillMaxWidth()
                .neuRaised(cornerRadius = 28.dp)
                .background(NewColors.NeuBg, RoundedCornerShape(28.dp))
                .padding(horizontal = 28.dp, vertical = 36.dp)
        ) {
            Text(
                text = "Имя пользователя",
                color = NewColors.LabelText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 4.dp)
            )
            NeuTextField(
                value = nikName,
                onValueChange = setNikName,
                placeholder = "Доминатор2000",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            )
            Text(
                text = "Email",
                color = NewColors.LabelText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 4.dp)
            )
            NeuTextField(
                value = email,
                onValueChange = setEmail,
                placeholder = "admin@CSSScript.com",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            )
            Text(
                text = "Password",
                color = NewColors.LabelText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 4.dp)
            )
            NeuTextField(
                value = password,
                onValueChange = setPassword,
                placeholder = "Enter Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(Modifier.height(28.dp))
            SignInButton(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    authViewModel.addUser(nikName, email, password)
                    triggerOnClick = true
                }
            )
        }
    }

}

@Composable
private fun NeuTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .neuInset(cornerRadius = 26.dp),
        singleLine = true,
        textStyle = TextStyle(color = NewColors.InputText, fontSize = 15.sp),
        cursorBrush = SolidColor(NewColors.InputText),
        visualTransformation = VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = NewColors.PlaceholderText,
                        fontSize = 15.sp
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
private fun SignInButton(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .neuRaised(cornerRadius = 27.dp)
            .clip(RoundedCornerShape(27.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        NewColors.ButtonTop,
                        NewColors.ButtonBottom
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Сохранить",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


