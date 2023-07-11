package com.example.task2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task2.ui.theme.Task2Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
fun LoadPerms(modifier: Modifier = Modifier) {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    DrawUI(
        modifier,
        requestPermissions = {
            cameraPermissionState.launchPermissionRequest()
        },
        cameraPermissionState.status.isGranted,
        cameraPermissionState.status.shouldShowRationale
    )
}

@Composable
fun DrawUI(
    modifier: Modifier,
    requestPermissions: () -> Unit,
    isCameraPermissionGranted: Boolean,
    shouldShowRationale: Boolean
) {

    val checkedState = rememberSaveable {
        mutableStateOf(false)
    }

    val permissionStatusTextState = rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        LoadSwitchStateless(checkedState.value, onCheckedChange = { value ->
            if (value) {
                requestPermissions()
            } else {
                checkedState.value = false
            }
        })
        LoadTextStateless(permissionStatusTextState.value)
    }

    if (isCameraPermissionGranted) {
        checkedState.value = true
        permissionStatusTextState.value = "Camera permission Granted"
    } else {
        checkedState.value = false
        if (shouldShowRationale) {
            permissionStatusTextState.value =
                "The camera is important for this app.\nPlease grant the permission."
        } else {
            permissionStatusTextState.value = "Camera permission Denied"
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