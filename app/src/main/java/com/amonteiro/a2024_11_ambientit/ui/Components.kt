package com.amonteiro.a2024_11_ambientit.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amonteiro.a2024_11_ambientit.ui.theme._2024_11_ambientitTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, locale = "fr")
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    _2024_11_ambientitTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //Je mets 2 versions pour tester avec et sans message d'erreur
                MyError(errorMessage = "Avec message d'erreur")
                Text("Sans erreur : ")
                MyError(errorMessage = "")
                Text("----------")
            }
        }
    }
}

@Composable
fun MyError(modifier:Modifier = Modifier, errorMessage:String?) {
//permet d'afficher / masquer l'erreur avec une animation
    AnimatedVisibility(!errorMessage.isNullOrBlank()) {
        Text(
            text = errorMessage ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onError,
            modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.error).padding(4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title:String? = null,
    backStack : Boolean = false,
    onBackIconClick : ()->Unit = {},
    //Icône
    topBarActions: List<@Composable () -> Unit>? = null,
    //Icône, Texte, Action
    dropDownMenuItem: List<Triple<ImageVector, String, () -> Unit>>? = null
) {
    var openDropDownMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title ?: "") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),

        //Icône retour
        navigationIcon = {
            if (backStack) {
                IconButton(onClick = onBackIconClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        //Icône sur la barre
        actions = {
            topBarActions?.forEach { it() }

            //DropDownMenu
            if (dropDownMenuItem != null) {

                //Icône du menu
                IconButton(onClick = { openDropDownMenu = true }) {
                    Icon(Icons.Filled.Menu, contentDescription = null)
                }

                //menu caché qui doit s'ouvrir quand on clique sur l'icône
                DropdownMenu(
                    expanded = openDropDownMenu,
                    onDismissRequest = { openDropDownMenu = false }
                ) {
                    //les items sont constitués à partir de la liste
                    dropDownMenuItem.forEach {
                        DropdownMenuItem(text = {
                            Row {
                                Icon(it.first, contentDescription = "")
                                Text(it.second)
                            }
                        }, onClick = {
                            openDropDownMenu = false //ferme le menu
                            it.third() //action au clic
                        })
                    }
                }
            }
        }
    )
}