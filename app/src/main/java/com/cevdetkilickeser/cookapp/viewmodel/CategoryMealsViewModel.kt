package com.cevdetkilickeser.cookapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cevdetkilickeser.cookapp.data.MealsByCategory
import com.cevdetkilickeser.cookapp.data.MealsByCategoryList
import com.cevdetkilickeser.cookapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {

    private val mealsByCategoryLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName:String) {
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {
                    mealsByCategoryLiveData.postValue(it.meals)
                    }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("şş", t.message.toString())
            }

        })
    }

    fun observeMealsByCategooryLiveData() : MutableLiveData<List<MealsByCategory>>{
        return mealsByCategoryLiveData
    }
}