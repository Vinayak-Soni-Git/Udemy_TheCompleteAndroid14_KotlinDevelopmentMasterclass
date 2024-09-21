package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMembersBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.MemberListItemAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase.FireStoreClass
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants

class MembersActivity : BaseActivity() {
    private lateinit var mBoardDetails:Board
    private lateinit var binding:ActivityMembersBinding
    private lateinit var mAssignedMembersList:ArrayList<User>
    private var anyChangesMade:Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }
        
        setupActionBar()
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }
    
    fun memberDetails(user: User){
        mBoardDetails.assignedTo.add(user.id)
        FireStoreClass().assignMemberToBoard(this, mBoardDetails, user)
    }
    
    private fun setupActionBar() {
        val toolBarMembersActivity: Toolbar = findViewById(R.id.toolbar_members_activity)
        setSupportActionBar(toolBarMembersActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        
        toolBarMembersActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    fun setupMembersList(list:ArrayList<User>){
        mAssignedMembersList = list
        hideProgressDialog()
        binding.rvMembersList.layoutManager = LinearLayoutManager(this)
        binding.rvMembersList.setHasFixedSize(true)
        val adapter = MemberListItemAdapter(this, list)
        binding.rvMembersList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trello_menu_add_member, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionAddMember->{
                dialogSearchMember()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    private fun dialogSearchMember(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.trello_dialog_search_member)
        val tvAdd:TextView = dialog.findViewById(R.id.tv_add_member)
        val tvCancel:TextView = dialog.findViewById(R.id.tv_cancel_member)
        val etEmailSearchMember:EditText = dialog.findViewById(R.id.et_email_search_member)
        tvAdd.setOnClickListener{
            val email = etEmailSearchMember.text.toString()
            if(email.isNotEmpty()){
                dialog.dismiss()
                showProgressDialog(resources.getString(R.string.please_wait))
                FireStoreClass().getMemberDetails(this, email)
            }else{
                Toast.makeText(this@MembersActivity, "Please enter members email address.", Toast.LENGTH_SHORT).show()
            }
        }
        tvCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        if(anyChangesMade){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
    
    fun memberAssignedSuccess(user:User){
        hideProgressDialog()
        mAssignedMembersList.add(user)
        anyChangesMade = true
        setupMembersList(mAssignedMembersList)
    }
}