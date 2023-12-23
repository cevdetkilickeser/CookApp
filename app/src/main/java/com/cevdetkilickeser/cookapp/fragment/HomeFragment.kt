package com.cevdetkilickeser.cookapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.MainActivity
import com.cevdetkilickeser.cookapp.R
import com.cevdetkilickeser.cookapp.activity.CategoryMealsActivity
import com.cevdetkilickeser.cookapp.activity.MealActivity
import com.cevdetkilickeser.cookapp.adapter.CategoriesAdapter
import com.cevdetkilickeser.cookapp.adapter.PopularMealsAdapter
import com.cevdetkilickeser.cookapp.data.Categories
import com.cevdetkilickeser.cookapp.data.PopularMeals
import com.cevdetkilickeser.cookapp.data.Meal
import com.cevdetkilickeser.cookapp.databinding.FragmentHomeBinding
import com.cevdetkilickeser.cookapp.fragment.bottomsheet.MealBottomSheetFragment
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealsAdapter: PopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = (activity as MainActivity).viewModel

        popularMealsAdapter = PopularMealsAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        Log.e("şş","onCreateView çalıştı")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("şş","onViewCreated çalıştı")

        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()

        loadingCase()

        homeViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        homeViewModel.getPopularItems()
        observePopularItems()
        onPopularMealsClick()

        homeViewModel.getCategories()
        observeCategories()
        onCategoriesItemsClick()

        onPopularMealsLongClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.homeToSearch)
        }
    }

    private fun onPopularMealsLongClick() {
        popularMealsAdapter.onLongItemClick = {
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun observeRandomMeal(){
        homeViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner){
            responseCase()
            Glide.with(this@HomeFragment)
                .load(it.strMealThumb)
                .into(binding.ivRandom)

            randomMeal = it
        }
    }

    private fun onRandomMealClick() {
        binding.cardViewRandomMeal.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("idMeal",randomMeal.idMeal)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
            adapter = popularMealsAdapter
        }
    }

    private fun observePopularItems() {
        homeViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner){
            popularMealsAdapter.setMeals(mealsList = it as ArrayList<PopularMeals>)
        }
    }

    private fun onPopularMealsClick() {
        popularMealsAdapter.onItemClick = { meal ->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra("idMeal",meal.idMeal)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView(){
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategories() {
        homeViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner){
            categoriesAdapter.setItems(categoriesList = it as ArrayList<Categories>)
        }
    }

    private fun onCategoriesItemsClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra("nameCategory",category.strCategory)
            startActivity(intent)
        }
    }

    private fun loadingCase(){
        Log.e("şş","loadingCase çalıştı")

        binding.ivRandom.visibility = View.INVISIBLE
        binding.progressBarRandomMeal.visibility = View.VISIBLE
    }

    private fun responseCase(){
        Log.e("şş","responseCase çalıştı")

        binding.ivRandom.visibility = View.VISIBLE
        binding.progressBarRandomMeal.visibility = View.INVISIBLE
    }

}
