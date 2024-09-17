package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import de.hdodenhof.circleimageview.CircleImageView

open class BoardItemAdapter(private val context:Context, private var list:ArrayList<Board>)
    :RecyclerView.Adapter<BoardItemAdapter.ViewHolder>(){
        
        private var onClickListener:OnClickListener? = null
        
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val boardImage:CircleImageView = view.findViewById(R.id.iv_board_image)
        val boardName:TextView = view.findViewById(R.id.tv_board_name)
        val createdBy:TextView = view.findViewById(R.id.tv_created_by)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if(holder is ViewHolder){
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.boardImage)
            holder.boardName.text = model.name
            holder.createdBy.text = model.createdBy
            
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    
    interface OnClickListener{
        fun onClick(position:Int, model:Board)
    } 
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
}