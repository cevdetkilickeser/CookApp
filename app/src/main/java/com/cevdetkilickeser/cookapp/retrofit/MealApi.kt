package com.cevdetkilickeser.cookapp.retrofit

import com.cevdetkilickeser.cookapp.data.CategoriesList
import com.cevdetkilickeser.cookapp.data.PopularMealsList
import com.cevdetkilickeser.cookapp.data.MealList
import com.cevdetkilickeser.cookapp.data.MealsByCategoryList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id:String) : Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String) : Call<PopularMealsList>

    @GET("categories.php")
    fun getCategoeies() : Call<CategoriesList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) : Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery:String) : Call<MealList>
}