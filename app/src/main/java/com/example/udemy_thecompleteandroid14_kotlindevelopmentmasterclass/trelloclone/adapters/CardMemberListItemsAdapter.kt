package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.SelectedMembers

open class CardMemberListItemsAdapter(
    private val context:Context,
    private val list:ArrayList<SelectedMembers>,
    private val assignMembers:Boolean):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
    private var onClickListener:OnClickListener? = null    
        
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trello_item_card_selected_member, parent, false))
        
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            if(position == list.size - 1 && assignMembers){
                holder.ivAddMember.visibility = View.VISIBLE
                holder.ivSelectedMemberImage.visibility = View.GONE
            }else{
                holder.ivAddMember.visibility = View.GONE
                holder.ivSelectedMemberImage.visibility = View.VISIBLE
                
                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.ivSelectedMemberImage)
            }
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick()
                }
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    
    interface OnClickListener{
        fun onClick()
    }
    
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivAddMember:ImageView = view.findViewById(R.id.iv_add_member)
        val ivSelectedMemberImage:ImageView = view.findViewById(R.id.iv_selected_member_image)
    }
}