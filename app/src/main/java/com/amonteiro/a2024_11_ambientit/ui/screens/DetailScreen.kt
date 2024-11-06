package com.amonteiro.a2024_11_ambientit.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amonteiro.a2024_11_ambientit.R
import com.amonteiro.a2024_11_ambientit.ui.theme._2024_11_ambientitTheme
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModel
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModelPreview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    _2024_11_ambientitTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                idPicture = 1,
                mainViewModel = MainViewModelPreview(),
               )

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable //id du PictureBean à afficher
fun DetailScreen(modifier: Modifier = Modifier,
                 idPicture: Int,
                 mainViewModel : MainViewModel,
                 onBackClick: ()->Unit = {}
                 ){

    val pictureBean = mainViewModel.dataList.firstOrNull { it.id == idPicture }

    Column(
        modifier = modifier.padding(8.dp)
    )  {
        Text(
            text = pictureBean?.title ?: "Non trouvé",
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        GlideImage(
            model = pictureBean?.url ?: "",
            contentDescription = "une photo de chat",
            loading = placeholder(R.mipmap.ic_launcher_round),
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Text(
            text = pictureBean?.longText ?: "-",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))

        Button(
            onClick = onBackClick ,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = Modifier
                .padding(8.dp)
                .align(CenterHorizontally)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Retour")
        }
    }
}