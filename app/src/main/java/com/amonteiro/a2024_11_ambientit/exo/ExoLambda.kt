package com.amonteiro.a2024_11_ambientit.exo


class MyLiveData<T>(value: T? = null) {

    var value: T? = value
        set(newValue) {
            field = newValue
            action?.invoke(newValue)
        }

    var action: ((T?) -> Unit)? = null
        set(newValue) {
            field = newValue
            action?.invoke(value)
        }

}


fun main() {

    var toto = MyLiveData("Coucou")

    toto.action = {
        println(it)
    }

    toto.value = "Hello"
}


fun exo1() {
    //Déclaration
    val lower: (String) -> Unit = { it: String -> println(it.lowercase()) }
    val lower2 = { it: String -> println(it.lowercase()) }
    val lower3: (String) -> Unit = { it -> println(it.lowercase()) }
    val lower4: (String) -> Unit = { println(it.lowercase()) }

    val max: (Int, Int) -> Int = { a, b -> Math.max(a, b) }

    //Appel
    lower("Coucou")
    val res = max(5, 6)
    println("res=$res")
}