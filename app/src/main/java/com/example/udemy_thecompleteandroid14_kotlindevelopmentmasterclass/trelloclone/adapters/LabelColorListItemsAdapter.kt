package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R

class LabelColorListItemsAdapter(
    private val context:Context,
    private var list:ArrayList<String>,
    private val mSelectedColor:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
    var onItemClickListener:OnItemClickListener? = null    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trello_item_label_color, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MyViewHolder){
            holder.viewMain.setBackgroundColor(Color.parseColor(item))
            if(item == mSelectedColor){
                holder.ivSelectedColor.visibility = View.VISIBLE
            }else{
                holder.ivSelectedColor.visibility = View.GONE
            }
            holder.itemView.setOnClickListener{
                if(onItemClickListener != null){
                    onItemClickListener!!.onClick(position, item)
                }
            }
        }
    }
    
    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val viewMain:View = view.findViewById(R.id.view_main)
        val ivSelectedColor:ImageView = view.findViewById(R.id.iv_selected_color)
    }
    
    interface OnItemClickListener{
        fun onClick(position:Int, color:String)
    }

}