package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.CardDetailsActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.CreateBoardActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.MainTrelloActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.activities.MembersActivity
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
    fun addUpdateTaskList(activity:Activity, board:Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList
        
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener { 
                Log.e(activity.javaClass.simpleName, "TaskList updated successfully.")
                if(activity is TaskListActivity){
                    activity.addUpdateTaskListSuccess()
                }else if(activity is CardDetailsActivity){
                    activity.addUpdateTaskListSuccess()
                }
            }.addOnFailureListener{exception ->
                if(activity is TaskListActivity){
                    activity.hideProgressDialog()
                }else if(activity is CardDetailsActivity){
                    activity.hideProgressDialog()
                }
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
            }
    }
    
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS).document(getCurrentUserId())
            .update(userHashMap).addOnSuccessListener { 
                Log.i(activity.javaClass.simpleName, "Profile data updated")
                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                
                when(activity){
                    is MainTrelloActivity->{
                        activity.tokenUpdateSuccess()
                    }
                    is ProfileActivity->{
                        activity.profileUpdateSuccess()
                    }
                }
            }.addOnFailureListener{exception ->
                when(activity){
                    is MainTrelloActivity->{
                        activity.hideProgressDialog()
                    }
                    is ProfileActivity->{
                        activity.hideProgressDialog()
                    }
                }
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
    
    fun getAssignedMembersListDetails(activity:Activity, assignedTo:ArrayList<String>){
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val userList:ArrayList<User> = ArrayList()
                for(i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    userList.add(user)
                }
                if(activity is MembersActivity){
                    activity.setupMembersList(userList)
                }else if(activity is TaskListActivity){
                    activity.boardMembersDetailsList(userList)
                }
            }.addOnFailureListener{exception->
                if(activity is MembersActivity){
                    activity.hideProgressDialog()
                }else if(activity is TaskListActivity){
                    activity.hideProgressDialog()
                }
                
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
            }
    }
    
    fun getMemberDetails(activity: MembersActivity, email:String){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL, email)
            .get()
            .addOnSuccessListener {document->
                if(document.documents.size > 0){
                    val user = document.documents[0].toObject(User::class.java)!!
                    activity.memberDetails(user)
                }else{
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("No such member found")
                }
            }.addOnFailureListener{exception->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting user details",exception)
            }
    }
    
    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User){
        val assignedToHashMap = HashMap<String, Any>()
        assignedToHashMap[Constants.ASSIGNED_TO] = board.assignedTo
        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToHashMap)
            .addOnSuccessListener { 
                activity.memberAssignedSuccess(user)
            }.addOnFailureListener{exception->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)
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