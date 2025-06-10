package com.example.useCase

import com.example.models.Request
import com.example.route.*

class FavoriteRepository: IFavoriteRepository {
    override fun getFavoriteByIdUser(userId: Int?): List<Favourites> {
        val sneakersById = favouritesList.filter { it.user_id == userId }
        return sneakersById
    }

    override fun deleteFavorite(userId: Int, shoeId: Int) :Boolean {
        try {
            favouritesList.remove(Favourites(sneakers_id = shoeId, user_id = userId))
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun addFavorite(userId: Int, shoeId: Int):Boolean {
        try {
            favouritesList.add(Favourites(sneakers_id = shoeId, user_id = userId))
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun findUserByID(userId: Int): User? {
        var user: User? = userList.firstOrNull() { it.userId == userId }
        return user
    }

    override fun findShoeById(shoeId: Int): Sneakers? {
       var shoe: Sneakers? = sneakersList.firstOrNull() { it.id == shoeId }
        return shoe
    }

    override fun convertIdFavorite(favoriteList: List<Favourites>): MutableList<Sneakers?> {
        val list = mutableListOf<Sneakers?>()
        favoriteList.forEach{
            val shTitle = findShoeById(it.sneakers_id)
            list.add(shTitle)
        }
        return list
    }

    override fun convertIdPopylar(favoriteList: List<Int>): MutableList<Sneakers?> {
        val list = mutableListOf<Sneakers?>()
        favoriteList.forEach{
            val shTitle = findShoeById(it)
            list.add(shTitle)
        }
        return list
    }
}