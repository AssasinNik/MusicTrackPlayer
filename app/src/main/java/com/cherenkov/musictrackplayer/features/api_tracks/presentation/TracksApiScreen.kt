package com.cherenkov.musictrackplayer.features.api_tracks.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.cherenkov.musictrackplayer.features.api_tracks.domain.model.Items
import kotlinx.coroutines.Dispatchers

@Composable
fun TracksApiScreen(
    viewModel: TracksApiViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1F1B24), Color(0xFF121212))
                )
            )
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing))
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = { query ->
                    searchQuery = query
                    viewModel.onAction(TracksApiAction.OnChangedText(query))
                },
                searchActive = searchActive,
                onFocusChanged = { isFocused -> searchActive = isFocused },
                onBackClicked = {
                    searchQuery = ""
                    searchActive = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Crossfade(targetState = Pair(searchActive, searchQuery), animationSpec = tween(700)) { (active, query) ->
                when {
                    active && query.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Введите название песни или исполнителя для поиска",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.LightGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    active && query.isNotEmpty() -> {
                        if (state.isFinding) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateContentSize(animationSpec = tween(700, easing = FastOutSlowInEasing)),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(10) { SongListItemShimmer() }
                            }
                        } else if (!state.isFinding && state.searchedSongs.isEmpty()) {
                            NoResultsFoundScreenWithoutButton()
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateContentSize(animationSpec = tween(700, easing = FastOutSlowInEasing)),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(state.searchedSongs) { song ->
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = fadeIn(
                                            animationSpec = tween(700, easing = FastOutSlowInEasing)
                                        ) + slideInVertically(
                                            initialOffsetY = { it / 3 },
                                            animationSpec = tween(700, easing = FastOutSlowInEasing)
                                        )
                                    ) {
                                        SongListItem(song = song)
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        if (state.isLoading) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateContentSize(animationSpec = tween(700, easing = FastOutSlowInEasing)),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                item { SectionTitle(title = "Популярное") }
                                item {
                                    LazyRow(
                                        modifier = Modifier.padding(vertical = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        items(5) { FeaturedSongItemShimmer() }
                                    }
                                }
                                item { SectionTitle(title = "Новое") }
                                items(10) { SongListItemShimmer() }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .animateContentSize(animationSpec = tween(700, easing = FastOutSlowInEasing)),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                if (state.charts.isNotEmpty()) {
                                    item { SectionTitle(title = "Популярное") }
                                    item {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            contentPadding = PaddingValues(horizontal = 16.dp)
                                        ) {
                                            items(state.charts.take(5)) { song ->
                                                FeaturedSongItem(song = song)
                                            }
                                        }
                                    }
                                    item { SectionTitle(title = "Новое") }
                                    items(state.charts) { song ->
                                        AnimatedVisibility(
                                            visible = true,
                                            enter = fadeIn(
                                                animationSpec = tween(700, easing = FastOutSlowInEasing)
                                            ) + slideInVertically(
                                                initialOffsetY = { it / 3 },
                                                animationSpec = tween(700, easing = FastOutSlowInEasing)
                                            )
                                        ) {
                                            SongListItem(song = song)
                                        }
                                    }
                                } else {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Нет доступных песен",
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    searchActive: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val animatedBorderColor by animateColorAsState(
        targetValue = if (searchActive) Color(0xFFBB86FC) else Color.LightGray,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
    )

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Поиск песни или исполнителя",
                color = Color.LightGray
            )
        },
        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
        leadingIcon = {
            if (searchActive) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Вернуться",
                    tint = Color.LightGray,
                    modifier = Modifier.clickable {
                        focusManager.clearFocus()
                        onBackClicked()
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Поиск",
                    tint = Color.LightGray
                )
            }
        },
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            onFocusChanged(focusState.isFocused)
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = animatedBorderColor,
            unfocusedBorderColor = animatedBorderColor,
            cursorColor = Color.White,
            containerColor = Color(0xFF1F1B24)
        )
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = Color.White,
        modifier = Modifier.padding(start = 24.dp, top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun NoResultsFoundScreenWithoutButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Ничего не найдено",
            tint = Color.LightGray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ой, ничего не найдено",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Composable
fun SongListItem(song: Items) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { /* Обработка клика по песне */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.cover)
                    .dispatcher(Dispatchers.IO)
                    .memoryCacheKey(song.cover)
                    .diskCacheKey(song.cover)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(rememberShimmerBrush())
                    )
                },
                contentDescription = "Обложка песни",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist_name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun SongListItemShimmer() {
    val shimmerBrush = rememberShimmerBrush()
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
    ) {
        Row(
            modifier = Modifier
                .height(110.dp)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(shimmerBrush)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmerBrush)
                )
            }
        }
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    )
    return Brush.linearGradient(
        colors = listOf(
            Color.DarkGray.copy(alpha = 0.6f),
            Color.Gray.copy(alpha = 0.2f),
            Color.DarkGray.copy(alpha = 0.6f)
        ),
        start = Offset(translateAnim.value, translateAnim.value),
        end = Offset(translateAnim.value + 300f, translateAnim.value + 300f)
    )
}

@Composable
fun FeaturedSongItem(song: Items, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { /* Обработка клика на избранную песню */ }
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                }
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.cover)
                    .dispatcher(Dispatchers.IO)
                    .memoryCacheKey(song.cover)
                    .diskCacheKey(song.cover)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(rememberShimmerBrush())
                    )
                },
                contentDescription = "Обложка песни",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            maxLines = 1
        )
        Text(
            text = song.artist_name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            maxLines = 1
        )
    }
}

@Composable
fun FeaturedSongItemShimmer(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .graphicsLayer {
                    shadowElevation = 8.dp.toPx()
                }
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(rememberShimmerBrush())
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(4.dp))
                .background(rememberShimmerBrush())
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(14.dp)
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(4.dp))
                .background(rememberShimmerBrush())
        )
    }
}
