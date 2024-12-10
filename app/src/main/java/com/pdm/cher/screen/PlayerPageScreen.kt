package com.pdm.cher.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.util.lerp
import com.pdm.cher.card.FavoriteGamesCard
import com.pdm.cher.card.HomeCard
import com.pdm.cher.card.ProfileCard
import com.pdm.cher.data.Player
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerPageScreen(
    player: Player,
    onEnterLobby: (Player) -> Unit,
    onAvailableRepetitions: (Array<String>) -> List<String>,
    onUploadImage: (Player, ByteArray, MutableState<ImageBitmap?>) -> Unit,
    onGetImage: (String, MutableState<ImageBitmap?>) -> Unit,
    bitmapState: MutableState<ImageBitmap?>) {
    val pagerState = rememberPagerState(pageCount = { 4 }, initialPage = 1)
    HorizontalPager(state = pagerState) { page ->
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                            .absoluteValue

                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }

        ) {
            when (page) {
                0 -> ProfileCard(player, onUploadImage, onGetImage, bitmapState)
                1 -> HomeCard(player, onEnterLobby)
                2 -> FavoriteGamesCard(onAvailableRepetitions, player)
                3 -> CreditCard()
            }
        }
    }
}



