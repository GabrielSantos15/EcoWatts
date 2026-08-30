package br.com.fiap.EcoWatts.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.EcoWatts.R
import br.com.fiap.EcoWatts.model.User
import br.com.fiap.EcoWatts.navigation.Destination
import br.com.fiap.EcoWatts.repository.RoomUserRepository
import br.com.fiap.EcoWatts.repository.SessionRepository
import br.com.fiap.EcoWatts.ui.theme.EcoWatssTheme
import br.com.fiap.EcoWatts.util.convertBitmapToByteArray
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current

    val placeholderImage = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.default_avatar)
    }

    var profileImage by remember { mutableStateOf<Bitmap>(placeholderImage) }

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                profileImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                profileImage = ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        BottomStartCard(modifier = Modifier.align(Alignment.BottomStart))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileTitle()
            Spacer(modifier = Modifier.height(48.dp))

            UserImage(
                profileImage = profileImage,
                launchImage = launchImage
            )
            ProfileForm(navController, profileImage) { loadedImage ->
                profileImage = loadedImage
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ProfileScreenPreview() {
    EcoWatssTheme {
        ProfileScreen(rememberNavController())
    }
}

@Composable
fun ProfileTitle(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Meu Perfil",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Atualize seus dados",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun ProfileForm(
    navController: NavController,
    profileImage: Bitmap,
    onImageLoaded: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sessionRepository = remember { SessionRepository(context) }
    val userRepository = remember { RoomUserRepository(context) }

    var userId by remember { mutableStateOf(0) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isCityError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    var showDialogError by remember { mutableStateOf(false) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDialogLogout by remember { mutableStateOf(false) }

    var showPassword by remember { mutableStateOf(false) }

    // Carrega os dados atuais do usuário
    LaunchedEffect(Unit) {
        val loggedInId = sessionRepository.getUserId()
        if (loggedInId != 0) {
            val user = userRepository.getUser(loggedInId)
            if (user != null) {
                userId = user.id
                name = user.name
                email = user.email
                password = user.password
                city = user.city

                user.userImage?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    onImageLoaded(bitmap)
                }
            }
        }
    }

    fun validate(): Boolean {
        isNameError = name.length < 3
        isEmailError = email.length < 3 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isCityError = city.length < 3
        isPasswordError = password.length < 3
        return !isNameError && !isEmailError && !isCityError && !isPasswordError
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(text = "Nome", style = MaterialTheme.typography.labelSmall)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            isError = isNameError,
            trailingIcon = {
                if (isNameError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
                }
            },
            supportingText = {
                if (isNameError) {
                    Text(
                        text = stringResource(R.string.invalid_name),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(text = "Cidade", style = MaterialTheme.typography.labelSmall)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PinDrop,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            isError = isCityError,
            trailingIcon = {
                if (isCityError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
                }
            },
            supportingText = {
                if (isCityError) {
                    Text(
                        text = stringResource(R.string.invalid_city),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(text = "E-mail", style = MaterialTheme.typography.labelSmall)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = isEmailError,
            trailingIcon = {
                if (isEmailError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
                }
            },
            supportingText = {
                if (isEmailError) {
                    Text(
                        text = stringResource(R.string.invalid_email),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(text = "Senha", style = MaterialTheme.typography.labelSmall)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = isPasswordError,
            trailingIcon = {
                val image =
                    if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = image,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            supportingText = {
                if (isPasswordError) {
                    Text(
                        text = stringResource(R.string.invalid_password),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (validate()) {
                    coroutineScope.launch {
                        val updatedUser = User(
                            id = userId,
                            name = name,
                            email = email,
                            password = password,
                            city = city,
                            userImage = convertBitmapToByteArray(profileImage)
                        )

                        userRepository.update(updatedUser)

                        showDialogSuccess = true
                    }
                } else {
                    showDialogError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Salvar alterações", style = MaterialTheme.typography.labelMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { showDialogLogout = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Sair da conta", style = MaterialTheme.typography.labelMedium)
        }
    }

    // sucesso
    if (showDialogSuccess) {
        AlertDialog(
            onDismissRequest = { showDialogSuccess = false },
            title = { Text(text = "Sucesso") },
            text = { Text(text = "Dados atualizados com sucesso") },
            confirmButton = {
                TextButton(onClick = {
                    showDialogSuccess = false
                    navController.navigate(Destination.HomeScreen.route)
                }) {
                    Text(text = "Ok",style = MaterialTheme.typography.labelMedium)
                }
            }
        )
    }

    // erro
    if (showDialogError) {
        AlertDialog(
            onDismissRequest = { showDialogError = false },
            title = { Text(text = "Erro") },
            text = { Text(text = "Preencha todos os campos corretamente") },
            confirmButton = {
                TextButton(onClick = {
                    showDialogError = false
                }) {
                    Text("Ok")
                }
            }
        )
    }

    // confirmação de logout
    if (showDialogLogout) {
        AlertDialog(
            onDismissRequest = { showDialogLogout = false },
            title = { Text(text = "Sair da conta") },
            text = { Text(text = "Tem certeza que deseja sair?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialogLogout = false
                        sessionRepository.logout()
                        navController.navigate(Destination.InitialScreen.route) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Text(text = "Sair", color = MaterialTheme.colorScheme.error,style = MaterialTheme.typography.labelMedium)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogLogout = false }) {
                    Text(text = "Cancelar",style = MaterialTheme.typography.labelMedium)
                }
            }
        )
    }
}