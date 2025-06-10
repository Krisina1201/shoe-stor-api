package com.example.useCase

import com.example.route.Basket
import com.example.route.Sneakers

interface IBasketRepository {
    fun getBasketByUserId(userId: Int) : List<Basket>
    fun addInBasket(basket: Basket): Boolean
    fun convertIdBasket(shoeList: List<Basket>) :MutableList<Sneakers?>
    fun deleteInBasket(basket: Basket): Boolean
}