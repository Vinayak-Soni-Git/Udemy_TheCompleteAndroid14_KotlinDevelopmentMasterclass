package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.roomdemo

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.EmployeeItemsRowBinding

class EmployeeAdapter(
    private val items:ArrayList<EmployeeEntity>,
    private val updateListener:(id:Int)->Unit,
    private val deleteListener:(id:Int)->Unit
):RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {
    
    class ViewHolder(binding:EmployeeItemsRowBinding):RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EmployeeItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        if(position %2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.lightGrey))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
        holder.ivEdit.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener{
            deleteListener.invoke(item.id)
        }
    }
    
}