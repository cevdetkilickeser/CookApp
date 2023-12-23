package com.cevdetkilickeser.cookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cevdetkilickeser.cookapp.data.Categories
import com.cevdetkilickeser.cookapp.databinding.CardCategoryItemBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesCardHolder>() {

    private var categoriesList = ArrayList<Categories>()
    lateinit var onItemClick: ((Categories) -> Unit)

    fun setItems (categoriesList:ArrayList<Categories>){
        this.categoriesList = categoriesList
        notifyDataSetChanged()
    }

    class CategoriesCardHolder(val binding: CardCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesCardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CardCategoryItemBinding.inflate(layoutInflater,parent,false)
        return CategoriesCardHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesCardHolder, position: Int) {
        val category = categoriesList.get(position)
        val b = holder.binding
        Glide.with(holder.itemView)
            .load(category.strCategoryThumb)
            .into(b.ivCategory)
        b.tvCategoryName.text = category.strCategory

        holder.itemView.setOnClickListener {
            onItemClick.invoke(category)
        }
    }
}