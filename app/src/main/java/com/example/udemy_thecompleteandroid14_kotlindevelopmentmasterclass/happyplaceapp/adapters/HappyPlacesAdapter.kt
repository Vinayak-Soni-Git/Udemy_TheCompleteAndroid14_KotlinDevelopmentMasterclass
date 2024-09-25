package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities.AddHappyPlaceActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities.MainHappyPlaceActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.database.DatabaseHandler
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.models.HappyPlaceModel

class HappyPlacesAdapter(
    private val context:Context,
    private var list:ArrayList<HappyPlaceModel>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        
    private var onClickListener:OnClickListener? = null
    
    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val ivPlaceImage:ImageView = view.findViewById(R.id.iv_place_image)
        val tvHappyPlaceTitle:TextView = view.findViewById(R.id.tvHappyPlaceTitle)
        val tvHappyPlaceDesc:TextView = view.findViewById(R.id.tvHappyPlaceDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_happy_place, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            holder.ivPlaceImage.setImageURI(Uri.parse(model.image))
            holder.tvHappyPlaceTitle.text = model.title
            holder.tvHappyPlaceDesc.text = model.description
            
            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }
    
    fun removeAt(position:Int){
        val dbHandler = DatabaseHandler(context)
        val idDeleted = dbHandler.deleteHappyPlace(list[position])
        if(idDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    
    fun notifyEditItem(activity:Activity, position:Int, requestCode:Int){
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }
    
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    
    interface OnClickListener{
        fun onClick(position:Int, model:HappyPlaceModel)
    }
}