package de.genericandwildcard.scopedserviceexample.example

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination
import de.genericandwildcard.scopedserviceexample.navigation.destination.Screen
import de.genericandwildcard.scopedserviceexample.navigation.service.scopedService
import de.genericandwildcard.scopedserviceexample.navigation.stack.LocalNavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.backToStart
import de.genericandwildcard.scopedserviceexample.navigation.stack.pop
import kotlinx.coroutines.flow.update

object CommendDetailsDestination : Destination {
    override fun build() = CommendDetailsScreen()
}

class CommendDetailsScreen : Screen {
    override val destination get() = CommendDetailsDestination

    @Composable
    override fun Content() = Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text("Want to add some details?")
            Spacer(Modifier.size(8.dp))

            val formModel = scopedService(
                scope = CommendFormScope,
                factory = CommendFormModel.Factory,
            )

            Spacer(Modifier.size(8.dp))

            val details by formModel.details.collectAsState()
            OutlinedTextField(
                value = details,
                onValueChange = { newContent -> formModel.details.update { newContent } },
            )

            val navigationState = LocalNavigationState.current
            Row {
                OutlinedButton(onClick = { navigationState.pop() }) {
                    Text("Back")
                }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(onClick = { navigationState.backToStart() }) {
                    Text("Complete")
                }
            }
        }
    }
}