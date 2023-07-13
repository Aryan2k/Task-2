package com.example.task2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat.startActivity
import com.example.task2.ui.theme.Task2Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task2Theme {
                LoadPerms()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadPerms() {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val checkedState = rememberSaveable {
        mutableStateOf(false)
    }

    val permissionStatusTextState = rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current as Activity
    val permsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkedState.value = true
                permissionStatusTextState.value = "Camera permission Granted"
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(context, intent, null)
            } else {
                checkedState.value = false
                if (shouldShowRequestPermissionRationale(
                        context,
                        android.Manifest.permission.CAMERA
                    )
                ) {
                    permissionStatusTextState.value =
                        "The camera is important for this app.\nPlease grant the permission."
                } else {
                    permissionStatusTextState.value = "Camera permission Denied"
                }
            }
        }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        LoadSwitchStateless(checkedState.value, onCheckedChange = { value ->
            if (value) {
                permsLauncher.launch(android.Manifest.permission.CAMERA)
            } else {
                checkedState.value = false
            }
        })
        LoadTextStateless(permissionStatusTextState.value)
    }

    LaunchedEffect(Unit) {
        when {
            cameraPermissionState.status.isGranted -> {
                // Camera permission already granted
            }

            else -> {
                // Request camera permission
            }
        }
    }
}

@Composable
fun LoadSwitchStateless(checkedState: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = checkedState,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun LoadTextStateless(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(16.dp),
        fontSize = 14.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Task2Theme {
        LoadPerms()
    }
}