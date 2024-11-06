package com.amonteiro.a2024_11_ambientit.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a2024_11_ambientit.model.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class PictureBean(val id: Int, val url: String, val title: String, val longText: String, var favorite: MutableState<Boolean> = mutableStateOf(false))

fun main() {
    val viewModel = MainViewModel()
    viewModel.loadWeathers("Paris")
    while (viewModel.runInProgress) {
        Thread.sleep(100)
    }

    viewModel.loadWeathers("")
    while (viewModel.runInProgress) {
        Thread.sleep(100)
    }
    //Affichage de la liste, qui doit être remplie
    println("List : ${viewModel.dataList}")
    println("ErrorMessage : ${viewModel.errorMessage}")
}

const val LONG_TEXT = """Le Lorem Ipsum est simplement du faux texte employé dans la composition
    et la mise en page avant impression. Le Lorem Ipsum est le faux texte standard
    de l'imprimerie depuis les années 1500"""

open class MainViewModel(var dispatcher : CoroutineDispatcher = Dispatchers.Default) : ViewModel() {
    var errorMessage by mutableStateOf("")
    var dataList by mutableStateOf(emptyList<PictureBean>())
    var runInProgress by mutableStateOf(false)

    val job = Job()

    open fun loadWeathers(cityName: String) {
        runInProgress = true
        errorMessage = ""

        viewModelScope.launch(dispatcher) {

            try {
                dataList = WeatherRepository.loadWeathers(cityName).map {
                    PictureBean(
                        id = it.id,
                        title = it.name,
                        url = it.weather.firstOrNull()?.icon ?: "",
                        longText = """  
                Il fait ${it.main.temp}° à ${it.name}(id=${it.id}) avec un vent de ${it.wind.speed} m/s
                -Description : ${it.weather.getOrNull(0)?.description}
                -Icon : ${it.weather.getOrNull(0)?.icon}
            """.trimIndent()
                    )
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                errorMessage = e.message ?: "Une erreur est survenue"
            }
            finally {
                runInProgress = false
            }
        }

    }
}

class MainViewModelPreview : MainViewModel() {

    init {//Création d'un jeu de donnée au démarrage
        loadFakeData()
    }

    fun loadFakeData() {
        dataList = listOf(
            PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT),
            PictureBean(2, "https://picsum.photos/201", "BCDE", LONG_TEXT),
            PictureBean(3, "https://picsum.photos/202", "CDEF", LONG_TEXT),
            PictureBean(4, "https://picsum.photos/203", "EFGH", LONG_TEXT)
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }
}

class MainViewModelTest : MainViewModel() {

    override fun loadWeathers(cityName: String) {

        if (cityName == "") {
            errorMessage = "Une erreur"
        }
        else {

            dataList = listOf(
                PictureBean(1, "https://picsum.photos/200", "ABCD", LONG_TEXT),
                PictureBean(2, "https://picsum.photos/201", "BCDE", LONG_TEXT),
                PictureBean(3, "https://picsum.photos/202", "CDEF", LONG_TEXT),
                PictureBean(4, "https://picsum.photos/203", "EFGH", LONG_TEXT)
            ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
        }
    }
}