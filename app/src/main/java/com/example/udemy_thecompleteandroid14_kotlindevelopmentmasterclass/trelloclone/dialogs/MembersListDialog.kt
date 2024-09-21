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
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.MemberListItemAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User

abstract class MembersListDialog (
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
) : Dialog(context) {

    private var adapter: MemberListItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        val view = LayoutInflater.from(context).inflate(R.layout.trello_dialog_lists, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View) {
        val tvTitle:TextView = findViewById(R.id.tvTitleDialogList)
        tvTitle.text = title
        val rvList:RecyclerView = findViewById(R.id.rvColorDialogList)

        if (list.size > 0) {

            rvList.layoutManager = LinearLayoutManager(context)
            adapter = MemberListItemAdapter(context, list)
            rvList.adapter = adapter

            adapter!!.setOnClickListener(object :
                MemberListItemAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action:String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, action:String)

}