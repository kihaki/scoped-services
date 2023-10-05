package de.genericandwildcard.scopedserviceexample.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.genericandwildcard.scopedserviceexample.R
import de.genericandwildcard.scopedserviceexample.navigation.destination.Destination
import de.genericandwildcard.scopedserviceexample.navigation.destination.Screen
import de.genericandwildcard.scopedserviceexample.navigation.stack.LocalNavigationState
import de.genericandwildcard.scopedserviceexample.navigation.stack.push

object EntryDestination : Destination {
    override fun build() = EntryScreen()
}

class EntryScreen : Screen {
    override val destination get() = EntryDestination

    @Composable
    override fun Content() = Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Inspecting following tweet.. commend?")
            Spacer(Modifier.size(8.dp))

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                painter = painterResource(R.drawable.tweet),
                contentScale = ContentScale.FillWidth,
                contentDescription = "the-tweet",
            )

            Spacer(Modifier.size(8.dp))

            val backStackState = LocalNavigationState.current
            OutlinedButton(onClick = { backStackState.push(CommendReasonDestination) }) {
                Text("Open Commend Form")
            }
        }
    }
}