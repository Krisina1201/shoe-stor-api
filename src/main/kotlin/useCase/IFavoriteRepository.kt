package com.example.useCase

import com.example.models.Request
import com.example.route.Favourites
import com.example.route.Sneakers
import com.example.route.User

interface IFavoriteRepository {
    fun getFavoriteByIdUser(userId: Int?) :List<Favourites>
    fun deleteFavorite(userId: Int, shoeId: Int): Boolean
    fun addFavorite(userId: Int, shoeId: Int): Boolean
    fun findUserByID(userId: Int): User?
    fun findShoeById(shoeId: Int): Sneakers?
    fun convertIdFavorite(favoriteList: List<Favourites>): MutableList<Sneakers?>
    fun convertIdPopylar(favoriteList: List<Int>): MutableList<Sneakers?>
}