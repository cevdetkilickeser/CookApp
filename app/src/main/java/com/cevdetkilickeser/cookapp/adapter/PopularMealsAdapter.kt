package com.cevdetkilickeser.cookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.data.PopularMeals
import com.cevdetkilickeser.cookapp.databinding.CardPopularMealBinding

class PopularMealsAdapter : RecyclerView.Adapter<PopularMealsAdapter.PopulerCardHolder>() {
    class PopulerCardHolder (val binding: CardPopularMealBinding) : RecyclerView.ViewHolder(binding.root){

    }
    private var mealsList = ArrayList<PopularMeals>()
    lateinit var onItemClick: ((PopularMeals) -> Unit)
    var onLongItemClick: ((PopularMeals) -> Unit)? = null

    fun setMeals(mealsList:ArrayList<PopularMeals>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardPopularMealBinding.inflate(layoutInflater,parent,false)
        return PopulerCardHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopulerCardHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.ivPopularItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener{
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }


}