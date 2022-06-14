package jd.sistemas.android.placeholder_compose.ui.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.coil.rememberCoilPainter
import dagger.hilt.android.AndroidEntryPoint
import jd.sistemas.android.placeholder_compose.R
import jd.sistemas.android.placeholder_compose.State
import jd.sistemas.android.placeholder_compose.data.model.User
import jd.sistemas.android.placeholder_compose.ui.theme.UserActivityTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            UserActivityTheme {
                Scaffold {
                    NavGraphComponent(navController)
                }
            }
        }
    }

    @Composable
    private fun NavGraphComponent(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                MainView(navController)
            }
        }
    }


    @Composable
    fun EmployeeItem(onClick: () -> Unit, data: User) {
        Card(
            modifier = Modifier
                .padding(all = 5.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(15.dp),
            elevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colors.surface)
            ) {
                Surface(
                    modifier = Modifier.size(130.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colors.surface.copy(alpha = 0.2f)
                ) {
                    val image = rememberCoilPainter(
                        request = data.avatar,
                        fadeIn = true
                    )

                    Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .height(100.dp)
                            .clip(shape = RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    Modifier
                        .padding(start = 12.dp)
                        .align(Alignment.CenterVertically)
                )
                {
                    Text(
                        text = data.firstName,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontSize = 22.sp),
                        color = Color.Black
                    )
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.medium
                    ) {
                        Text(
                            text = data.email,
                            style = typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(end = 25.dp)
                        )
                    }
                }
            }
        }

    }


    @Composable
    fun MainView(navController: NavHostController) {
        val viewModel: MainViewModel by viewModels()
        val state by viewModel.state.collectAsState()
        when (state) {
            is State.START -> {}
            is State.LOADING -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is State.ERROR -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = stringResource(R.string.an_error), fontSize = 16.sp)
                }
            }
            is State.SUCCESS -> {
                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    state.data?.data?.let {
                        items(items = it) { user ->
                            EmployeeItem(onClick = {}, data = user)
                        }
                    }
                }
            }
        }
    }
}
