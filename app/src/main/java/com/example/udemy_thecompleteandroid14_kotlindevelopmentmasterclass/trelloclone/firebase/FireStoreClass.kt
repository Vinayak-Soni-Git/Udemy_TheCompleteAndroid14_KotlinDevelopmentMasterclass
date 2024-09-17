package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.CreateBoardActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.MainTrelloActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.ProfileActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.SignInActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.SignUpActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.TaskListActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.Board
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.models.User
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    
    fun registerUser(activity:SignUpActivity, userInfo: User){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener { 
                activity.userRegisteredSuccess()
            }.addOnFailureListener{e->
                Log.e(activity.javaClass.simpleName, "Error: $e")
            }
    }
    
    fun getBoardsList(activity:MainTrelloActivity){
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { 
                document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val boardList:ArrayList<Board> = ArrayList()
                for (i in document.documents){
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }
                activity.populateBoardListToUI(boardList = boardList)
            }.addOnFailureListener{exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating board.", exception)
            }
        
    }
    fun getBoardDetails(activity:TaskListActivity, documentId:String){
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)
            }.addOnFailureListener{exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating board.", exception)
            }
    }
    
    fun createBoard(activity:CreateBoardActivity, board:Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener { 
                Toast.makeText(activity, "Board created successfully", Toast.LENGTH_SHORT).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener{exception->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating the board", exception)
            }
    }
    fun addUpdateTaskList(activity:TaskListActivity, board:Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener { 
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")
                activity.addUpdateTaskListSuccess()
            }.addOnFailureListener{exception ->
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
            }
    }
    
    fun updateUserProfileData(activity: ProfileActivity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId())
            .update(userHashMap).addOnSuccessListener { 
                Log.i(activity.javaClass.simpleName, "Profile data updated")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener{exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
            }
    }
    
    fun loadUserData(activity:Activity, readBoardsList:Boolean = false){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {documentSnapshot -> 
                val loggedInUser = documentSnapshot.toObject(User::class.java)!!
                when(activity){
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainTrelloActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                    }
                    is ProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }
                }
            }.addOnFailureListener{e ->

                when(activity){
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainTrelloActivity -> {
                        activity.hideProgressDialog()
                    }
                    
                }
                
                Log.e(activity.javaClass.simpleName, "Error $e")
            }
    }
    
    fun getCurrentUserId():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }
}