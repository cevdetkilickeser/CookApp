package com.cevdetkilickeser.cookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.data.MealsByCategory
import com.cevdetkilickeser.cookapp.databinding.CardCategoryMealsBinding

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsCardHolder>(){

    lateinit var onItemClick: ((MealsByCategory) -> Unit)

    class CategoryMealsCardHolder(val binding: CardCategoryMealsBinding) : RecyclerView.ViewHolder(binding.root){

    }

    private var mealsList = ArrayList<MealsByCategory>()

    fun setMealsList(mealsList: List<MealsByCategory>){
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardCategoryMealsBinding.inflate(layoutInflater,parent,false)
        return CategoryMealsCardHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsCardHolder, position: Int) {
        val meal = mealsList.get(position)
        val b = holder.binding

        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(b.ivCategoryMeal)

        b.tvCategoryMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }
}