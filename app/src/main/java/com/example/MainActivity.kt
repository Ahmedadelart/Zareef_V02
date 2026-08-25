package com.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.di.AppContainer
import com.example.ui.account.AccountScreen
import com.example.ui.account.AccountViewModel
import com.example.ui.account.SettingsScreen
import com.example.ui.artist.ArtistScreen
import com.example.ui.artist.ArtistViewModel
import com.example.ui.category.CategoryScreen
import com.example.ui.category.CategoryViewModel
import com.example.ui.gift.RedeemScreen
import com.example.ui.pack.PackScreen
import com.example.ui.pack.PackViewModel
import com.example.ui.search.SearchScreen
import com.example.ui.search.SearchViewModel
import com.example.ui.shop.ShopScreen
import com.example.ui.shop.ShopViewModel
import com.example.ui.submit.SubmitScreen
import com.example.ui.theme.Accent
import com.example.ui.theme.Ink
import com.example.ui.theme.Line
import com.example.ui.theme.Muted
import com.example.ui.theme.Surface
import com.example.ui.theme.ZareefTheme
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class MainActivity : AppCompatActivity() {
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (AppCompatDelegate.getApplicationLocales().isEmpty) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ar"))
        }

        appContainer = AppContainer(applicationContext)
        enableEdgeToEdge()
        setContent {
            ZareefTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { ZareefBottomNavigation(navController) },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    ZareefNavHost(
                        navController = navController,
                        appContainer = appContainer,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ZareefBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    // Only show bottom nav on top level destinations
    if (currentRoute !in listOf("shop", "search", "account")) return

    NavigationBar(
        containerColor = Surface,
        contentColor = Muted,
        tonalElevation = 0.dp,
        modifier = Modifier.padding(top = 1.dp) // Simulate 1dp top border, actual drawing needs different approach
    ) {
        val items = listOf(
            Triple("shop", R.string.nav_shop, Icons.Default.ShoppingCart),
            Triple("search", R.string.nav_search, Icons.Default.Search),
            Triple("account", R.string.nav_account, Icons.Default.Person)
        )
        
        items.forEach { (route, labelRes, icon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(labelRes),
                        tint = if (selected) Accent else Muted
                    )
                },
                label = {
                    Text(
                        stringResource(labelRes),
                        color = if (selected) Ink else Muted,
                        fontSize = 11.sp,
                        fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Medium else androidx.compose.ui.text.font.FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Surface // Invisible indicator
                )
            )
        }
    }
}

@Composable
fun ZareefNavHost(
    navController: NavHostController,
    appContainer: AppContainer,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "shop",
        modifier = modifier
    ) {
        composable("shop") {
            val vm: ShopViewModel = viewModel(factory = ShopViewModel.provideFactory(appContainer.stickerRepository, appContainer.userPrefs))
            ShopScreen(
                viewModel = vm,
                onPackClick = { slug -> navController.navigate("pack/$slug") },
                onArtistClick = { id -> navController.navigate("artist/$id") },
                onCategoryClick = { id -> navController.navigate("category/$id") },
                onSubmitClick = { navController.navigate("submit") }
            )
        }
        composable("search") {
            val vm: SearchViewModel = viewModel(factory = SearchViewModel.provideFactory(appContainer.stickerRepository, appContainer.analyticsTracker))
            SearchScreen(viewModel = vm, onPackClick = { slug -> navController.navigate("pack/$slug") })
        }
        composable("account") {
            val vm: AccountViewModel = viewModel(factory = AccountViewModel.provideFactory(appContainer.authHandler, appContainer.userPrefs, appContainer.stickerRepository))
            AccountScreen(
                viewModel = vm,
                onSettingsClick = { navController.navigate("settings") },
                onRedeemClick = { navController.navigate("redeem") },
                onPackClick = { slug -> navController.navigate("pack/$slug") }
            )
        }
        composable(
            route = "pack/{slug}",
            deepLinks = listOf(navDeepLink { uriPattern = "https://zareef.app/p/{slug}" })
        ) { backStackEntry ->
            val slug = backStackEntry.arguments?.getString("slug") ?: return@composable
            val vm: PackViewModel = viewModel(factory = PackViewModel.provideFactory(
                appContainer.stickerRepository, appContainer.userPrefs, appContainer.purchaseHandler, appContainer.analyticsTracker, slug
            ))
            PackScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onArtistClick = { id -> navController.navigate("artist/$id") },
                onCategoryClick = { id -> navController.navigate("category/$id") },
                onPackClick = { newSlug -> navController.navigate("pack/$newSlug") }
            )
        }
        composable("artist/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val vm: ArtistViewModel = viewModel(factory = ArtistViewModel.provideFactory(appContainer.stickerRepository, appContainer.analyticsTracker, id))
            ArtistScreen(viewModel = vm, onBack = { navController.popBackStack() }, onPackClick = { slug -> navController.navigate("pack/$slug") })
        }
        composable("category/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val vm: CategoryViewModel = viewModel(factory = CategoryViewModel.provideFactory(appContainer.stickerRepository, id))
            CategoryScreen(viewModel = vm, onBack = { navController.popBackStack() }, onPackClick = { slug -> navController.navigate("pack/$slug") })
        }
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() }, authHandler = appContainer.authHandler)
        }
        composable("submit") {
            SubmitScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = "redeem?code={code}",
            deepLinks = listOf(navDeepLink { uriPattern = "https://zareef.app/g/{code}" })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            RedeemScreen(
                code = code,
                onBack = { navController.popBackStack() },
                purchaseHandler = appContainer.purchaseHandler,
                onSuccess = { slug -> 
                    navController.popBackStack()
                    navController.navigate("pack/$slug")
                }
            )
        }
    }
}
