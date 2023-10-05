package de.genericandwildcard.scopedserviceexample.derivedstateof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import de.genericandwildcard.scopedserviceexample.ui.theme.ScopedServiceExampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class DerivedStateOfActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScopedServiceExampleTheme {
                Surface {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StaticValueExample()
                        Divider()
                        LocalValueExample()
                        Divider()
                        StateExample()
                        Divider()
                        StateFlowExample()
                        Divider()
                        ComposeViewModelExample()
                        Divider()
                        NonComposeViewModelExample()
                        Divider()
                        CombinedValueExample()
                    }
                }
            }
        }
    }
}

var staticValue = 3

@Composable
fun ColumnScope.StaticValueExample() {
    val updatedValue by remember { derivedStateOf { staticValue * 2 } }

    Text("Static Value Example")
    ExampleUi(
        initialValue = staticValue,
        calculatedValue = updatedValue,
        buttonAction = { staticValue += 1 }
    )
}

@Composable
fun ColumnScope.LocalValueExample() {
    var localValue = 3
    val updatedValue by remember { derivedStateOf { localValue * 2 } }

    Text("Local Value Example")
    ExampleUi(
        initialValue = localValue,
        calculatedValue = updatedValue,
        buttonAction = { localValue += 1 }
    )
}

@Composable
fun ColumnScope.StateExample() {
    var stateValue by remember { mutableIntStateOf(3) }
    val updatedValue by remember { derivedStateOf { stateValue * 2 } }

    Text("MutableState Example")
    ExampleUi(
        initialValue = stateValue,
        calculatedValue = updatedValue,
        buttonAction = { stateValue += 1 }
    )
}

@Composable
fun ColumnScope.StateFlowExample() {
    val stateFlowValue = remember { MutableStateFlow(3) }
    val updatedValue by remember { derivedStateOf { stateFlowValue.value * 2 } }

    val collectedStateFlowValue by stateFlowValue.collectAsState()
    Text("MutableStateFlow Example")
    ExampleUi(
        initialValue = collectedStateFlowValue,
        calculatedValue = updatedValue,
        buttonAction = { stateFlowValue.update { it + 1 } }
    )
}

abstract class AbstractExampleViewModel: ViewModel() {
    abstract var initialValue: Int
}

class ComposeExampleViewModel: AbstractExampleViewModel() {
    override var initialValue: Int by mutableIntStateOf(3)
}

class NonComposeExampleViewModel: AbstractExampleViewModel() {
    override var initialValue: Int = 3
}

@Composable
fun ColumnScope.ComposeViewModelExample() {
    val viewModel = viewModel<ComposeExampleViewModel>(key = "firstCompose")
    val updatedValue by remember { derivedStateOf { viewModel.initialValue * 2 } }

    Text("State in ViewModel Example")
    ExampleUi(
        initialValue = viewModel.initialValue,
        calculatedValue = updatedValue,
        buttonAction = { viewModel.initialValue += 1 }
    )
}

@Composable
fun ColumnScope.NonComposeViewModelExample() {
    val viewModel = viewModel<NonComposeExampleViewModel>(key = "firstNonCompose")
    val updatedValue by remember { derivedStateOf { viewModel.initialValue * 2 } }

    Text("Value in ViewModel Example")
    ExampleUi(
        initialValue = viewModel.initialValue,
        calculatedValue = updatedValue,
        buttonAction = { viewModel.initialValue += 1 }
    )
}

@Composable
fun ColumnScope.CombinedValueExample() {
    val viewModelNonCompose = viewModel<NonComposeExampleViewModel>(key = "secondNonCompose")
    val viewModelCompose = viewModel<ComposeExampleViewModel>(key = "secondCompose")
    val updatedValue by remember {
        derivedStateOf {
            viewModelNonCompose.initialValue + viewModelCompose.initialValue
        }
    }

    Text("Combining States with non State in ViewModel Example")
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Initial Value Non-State is ${viewModelNonCompose.initialValue}")
        Text("Initial Value With State is ${viewModelCompose.initialValue}")
        Text("Calculated Value is $updatedValue")
        ElevatedButton(onClick = { viewModelNonCompose.initialValue += 1 }) {
            Text("Increment Non State!")
        }
        ElevatedButton(onClick = { viewModelCompose.initialValue += 1 }) {
            Text("Increment State!")
        }
    }
}

@Composable
fun ExampleUi(
    initialValue: Int,
    calculatedValue: Int,
    modifier: Modifier = Modifier,
    buttonAction: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Initial Value is $initialValue")
        Text("Calculated Value is $calculatedValue")
        ElevatedButton(onClick = buttonAction) {
            Text("Increment!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExampleUiPreview() {
    ExampleUi(
        modifier = Modifier.padding(all = 16.dp),
        initialValue = 0,
        calculatedValue = 0,
        buttonAction = { },
    )
}