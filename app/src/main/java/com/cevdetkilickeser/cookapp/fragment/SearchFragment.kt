package com.cevdetkilickeser.cookapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cevdetkilickeser.cookapp.MainActivity
import com.cevdetkilickeser.cookapp.activity.MealActivity
import com.cevdetkilickeser.cookapp.adapter.FavoritesAdapter
import com.cevdetkilickeser.cookapp.databinding.FragmentSearchBinding
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: HomeViewModel
    private lateinit var searchAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchAdapter = FavoritesAdapter()
        searchViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareSearchRecyclerView()

        binding.ivSearchOk.setOnClickListener { searchMeals() }

        observeSearchedMealsLiveData()

        onSearchedMealClick()

        var searchJob: Job? = null
        binding.etSearchbox.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch{
                delay(500)
                searchViewModel.searchMeals(it.toString())
            }
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.etSearchbox.text.toString()
        if (searchQuery.isNotEmpty()){
            searchViewModel.searchMeals(searchQuery)
        }
    }

    private fun onSearchedMealClick() {
        searchAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("idMeal",meal.idMeal)
            startActivity(intent)
        }
    }

    private fun prepareSearchRecyclerView() {
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(activity,2, GridLayoutManager.VERTICAL,false)
            adapter = searchAdapter
        }
    }

    private fun observeSearchedMealsLiveData() {
        searchViewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner){
            searchAdapter.differ.submitList(it)
        }
    }
}