package com.lacolinares.ragemusicph.extensions

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

@ExperimentalAnimationApi
fun NavGraphBuilder.customComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    val offSetX = 300
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = { _, _ -> slideInHorizontally(
            initialOffsetX = { offSetX },
            animationSpec = tween(
                durationMillis = offSetX,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(offSetX)) },
        exitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { -offSetX },
                animationSpec = tween(
                    durationMillis = offSetX,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(offSetX))
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -offSetX },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(offSetX))
        },
        popExitTransition = { _, _ -> slideOutHorizontally(
            targetOffsetX = { offSetX },
            animationSpec = tween(
                durationMillis = offSetX,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(animationSpec = tween(offSetX)) }
    ) {
        content.invoke(it)
    }
}