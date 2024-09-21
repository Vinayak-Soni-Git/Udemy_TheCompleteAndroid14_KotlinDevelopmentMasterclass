package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants

class MemberListItemAdapter(
    private val context:Context,
    private var list:ArrayList<User>
) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onClickListener:OnClickListener? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.trello_item_member, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide.with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.ivMemberImage)
            
            holder.tvMemberName.text = model.name
            holder.tvMemberEmail.text = model.email
            
            if(model.selected){
                holder.ivSelectedMember.visibility = View.VISIBLE
            }else{
                holder.ivSelectedMember.visibility = View.GONE
            }
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    if(model.selected){
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    }else{
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivMemberImage:ImageView = view.findViewById(R.id.iv_member_image)
        val tvMemberName:TextView = view.findViewById(R.id.tv_member_name)
        val tvMemberEmail:TextView = view.findViewById(R.id.tv_member_email)
        val ivSelectedMember:ImageView = view.findViewById(R.id.iv_selected_member)
    }
    
    interface OnClickListener{
        fun onClick(position:Int, user:User, action:String)
    }

}