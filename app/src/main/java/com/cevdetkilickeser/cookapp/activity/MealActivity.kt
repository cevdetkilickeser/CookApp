package com.cevdetkilickeser.cookapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.data.Meal
import com.cevdetkilickeser.cookapp.db.MealDatabase
import com.cevdetkilickeser.cookapp.viewmodel.MealViewModel
import com.cevdetkilickeser.cookapp.viewmodel.MealViewModelFactory
import com.cevdetkilickeser.cookapp.databinding.ActivityMealBinding

@Suppress("DEPRECATION")
class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var idMeal : String
    private lateinit var ytLink: String
    private lateinit var mealViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelFactory = MealViewModelFactory(mealDatabase)
        mealViewModel = ViewModelProvider(this,mealViewModelFactory)[MealViewModel::class.java]

        /*val tempViewModel : MealViewModel by viewModels()
        mealViewModel = tempViewModel*/

        getMealInfo()

        loadingCase()

        mealViewModel.getMealDetails(idMeal)
        observeMealDetailsLiveData()

        youtubeClick()
        onFavoriteClick()
    }
    private var favMeal:Meal? = null
    private fun observeMealDetailsLiveData() {
        mealViewModel.observeMealDetailsLivedata().observe(this, object : Observer<Meal>{
            override fun onChanged(meal: Meal) {
                responseCase()
                favMeal = meal
                Glide.with(applicationContext)
                    .load(meal.strMealThumb)
                    .into(binding.ivMealDetail)

                binding.collapsingToolbar.title = meal.strMeal
                binding.tvCategory.text = "Category: ${meal.strCategory}"
                binding.tvLocation.text = "Location: ${meal.strArea}"
                binding.tvInstructionDetail.text = meal.strInstructions
                ytLink = meal.strYoutube.toString()
            }

        })
    }

    private fun getMealInfo(){
        val intent = intent
        if (intent != null && intent.hasExtra("idMeal")) {
            this.idMeal = intent.getStringExtra("idMeal").toString()
        }
    }

    private fun youtubeClick(){
        binding.ivYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
    }

    private fun onFavoriteClick() {
        binding.fab.setOnClickListener {
            favMeal?.let {
                mealViewModel.insertMealToFav(it)
            }
        }
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fab.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvLocation.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvInstructionDetail.visibility = View.INVISIBLE
        binding.ivYoutube.visibility = View.INVISIBLE
    }

    private fun responseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.fab.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvLocation.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvInstructionDetail.visibility = View.VISIBLE
        binding.ivYoutube.visibility = View.VISIBLE
    }
}