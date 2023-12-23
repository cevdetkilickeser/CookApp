package com.cevdetkilickeser.cookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.data.Meal
import com.cevdetkilickeser.cookapp.databinding.CardFavoritesBinding

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.FavoritesCardHolder>() {

    lateinit var onItemClick: ((Meal) -> Unit)

    class FavoritesCardHolder(val binding: CardFavoritesBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardFavoritesBinding.inflate(layoutInflater,parent,false)
        return FavoritesCardHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoritesCardHolder, position: Int) {
        val meal = differ.currentList[position]
        val b = holder.binding
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(b.ivFavoriteMeal)

        b.tvFavoriteMealName.text = meal.strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }
}