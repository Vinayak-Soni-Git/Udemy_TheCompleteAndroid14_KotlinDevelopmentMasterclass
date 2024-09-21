package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Card

class CardListItemAdapter(
    private val context:Context,
    private var list:ArrayList<Card>
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private var onClickListener:OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trello_item_card, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            
            if(model.labelColor.isNotEmpty()){
                holder.viewLabelColor.visibility = View.VISIBLE
                holder.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.viewLabelColor.visibility = View.GONE
            }
            
            holder.tvCardName.text = model.name
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position)
                }
            }
        }
    }
    
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    interface OnClickListener{
        fun onClick(position:Int)
    }
    
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvCardName:TextView = view.findViewById(R.id.tv_card_name)
        val viewLabelColor:View = view.findViewById(R.id.view_label_color)
    }

}