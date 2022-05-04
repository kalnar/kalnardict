package eu.kalnarapps.kalnardict.androidui.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MockDbImportActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MockDbScreen()
        }
    }

}

@Composable
@Preview
fun MockDbScreen() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MockCard()
        }
    }

}

@Composable
fun MockCard() {

    val isClicked = remember { mutableStateOf(false) }

    Column {
        CardComponent("mock db test") {
            isClicked.value = !isClicked.value
        }

        if (isClicked.value) {
            Text("ok")
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardComponent(title: String, onClickAction: (() -> Unit)? = null) {
    Card(
        shape = RoundedCornerShape(25),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth(),
        elevation = 10.dp,
        onClick = {
            onClickAction?.invoke()
        }
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
            text = title
        )
    }
}
