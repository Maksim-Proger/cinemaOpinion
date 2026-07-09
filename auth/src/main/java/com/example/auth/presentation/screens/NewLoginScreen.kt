package com.example.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.auth.presentation.viewmodel.AuthViewModel
import com.example.ui.presentation.components.InlineBottomSheet


@Composable
fun NewLoginScreen(
    onLoginSuccess: (String, Boolean) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // region Переменные

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val userData by authViewModel.userData.collectAsState()
    val loginResult by authViewModel.authResult.collectAsState()

    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    var openBottomSheet by remember { mutableStateOf(false) }

    // endregion
    // region LaunchedEffect

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.isSuccess) {
                userData?.let { user ->
                    onLoginSuccess(user.id, true)
                }
            }
            authViewModel.resetAuthResult()
        }
    }
    LaunchedEffect(Unit) {
        authViewModel.toastMessage.collect { resId ->
            val message = context.getString(resId)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // endregion

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NewColors.NeuBg)
                .padding(WindowInsets.statusBars.asPaddingValues()),
        ) {
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text(text = "Кино Мнение", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 37.sp,
                ))
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .neuRaised(cornerRadius = 28.dp)
                    .background(NewColors.NeuBg, RoundedCornerShape(28.dp))
                    .padding(horizontal = 28.dp, vertical = 36.dp)
            ) {

                Text(
                    text = "Username",
                    color = NewColors.LabelText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(Modifier.height(10.dp))
                NeuTextField(
                    value = login,
                    onValueChange = setLogin,
                    placeholder = "admin@CSSScript.com",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                Spacer(Modifier.height(22.dp))
                Text(
                    text = "Password",
                    color = NewColors.LabelText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(Modifier.height(10.dp))
                NeuTextField(
                    value = password,
                    onValueChange = setPassword,
                    placeholder = "Enter Password",
                    isPassword = true,
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
                    label = "Вход",
                    onClick = { authViewModel.authorization(login, password) }
                )
                Spacer(Modifier.height(28.dp))
                SignInButton(
                    label = "Регистрация",
                    onClick = { openBottomSheet = true }
                )
            }

            Spacer(Modifier.weight(1f))
        }

        InlineBottomSheet(
            visible = openBottomSheet,
            onDismissRequest = { openBottomSheet = false },
            containerColor = NewColors.NeuBg
        ) {
            NewRegistrationScreen(
                authViewModel = authViewModel,
                fraction = 0.9f,
                onClose = { openBottomSheet = false }
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
    isPassword: Boolean = false,
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
        visualTransformation =
            if (isPassword) PasswordVisualTransformation()
            else VisualTransformation.None,
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
    label: String,
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
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

