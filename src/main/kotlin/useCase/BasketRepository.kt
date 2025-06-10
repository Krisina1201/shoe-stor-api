package com.example.useCase

import com.example.route.Basket
import com.example.route.Sneakers
import com.example.route.basketList

class BasketRepository: IBasketRepository {
    var favoriteRepository = FavoriteRepository()
    override fun getBasketByUserId(userId: Int): List<Basket> {
        var sneakersById = basketList.filter { it.userId == userId }
        return sneakersById
    }

    override fun addInBasket(basket: Basket): Boolean {
        try {
            var sneakersById = basketList.filter { it.userId == basket.userId }
                .filter { it.sneakersId == basket.sneakersId }

            if (sneakersById.isEmpty()) {
                basketList.add(basket)
            } else {
                val index = basketList.indexOfFirst { it.userId == basket.userId && it.sneakersId == basket.sneakersId }
                basketList[index].count += basket.count
            }
            return true
        } catch (e: Exception) {
            return false
        }

    }

    override fun convertIdBasket(shoeList: List<Basket>):MutableList<Sneakers?> {
        val list = mutableListOf<Sneakers?>()
        shoeList.forEach{
            val shTitle = favoriteRepository.findShoeById(it.sneakersId)
            list.add(shTitle)
        }
        return list
    }

    override fun deleteInBasket(basket: Basket): Boolean {
        try {
            val index = basketList.indexOfFirst {
                it.userId == basket.userId && it.sneakersId == basket.sneakersId
            }
            if (basketList[index].count == basket.count) {
                basketList.removeAt(index)
            } else if (basketList[index].count > basket.count) {
                basketList[index].count -= basket.count
            } else {
                return false
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }
}