package com.example.useCase

interface IPopularRepository {
    fun getPopularByIdUser(userId: Int)
    fun deletePopular(userId: Int, shoeId: Int)
    fun addPopular(userId: Int, shoeId: Int)
}