package com.example.task2

import android.os.Build
import android.os.Bundle
import android.util.Log
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
                LoadPerms(
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadPerms(modifier: Modifier = Modifier) {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val permissionStatusTextState = rememberSaveable {
        mutableStateOf("")
    }

    val checked = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Switch(
            checked = checked.value,
            onCheckedChange = { value ->
                if (value) {
                    cameraPermissionState.launchPermissionRequest()
                } else {
                    checked.value = false
                }
            },
        )

        Text(
            text = permissionStatusTextState.value,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            fontSize = 14.sp
        )
    }

    if (cameraPermissionState.status.isGranted) {
        Log.i("perms", "Camera permission Granted")
        permissionStatusTextState.value = "Camera permission Granted"
        checked.value = true
    } else {
        checked.value = false
        Log.i("perms", "Camera permission Denied")
        if (cameraPermissionState.status.shouldShowRationale) {
            permissionStatusTextState.value =
                "The camera is important for this app.\nPlease grant the permission."
        } else {
            permissionStatusTextState.value = "Camera permission Denied"
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Task2Theme {
        LoadPerms()
    }
}