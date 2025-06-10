package com.example.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Sneakers(
    val id: Int,
    var productName: String,
    var cost: String,
    var count: Int,
    var photo: String,
    var description: String,
    var category: String
)

val sneakersList = mutableListOf(
    Sneakers(
        id = 1,
        productName = "abibas",
        cost = "10.000.000",
        count = 1,
        photo = "без фото",
        description = "Единственные и уникальные в совем роде мега модные и классные кросовки, известной комапнии",
        category = "Outdoor"
    ),
    Sneakers(
        id = 2,
        productName = "nikee",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Tennis"
    ),

    Sneakers(
        id = 3,
        productName = "pyyma",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Outdoor"
    ),
    Sneakers(
        id = 4,
        productName = "nike forse",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Outdoor"
    ),
    Sneakers(
        id = 5,
        productName = "danila crazy",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Tennis"
    ),
    Sneakers(
        id = 6,
        productName = "ivan dyrak",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Tennis"
    ),

    Sneakers(
        id = 7,
        productName = "blablabla",
        cost = "1000",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Tennis"
    ),

    Sneakers(
        id = 8,
        productName = "pypypypy",
        cost = "1",
        count = 300,
        photo = "без фото",
        description = "Китайские скороходы",
        category = "Tennis"
    ),
)

fun Route.sneakersRoute() {
    get("/allSneakers") {
        call.respond(sneakersList)
    }
}