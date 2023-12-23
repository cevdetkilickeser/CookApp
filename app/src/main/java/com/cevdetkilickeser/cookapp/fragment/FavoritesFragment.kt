package com.cevdetkilickeser.cookapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cevdetkilickeser.cookapp.MainActivity
import com.cevdetkilickeser.cookapp.activity.MealActivity
import com.cevdetkilickeser.cookapp.adapter.FavoritesAdapter
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel
import com.cevdetkilickeser.cookapp.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
   private lateinit var binding: FragmentFavoritesBinding
   private lateinit var favoritesViewModel: HomeViewModel
   private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoritesAdapter = FavoritesAdapter()
        favoritesViewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareFavoriteMealsRecyclerView()
        observeFavoriteMealsLiveData()
        onFavoriteMealClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMeal = favoritesAdapter.differ.currentList[position]
                favoritesViewModel.deleteMealFromFav(deletedMeal)

                Snackbar.make(requireView(),"Meal deleted from favorites",Snackbar.LENGTH_LONG)
                    .setAction("UNDO",
                        View.OnClickListener {
                            favoritesViewModel.insertMealToFav(deletedMeal)
                        }
                    ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)

    }


    private fun prepareFavoriteMealsRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
            adapter = favoritesAdapter
        }
    }

    private fun observeFavoriteMealsLiveData() {
        favoritesViewModel.observeFavoriteMealsLiveData().observe(viewLifecycleOwner){
            favoritesAdapter.differ.submitList(it)
        }
    }

    private fun onFavoriteMealClick() {
        favoritesAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("idMeal",meal.idMeal)
            startActivity(intent)
        }
    }
}