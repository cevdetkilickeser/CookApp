package com.cevdetkilickeser.cookapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.cookapp.data.Categories
import com.cevdetkilickeser.cookapp.data.CategoriesList
import com.cevdetkilickeser.cookapp.data.PopularMealsList
import com.cevdetkilickeser.cookapp.data.PopularMeals
import com.cevdetkilickeser.cookapp.data.Meal
import com.cevdetkilickeser.cookapp.data.MealList
import com.cevdetkilickeser.cookapp.db.MealDatabase
import com.cevdetkilickeser.cookapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel(){

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealsLiveData = MutableLiveData<List<PopularMeals>>()
    private var categoriesLiveData = MutableLiveData<List<Categories>>()
    private var favoriteMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var mealBottomSheetLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    fun getRandomMeal(){

        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("şş",t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<PopularMealsList>{
            override fun onResponse(call: Call<PopularMealsList>, response: Response<PopularMealsList>) {
                if (response.body() != null){
                    popularMealsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<PopularMealsList>, t: Throwable) {
                Log.e("şş", t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategoeies().enqueue(object : Callback<CategoriesList> {
            override fun onResponse(call: Call<CategoriesList>, response: Response<CategoriesList>) {
                if (response.body() != null){
                    val category = response.body()!!.categories
                    categoriesLiveData.value = category
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<CategoriesList>, t: Throwable) {
                Log.e("şş",t.message.toString())
            }
        })
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let {
                    mealBottomSheetLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("şş", t.message.toString())
            }

        })
    }

    fun insertMealToFav(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().insertMeal(meal)
        }
    }

    fun deleteMealFromFav(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun searchMeals(searchQuery:String) {
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("şş",t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData() : LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularItemsLiveData() : LiveData<List<PopularMeals>>{
        return popularMealsLiveData
    }

    fun observeCategoriesLiveData() : LiveData<List<Categories>>{
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData() : LiveData<List<Meal>>{
        return favoriteMealsLiveData
    }

    fun observeMealBottomSheetLiveData() : LiveData<Meal>{
        return mealBottomSheetLiveData
    }

    fun observeSearchedMealsLiveData() : LiveData<List<Meal>>{
        return searchedMealsLiveData
    }

}