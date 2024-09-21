package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityTaskListBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.TaskListItemsAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase.FireStoreClass
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Card
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Task
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants

class TaskListActivity : BaseActivity() {
    private lateinit var mBoardDetails:Board
    private lateinit var binding:ActivityTaskListBinding
    private lateinit var mBoardDocumentId:String
    lateinit var mAssignedMemberDetailList:ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardDetails(this, mBoardDocumentId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MEMBERS_REQUEST_CODE || requestCode == CARD_DETAILS_REQUEST_CODE){
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getBoardDetails(this, mBoardDocumentId)
        }else{
            Log.e("Cancelled", "Cancelled")
        }
    }
    fun cardDetails(taskListPosition:Int, cardPosition:Int){
        val openCardDetailsActivity = Intent(this, CardDetailsActivity::class.java)
        openCardDetailsActivity.putExtra(Constants.BOARD_DETAILS, mBoardDetails)
        openCardDetailsActivity.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        openCardDetailsActivity.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
        openCardDetailsActivity.putExtra(Constants.BOARD_MEMBERS_LIST, mAssignedMemberDetailList)
        startActivityForResult(openCardDetailsActivity, CARD_DETAILS_REQUEST_CODE)
    }

    private fun setupActionBar() {
        val toolBarTaskListActivity:Toolbar = findViewById(R.id.toolbar_task_list_activity)
        setSupportActionBar(toolBarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }
        toolBarTaskListActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    
    fun boardDetails(board:Board){
        mBoardDetails = board
        hideProgressDialog()
        setupActionBar()
        
        
        
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
        
    }
    fun addUpdateTaskListSuccess(){
        hideProgressDialog()
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardDetails(this,  mBoardDetails.documentId)
    }
    
    fun createTaskList(taskListName:String){
        val task = Task(taskListName, FireStoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    fun updateTaskList(position:Int, listName:String, model:Task){
        val task = Task(listName, model.createdBy)
        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    fun deleteTaskList(position:Int){
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    fun addCardToTaskList(position:Int, cardName:String){
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        val cardAssignedUsersList:ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FireStoreClass().getCurrentUserId())
        
        val card = Card(cardName, FireStoreClass().getCurrentUserId(), cardAssignedUsersList)
        
        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)
        
        val task = Task(mBoardDetails.taskList[position].title, 
                        mBoardDetails.taskList[position].createdBy,
                        cardsList
        )
        mBoardDetails.taskList[position] = task
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trello_menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.actionMembers->{
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAILS, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    fun boardMembersDetailsList(list:ArrayList<User>){
        mAssignedMemberDetailList = list
        hideProgressDialog()

        val addTaskList = Task(resources.getString(R.string.add_list))
        mBoardDetails.taskList.add(addTaskList)
        binding.rvTaskList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL , false)
        binding.rvTaskList.setHasFixedSize(true)

        val adapter = TaskListItemsAdapter(this, mBoardDetails.taskList)
        binding.rvTaskList.adapter = adapter
    }
    
    fun updateCardsInTaskList(taskListPosition:Int, cards:ArrayList<Card>){
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
        mBoardDetails.taskList[taskListPosition].cards = cards
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }
    
    companion object{
        const val MEMBERS_REQUEST_CODE:Int = 13
        const val CARD_DETAILS_REQUEST_CODE:Int = 14
    }
}