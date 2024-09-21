package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.trelloclone.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS:String = "users"
    const val IMAGE:String = "image"
    const val NAME:String = "name"
    const val MOBILE:String = "mobile"
    const val ASSIGNED_TO:String = "assignedTo"
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val BOARDS:String = "boards"
    const val DOCUMENT_ID:String = "documentId"
    const val TASK_LIST:String = "taskList"
    const val BOARD_DETAILS:String = "boardDetail"
    const val ID:String = "id"
    const val EMAIL:String = "email"
    const val TASK_LIST_ITEM_POSITION:String = "taskListItemPosition"
    const val CARD_LIST_ITEM_POSITION:String = "cardListItemPosition"
    const val BOARD_MEMBERS_LIST:String = "boardMembersList"
    const val SELECT:String = "select"
    const val UN_SELECT:String = "unSelect"
    const val TRELLO_CLONE_PREFERENCES = "trelloClonePreferences"
    const val FCM_TOKEN_UPDATED = "fcmTokenUpdated"
    const val FCM_TOKEN = "fcmToken"

    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = ""
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"
    
    fun showImageChooser(activity:Activity){
        val galleyIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleyIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity:Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}