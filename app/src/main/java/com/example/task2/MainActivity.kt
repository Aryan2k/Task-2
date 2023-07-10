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
                LoadPermsStateful()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LoadPermsStateful(modifier: Modifier = Modifier) {

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    LoadPermsStateless(
        modifier,
        requestPermissions = {
            Log.i("aryan", "hi")
            cameraPermissionState.launchPermissionRequest()
        },
        cameraPermissionState.status.isGranted,
        cameraPermissionState.status.shouldShowRationale
    )

}

@Composable
fun LoadPermsStateless(
    modifier: Modifier,
    requestPermissions: () -> Unit,
    isCameraPermissionGranted: Boolean,
    shouldShowRationale: Boolean
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        LoadSwitchStateful(requestPermissions = requestPermissions)
        LoadTextStateful()
    }

    if (isCameraPermissionGranted) {
        Log.i("aryan", "granted")
        LoadSwitchStateful(requestPermissions = {}, changeCheckedStateValue = true, newValue = true)
        LoadTextStateful(changeTextStateValue = true, newText = "Camera permission Granted")
    } else {
        LoadSwitchStateful(
            requestPermissions = {},
            changeCheckedStateValue = true,
            newValue = false
        )
        //   Log.i("aryan", shouldShowRationale.toString())
        if (shouldShowRationale) {
            LoadTextStateful(
                changeTextStateValue = true,
                newText = "The camera is important for this app.\nPlease grant the permission."
            )
        } else {
            LoadTextStateful(
                changeTextStateValue = true,
                newText = "Camera permission Denied"
            )
        }
    }
}

@Composable
fun LoadSwitchStateful(
    requestPermissions: () -> Unit,
    changeCheckedStateValue: Boolean = false,
    newValue: Boolean = false
) {

    val checkedState = rememberSaveable {
        mutableStateOf(false)
    }

    Log.i("aryan", "changeCheckedState: " + changeCheckedStateValue.toString())
    if (changeCheckedStateValue) {
        checkedState.value = newValue
        Log.i("aryan", "checkedState: " + checkedState.value.toString())
    } else {
        LoadSwitchStateless(checkedState.value, onCheckedChange = { value ->
            if (value) {
                requestPermissions()
            } else {
                checkedState.value = false
            }
        })
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
fun LoadTextStateful(changeTextStateValue: Boolean = false, newText: String = "") {

    val permissionStatusTextState = rememberSaveable {
        mutableStateOf("")
    }

    if (changeTextStateValue) {
        permissionStatusTextState.value = newText
    } else {
        LoadTextStateless(permissionStatusTextState.value)
    }
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
        LoadPermsStateful()
    }
}