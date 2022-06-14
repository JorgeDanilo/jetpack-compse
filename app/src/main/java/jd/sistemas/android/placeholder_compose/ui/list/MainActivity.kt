package jd.sistemas.android.placeholder_compose.ui.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jd.sistemas.android.placeholder_compose.R
import jd.sistemas.android.placeholder_compose.State
import jd.sistemas.android.placeholder_compose.data.model.User
import jd.sistemas.android.placeholder_compose.ui.detail.DetailViewModel
import jd.sistemas.android.placeholder_compose.ui.theme.UserActivityTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val detailViewModel: DetailViewModel by viewModels()

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
            composable("detail") {
                UserDetailScreen()
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
                UserListScreen(state.data, navController)
            }
        }
    }

    @Composable
    fun UserListScreen(users: List<User>?, navController: NavHostController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Users") }
                )
            }
        ) {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                users?.let {
                    items(items = it) { user ->
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Black, shape = CircleShape)
                                    .size(50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.substring(0, 1),
                                    color = Color.White,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .align(CenterVertically)
                                    .padding(start = 6.dp)
                            ) {
                                Text(
                                    text = user.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = user.email,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Button(onClick = {
                                    detailViewModel.fetchUser(user.id.toString()).also {
                                        navController.navigate("detail")
                                    }
                                }) {
                                    Text(text = "Go to Detail")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun UserDetailScreen() {

        val state by detailViewModel.state.collectAsState()

        when (state) {
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
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "User Detail")
                            }
                        )
                    },
                    content = {
                        Column {
                            Column(Modifier.padding(all = 16.dp)) {
                                state.data?.let {
                                    Text(text = it.name)
                                    Text(text = it.email)
                                }
                            }
                        }
                    }
                )
            }
            else -> {}
        }
    }
}
