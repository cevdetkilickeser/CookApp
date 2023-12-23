package com.cevdetkilickeser.cookapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cevdetkilickeser.cookapp.MainActivity
import com.cevdetkilickeser.cookapp.activity.CategoryMealsActivity
import com.cevdetkilickeser.cookapp.adapter.CategoriesAdapter
import com.cevdetkilickeser.cookapp.data.Categories
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel
import com.cevdetkilickeser.cookapp.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesViewModel: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoriesAdapter = CategoriesAdapter()
        categoriesViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareCategoriesRecyclerView()
        observeCategoriesLiveData()
        onCategoriesClick()
    }

    private fun prepareCategoriesRecyclerView() {
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        categoriesViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner){
            categoriesAdapter.setItems(categoriesList = it as ArrayList<Categories>)
        }
    }

    private fun onCategoriesClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra("nameCategory",category.strCategory)
            startActivity(intent)
        }
    }

}