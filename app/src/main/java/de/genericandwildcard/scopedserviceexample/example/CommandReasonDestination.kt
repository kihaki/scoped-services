package de.genericandwildcard.scopedserviceexample.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination
import de.genericandwildcard.scopedserviceexample.navigation.destination.Screen
import de.genericandwildcard.scopedserviceexample.navigation.service.scopedService
import de.genericandwildcard.scopedserviceexample.navigation.stack.LocalNavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.pop
import de.genericandwildcard.scopedserviceexample.navigation.stack.push

object CommendReasonDestination : Destination {
    override fun build() = CommendReasonScreen()
}

class CommendReasonScreen : Screen {
    override val destination get() = CommendReasonDestination

    @Composable
    override fun Content() = Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text("Tweet Commend Reason")
            Spacer(Modifier.size(8.dp))

            val formModel = scopedService(
                scope = CommendFormScope,
                factory = CommendFormModel.Factory,
            )

            val commendReasons = remember { listOf(null) + CommendReason.entries }
            val selectedReason by formModel.reason.collectAsState()
            LazyColumn {
                items(commendReasons) { commendReason ->
                    ListItem(
                        modifier = Modifier.clickable { formModel.reason.value = commendReason },
                        headlineContent = { Text(commendReason.label) },
                        trailingContent = if (selectedReason == commendReason) {
                            @Composable {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = "selected",
                                )
                            }
                        } else null
                    )
                }
            }

            Spacer(Modifier.size(8.dp))

            val navigationState = LocalNavigationState.current
            Row {
                OutlinedButton(onClick = { navigationState.pop() }) {
                    Text("Back")
                }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(onClick = { navigationState.push(CommendDetailsDestination) }) {
                    Text("Add details")
                }
            }
        }
    }
}