package com.cevdetkilickeser.cookapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.cevdetkilickeser.cookapp.adapter.CategoryMealsAdapter
import com.cevdetkilickeser.cookapp.databinding.ActivityCategoryMealsBinding
import com.cevdetkilickeser.cookapp.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tempViewModel : CategoryMealsViewModel by viewModels()
        categoryMealsViewModel = tempViewModel

        categoryMealsAdapter = CategoryMealsAdapter()

        val categoryName = intent.getStringExtra("nameCategory").toString()

        prepareCategoryMealsRecyclerView()

        categoryMealsViewModel.getMealsByCategory(categoryName)
        observeMealsByCategoryLiveData()
        onMealsByCategoryClick()
    }

    private fun prepareCategoryMealsRecyclerView() {
        binding.rvCategoryMeals.apply {
            layoutManager = GridLayoutManager(this@CategoryMealsActivity,2, GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }

    private fun observeMealsByCategoryLiveData(){
        categoryMealsViewModel.observeMealsByCategooryLiveData().observe(this, Observer {
            categoryMealsAdapter.setMealsList(it)
            binding.tvCategoryName.text = intent.getStringExtra("nameCategory").toString() + " : " + it.size.toString()
        })
    }

    private fun onMealsByCategoryClick(){
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(applicationContext,MealActivity::class.java)
            intent.putExtra("idMeal",meal.idMeal)
            startActivity(intent)
        }
    }
}