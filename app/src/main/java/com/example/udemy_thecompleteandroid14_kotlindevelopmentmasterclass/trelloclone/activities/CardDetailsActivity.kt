package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityCardDetailsBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.CardMemberListItemsAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.dialogs.LabelColorListDialog
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.dialogs.MembersListDialog
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase.FireStoreClass
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Card
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.SelectedMembers
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Task
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CardDetailsActivity : BaseActivity() {
    
    private var mSelectedColor = ""
    private lateinit var binding:ActivityCardDetailsBinding
    private lateinit var mBoardDetails:Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private lateinit var mMembersDetailsLIst:ArrayList<User>
    private var mSelectedDueDateMilliSeconds:Long = 0
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        getIntentData()
        setupActionBar()
        
        binding.etNameCardDetails.setText(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
//        binding.etNameCardDetails.setSelection(binding.etNameCardDetails.text.toString())
        
        mSelectedColor = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor
        if(mSelectedColor.isNotEmpty()){
            setColor()
        }
        
        binding.btnUpdateCardDetails.setOnClickListener{
            if(binding.etNameCardDetails.text.toString().isNotEmpty()){
                updateCardDetails()
            }else{
                Toast.makeText(this, "Enter a card name.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvSelectLabelColor.setOnClickListener{
            labelColorsListDialog()
        }
        binding.tvSelectMembers.setOnClickListener{
            membersListDialog()
        }
        
        setupSelectedMembersList()
        
        mSelectedDueDateMilliSeconds = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].dueDate
        
        if(mSelectedDueDateMilliSeconds > 0){
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val selectedDate = simpleDateFormat.format(Date(mSelectedDueDateMilliSeconds))
            binding.tvSelectDueDate.text = selectedDate
        }
        
        binding.tvSelectDueDate.setOnClickListener{
            showDatePicker()
        }
    }
    
    private fun labelColorsListDialog(){
        val colorsList:ArrayList<String> = colorsList()
        val listDialog = object:LabelColorListDialog(this,
            colorsList,
            resources.getString(R.string.str_select_label_color, mSelectedColor)){
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }
        }
        listDialog.show()
                
    }

    private fun setupActionBar() {
        val toolBarCardDetailsActivity: Toolbar = findViewById(R.id.toolbar_card_details_activity)
        setSupportActionBar(toolBarCardDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        }

        toolBarCardDetailsActivity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    
    private fun setupSelectedMembersList(){
        val cardAssignedMemberList = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo
        val selectedMembersList:ArrayList<SelectedMembers> = ArrayList()
        
        for(i in mMembersDetailsLIst.indices){
            for(j in cardAssignedMemberList){
                if(mMembersDetailsLIst[i].id == j){
                    val selectedMember = SelectedMembers(
                        mMembersDetailsLIst[i].id,
                        mMembersDetailsLIst[i].image)
                    selectedMembersList.add(selectedMember)
                }
            }
        }
        if(selectedMembersList.size > 0 ){
            selectedMembersList.add(SelectedMembers("", ""))
            binding.tvSelectMembers.visibility = View.GONE
            binding.rvSelectedMembersList.visibility = View.VISIBLE
            
            binding.rvSelectedMembersList.layoutManager = GridLayoutManager(this, 6)
            val adapter = CardMemberListItemsAdapter(this, selectedMembersList, true)
            binding.rvSelectedMembersList.adapter = adapter
            adapter.setOnClickListener(object : CardMemberListItemsAdapter.OnClickListener{
                override fun onClick() {
                    membersListDialog()
                }
            })
        }else{
            binding.tvSelectMembers.visibility = View.VISIBLE
            binding.rvSelectedMembersList.visibility = View.GONE
        }
    }
    
    private fun membersListDialog(){
        var cardAssignedMembersList = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo
        if(cardAssignedMembersList.size > 0){
            for(i in mMembersDetailsLIst.indices){
                for(j in cardAssignedMembersList){
                    if(mMembersDetailsLIst[i].id == j){
                        mMembersDetailsLIst[i].selected = true
                    }
                }
            }
        }else{
            for(i in mMembersDetailsLIst.indices){
                mMembersDetailsLIst[i].selected = false
            }
        }
        val listDialog = object :MembersListDialog(this, mMembersDetailsLIst, 
            resources.getString(R.string.str_select_member)){
            override fun onItemSelected(user: User, action: String) {
                if(action == Constants.SELECT){
                    if(!mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.contains(user.id)){
                        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.add(user.id)
                    }
                }else{
                    mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.remove(user.id)

                    for(i in mMembersDetailsLIst.indices){
                        if(mMembersDetailsLIst[i].id == user.id){
                            mMembersDetailsLIst[i].selected = false
                        }
                    }
                }
                setupSelectedMembersList()
            }
        }
        listDialog.show()
    }
    
    private fun updateCardDetails(){
        val card = Card(binding.etNameCardDetails.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
            mSelectedColor,
            mSelectedDueDateMilliSeconds)
        
        val taskList:ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)
        
        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }
    
    private fun deleteCard(){
        val cardsList:ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards
        cardsList.removeAt(mCardPosition)
        val taskList:ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)
        taskList[mTaskListPosition].cards = cardsList
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }
    
    private fun alertDialogForDeleteCard(cardName:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(resources.getString(R.string.confirmation_message_to_delete_card, cardName))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)){dialog, _->
            dialog.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)){dialog, _->
            dialog.dismiss()
        }
        val alertDialog:AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    
    fun addUpdateTaskListSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
    
    private fun colorsList():ArrayList<String>{
        val colorList:ArrayList<String> = ArrayList()
        colorList.add("#43C86F")
        colorList.add("#0C90F1")
        colorList.add("#F72400")
        colorList.add("#7A8089")
        colorList.add("#D57C1D")
        colorList.add("#770000")
        colorList.add("#0022F8")
        return colorList
    }
    
    private fun setColor(){
        binding.tvSelectLabelColor.text = ""
        binding.tvSelectLabelColor.setBackgroundColor(Color.parseColor(mSelectedColor))
    }
    
    private fun getIntentData(){
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }
        if(intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
            
        }
        if(intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
        if(intent.hasExtra(Constants.BOARD_MEMBERS_LIST)){
            mMembersDetailsLIst = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!
            
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"

                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                val tvSelectDueDate:TextView = findViewById(R.id.tv_select_due_date)
                tvSelectDueDate.text = selectedDate
                
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

           
                val theDate = sdf.parse(selectedDate)
                
                mSelectedDueDateMilliSeconds = theDate!!.time
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.trello_menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_card->{
                alertDialogForDeleteCard(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
}