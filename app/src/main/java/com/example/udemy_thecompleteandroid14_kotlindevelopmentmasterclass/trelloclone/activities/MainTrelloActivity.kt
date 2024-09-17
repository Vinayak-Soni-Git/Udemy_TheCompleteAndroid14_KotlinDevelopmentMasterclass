package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMainTrelloBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.adapters.BoardItemAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase.FireStoreClass
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainTrelloActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding:ActivityMainTrelloBinding
    companion object{
        const val MY_PROFILE_REQUEST_CODE:Int = 11
        const val CREATE_BOARD_REQUEST_CODE:Int = 12
    }
    private lateinit var mUserName:String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTrelloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupActionBar()
        
        binding.navView.setNavigationItemSelectedListener(this)
        
        FireStoreClass().loadUserData(this, true)
        
        val fabCreateBoardButton:FloatingActionButton = findViewById(R.id.fab_create_board)
        fabCreateBoardButton.setOnClickListener{
            val intent = Intent(this, CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }
    
    
    fun populateBoardListToUI(boardList:ArrayList<Board>){
        val boardListRecyclerView:RecyclerView = findViewById(R.id.rv_boards_list)
        val noBoardsAvailableText:TextView = findViewById(R.id.tv_no_boards_available)
        hideProgressDialog()
        if(boardList.size > 0){
            boardListRecyclerView.visibility = View.VISIBLE
            noBoardsAvailableText.visibility = View.GONE
            boardListRecyclerView.layoutManager = LinearLayoutManager(this)
            boardListRecyclerView.setHasFixedSize(true)
            
            val adapter = BoardItemAdapter(this, boardList)
            boardListRecyclerView.adapter = adapter
            
            adapter.setOnClickListener(object:BoardItemAdapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainTrelloActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
        }else{
            boardListRecyclerView.visibility = View.GONE
            noBoardsAvailableText.visibility = View.VISIBLE 
        }
    }
    
    private fun setupActionBar(){
        val toolBarMainTrelloActivity:Toolbar = findViewById(R.id.toolbar_main_trello_activity)
        toolBarMainTrelloActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)
        toolBarMainTrelloActivity.setNavigationOnClickListener{
            toggleDrawer()
        }
    }
    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    
    fun updateNavigationUserDetails(user:User, readBoardsList:Boolean){
        mUserName = user.name
        val userImage:ImageView = findViewById(R.id.iv_user_image_nav)
        val userName:TextView = findViewById(R.id.tv_username_nav)
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(userImage)
        userName.text = user.name
        if(readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getBoardsList(this)
        }
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FireStoreClass().loadUserData(this)
        }else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FireStoreClass().getBoardsList(this)    
        }
        else{
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                startActivityForResult(Intent(this, ProfileActivity::class.java), MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        
        return true
    }
}