package com.example.route

import com.example.useCase.BasketRepository
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Basket(
    var sneakersId: Int,
    var userId: Int,
    var count: Int
)


var basketList = mutableListOf(
    Basket(
        userId = 1,
        sneakersId = 4,
        count = 3
    ),

    Basket(
        userId = 1,
        sneakersId = 5,
        count = 2
    )
)

fun Route.basketRoute() {
    val basketRepository = BasketRepository()
    get("/basket/{userId}") {
        try {
            val userId = call.parameters["userId"]?.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid ID")

            var itemInInt = basketRepository.getBasketByUserId(userId)
            var itemInString = basketRepository.convertIdBasket(itemInInt)

            call.respond(itemInString)
        } catch (e: Exception) {
            call.respond(e)
        }
    }

    post("/basket") {
        try {
            val loginRequest = call.receive<Basket>()

            basketRepository.addInBasket(loginRequest)

            call.respond(true)
        } catch (e: Exception) {
            call.respond(false)
        }
    }

    delete("/basket") {
        try {
            val loginRequest = call.receive<Basket>()

            basketRepository.deleteInBasket(loginRequest)

            call.respond(true)
        } catch (e: Exception) {
            call.respond(false)
        }

    }
}