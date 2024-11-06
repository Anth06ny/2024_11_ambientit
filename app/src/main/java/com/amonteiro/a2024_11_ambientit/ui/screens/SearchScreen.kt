package com.amonteiro.a2024_11_ambientit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amonteiro.a2024_11_ambientit.R
import com.amonteiro.a2024_11_ambientit.ui.MyError
import com.amonteiro.a2024_11_ambientit.ui.MyTopBar
import com.amonteiro.a2024_11_ambientit.ui.theme._2024_11_ambientitTheme
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModel
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModelPreview
import com.amonteiro.a2024_11_ambientit.viewmodel.PictureBean
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, locale = "fr")
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _2024_11_ambientitTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val mainViewModel = MainViewModelPreview()
            mainViewModel.runInProgress = true
            mainViewModel.errorMessage = "Coucou"
            SearchScreen(
                modifier = Modifier.padding(innerPadding),
                mainViewModel = mainViewModel
            )
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    backStack :Boolean = false,
    onBackIconClick : ()->Unit = {},
    onPictureRowItemClick : (PictureBean)->Unit = {}
    ) {

    Column(modifier = modifier,  horizontalAlignment = Alignment.CenterHorizontally) {

        MyTopBar(
            title = "Recherche",
            backStack =backStack,
            onBackIconClick = onBackIconClick,
            topBarActions = listOf {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Favorite, contentDescription = "Clear")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Clear")
                }
            }
        )

        var searchText by rememberSaveable { mutableStateOf("") }

        SearchBar(text = searchText) {
            searchText = it

            if(searchText.length >= 3) {
                mainViewModel.loadWeathers(searchText)
            }

        }

        MyError(errorMessage = mainViewModel.errorMessage)

        AnimatedVisibility(visible = mainViewModel.runInProgress){
            CircularProgressIndicator()
        }

        Spacer(Modifier.size(ButtonDefaults.IconSpacing))

        LazyColumn(modifier = Modifier.weight(10f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val filterList = mainViewModel.dataList //.filter { it.title.contains(searchText, true) }

            items(filterList.size) {
                PictureRowItem(
                    data = filterList[it],
                    modifier = Modifier                        .fillMaxWidth(),
                    onPictureClick = {
                        onPictureRowItemClick(filterList[it])
                    }
                )
            }
        }

        Row {

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { searchText = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(3f)
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.clear_filter))
            }

            Button(
                onClick = { mainViewModel.loadWeathers(searchText) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.weight(3f)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.bt_load))
            }

            Spacer(modifier = Modifier.weight(1f))

        }

    }

}

@Composable
fun SearchBar(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit) {

    TextField(
        value = text, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icone
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { Text("Enter text") }, //Texte d'aide qui se déplace
        placeholder = { //Texte d'aide qui disparait
            //Pour aller le chercher dans string.xml
            //Text(stringResource(R.string.placeholder_search))
            //En dur
            Text("Recherche")
        },
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable //Composable affichant 1 PictureBean
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean, onPictureClick : ()->Unit = {}) {

    var fullText by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .then(modifier)
    ) {

        //Permission Internet nécessaire
        GlideImage(
            model = data.url,
            //Pour aller le chercher dans string.xml
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            // Image d'attente. Permet également de voir l'emplacement de l'image dans la Preview
            loading = placeholder(R.mipmap.ic_launcher_round),
            // Image d'échec de chargement
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            //même autres champs qu'une Image classique
            modifier = Modifier
                .heightIn(max = 100.dp) //Sans hauteur il prendra tous l'écran
                .widthIn(max = 100.dp)
                .clickable(onClick = onPictureClick)
        )

        Column(modifier = Modifier
            .padding(5.dp)
            .clickable { fullText = !fullText }) {
            Text(text = data.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = if (fullText) data.longText else (data.longText.take(20) + "..."),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.animateContentSize()

            )
        }

    }
}