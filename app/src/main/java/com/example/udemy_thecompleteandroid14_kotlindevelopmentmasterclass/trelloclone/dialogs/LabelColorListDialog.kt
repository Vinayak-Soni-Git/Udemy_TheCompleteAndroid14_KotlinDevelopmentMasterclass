package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.LabelColorListItemsAdapter

abstract class LabelColorListDialog(context:Context,
                                    private var list:ArrayList<String>,
                                    private var mSelectedColor:String = "",
                                    private val title:String = "") :Dialog(context){
    private var adapter:LabelColorListItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(context).inflate(R.layout.trello_dialog_lists, null)
        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setupRecyclerView(view)
    }
    
    private fun setupRecyclerView(view:View){
        val tvTitle:TextView = findViewById(R.id.tvTitleDialogList)
        tvTitle.text = title
        val rvColorDialogList:RecyclerView = findViewById(R.id.rvColorDialogList)
        rvColorDialogList.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListItemsAdapter(context, list, mSelectedColor)
        rvColorDialogList.adapter = adapter
        adapter!!.onItemClickListener = object:LabelColorListItemsAdapter.OnItemClickListener{
            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }
        }
    }
    protected abstract fun onItemSelected(color:String)
}