package com.amonteiro.a2024_11_ambientit.model

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStreamReader


fun main() {

    val list = WeatherRepository.loadWeathers("Nice")
    for (w in list) {
        println("""
            Il fait ${w.main.temp}° à ${w.name}(id=${w.id}) avec un vent de ${w.wind.speed} m/s
            -Description : ${w.weather.getOrNull(0)?.description}
            -Icon : ${w.weather.getOrNull(0)?.icon}
        """.trimIndent())
    }
}

object WeatherRepository {

    //Attribut instancié 1 seule fois car c'est un singleton
    //Et uniquement à la 1er utilisation (Lazy Loading)
    private val client = OkHttpClient()
    private val gson = Gson()

    const val URL_API = "https://api.openweathermap.org/data/2.5/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    fun loadWeathers(city: String): List<WeatherBean> {
        val json = sendGet(URL_API + city)
        val list =  gson.fromJson(json, WeatherAroundBean::class.java).list

        list.forEach {
            it.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }

        return list
    }


    fun sendGet(url: String): String {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        return client.newCall(request).execute().use {
            //Analyse du code retour
            if (!it.isSuccessful) {
                throw Exception("Réponse du serveur incorrect :${it.code}\n${it.body.string()}")
            }
            //Résultat de la requête
            it.body.string()
        }
    }

    //Optimiser car il récupère un flux et non un String qu'il transmet directement à Gson
    //On a donc un seul parcourt du JSON et un seul stockage mémoire vu qu'il ne passe
    //pas par un String intermédiaire
    //use permet de fermer la réponse qu'il y ait ou non une exception
    fun loadWeatherOpti(city: String) = sendGetOpti(URL_API + city).use { //it:Response
        var isr = InputStreamReader(it.body.byteStream())
        val list = gson.fromJson(isr, WeatherAroundBean::class.java).list
        list.onEach {

        }
    }

    fun sendGetOpti(url: String): Response {
        println("url : $url")
        //Création de la requête
        val request = Request.Builder().url(url).build()
        //Execution de la requête
        val response = client.newCall(request).execute()
        //Analyse du code retour
        return if (!response.isSuccessful) {
            //On ferme la réponse qui n'est plus fermé par le use
            response.close()
            throw Exception("Réponse du serveur incorrect : ${response.code}")
        } else {
            response
        }
    }
}

//Objet de base retourné par l'API
data class WeatherAroundBean(var list: List<WeatherBean>)

//Ici je n'ai mis que ce qui est utile pour l'affichage demandé mais on peut tout mettre
data class WeatherBean(
    var id: Int,
    var name: String,
    var main: TempBean,
    var wind: WindBean,
    var weather: List<DescriptionBean>
)

data class TempBean(var temp: Double)
data class WindBean(var speed: Double)
data class DescriptionBean(var description: String, var icon: String)