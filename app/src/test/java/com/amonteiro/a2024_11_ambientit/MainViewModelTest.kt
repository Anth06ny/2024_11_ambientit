package com.amonteiro.a2024_11_ambientit

import com.amonteiro.a2024_11_ambientit.model.DescriptionBean
import com.amonteiro.a2024_11_ambientit.model.TempBean
import com.amonteiro.a2024_11_ambientit.model.WeatherBean
import com.amonteiro.a2024_11_ambientit.model.WeatherRepository
import com.amonteiro.a2024_11_ambientit.model.WindBean
import com.amonteiro.a2024_11_ambientit.viewmodel.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainViewModelTest {

    //Dispatcher pour les Coroutines, pilotables à  l'aide de advanceUntilIdle()
    private val testDispatcher = StandardTestDispatcher()
    private val viewModel: MainViewModel = MainViewModel(testDispatcher)


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadWeather() = runTest(testDispatcher) {

        // Vérifier l'état avant le lancement de la coroutine
        assertFalse(viewModel.runInProgress)

        //On mock WeatherRepository pour déclencher le résultat voulu
        mockkObject(WeatherRepository)
        every { WeatherRepository.loadWeathers("Paris") }.returns(getParisFakeResult())

        // Appeler la méthode à tester
        viewModel.loadWeathers("Paris")

        // Vérifier que runInProgress est true
        assertTrue(viewModel.runInProgress)

        // Avancer l'exécution des coroutines jusqu'à l'état courant
        advanceUntilIdle()

        // Vérifier que runInProgress est false
        assertFalse(viewModel.runInProgress)

        //On vérifie que loadWeathers("Paris") à bien été appelé
        verify { WeatherRepository.loadWeathers("Paris") }

        //On vérifie qu'aucun autre appel à WeatherRepository à été effectué
        confirmVerified(WeatherRepository)

        // Qu'on a des éléments dans la liste
        assertFalse(viewModel.dataList.isEmpty())

        //Que le 1 er élément c'est bien Paris et le même id
        assertEquals("La ville n'est pas Paris", getParisFakeResult().first().name, viewModel.dataList.first().title)
        assertEquals("L'id n'est pas identique", getParisFakeResult().first().id, viewModel.dataList.first().id)

    }

    fun getParisFakeResult() = arrayListOf(
        WeatherBean(
            id = 1,
            name = "Paris",
            main = TempBean(temp = 20.0),
            wind = WindBean(speed = 5.0),
            weather = listOf(DescriptionBean(description = "Ensoleillé", icon = "01d"))
        )
    )
}