package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.workoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items:ArrayList<ExerciseModel>):RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {
    
    class ViewHolder(binding:ItemExerciseStatusBinding):RecyclerView.ViewHolder(binding.root){
        val itemTextView = binding.itemTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model:ExerciseModel = items[position]
        holder.itemTextView.text = model.getId().toString()
        when{
            model.getIsSelected() -> {
                holder.itemTextView.background = ContextCompat.getDrawable(
                    holder.itemView.context, R.drawable.item_circular_thin_color_accent_border)
                holder.itemTextView.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCompleted() -> {
                holder.itemTextView.background = ContextCompat.getDrawable(
                    holder.itemView.context, R.drawable.item_circular_accent_background)
                holder.itemTextView.setTextColor(Color.parseColor("#ffffff"))
            }
            else -> {
                holder.itemTextView.background = ContextCompat.getDrawable(
                    holder.itemView.context, R.drawable.item_circular_color_gray_background)
                holder.itemTextView.setTextColor(Color.parseColor("#212121"))
            }
        }
        
    }
}