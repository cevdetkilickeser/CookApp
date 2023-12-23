package com.cevdetkilickeser.cookapp.fragment.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.MainActivity
import com.cevdetkilickeser.cookapp.activity.MealActivity
import com.cevdetkilickeser.cookapp.databinding.FragmentMealBottomSheetBinding
import com.cevdetkilickeser.cookapp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var mealBottomSheetViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        mealBottomSheetViewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            mealBottomSheetViewModel.getMealById(it)
        }

        observeMealBottomSheetLiveData()
        bottomSheetDialogClick()
    }

    private fun observeMealBottomSheetLiveData() {
        mealBottomSheetViewModel.observeMealBottomSheetLiveData().observe(viewLifecycleOwner){
            Glide.with(this)
                .load(it.strMealThumb)
                .into(binding.ivBottomSheet)

            binding.tvBottomSheetCategory.text = it.strCategory
            binding.tvBottomSheetMealName.text = it.strMeal
            binding.tvBottomSheetLocation.text = it.strArea
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }

    private fun bottomSheetDialogClick(){
        binding.bottomSheet.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("idMeal",mealId)
            startActivity(intent)
        }
    }

}