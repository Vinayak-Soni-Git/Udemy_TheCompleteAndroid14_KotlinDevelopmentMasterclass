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

    fun showImageChooser(activity:Activity){
        val galleyIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleyIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity:Activity, uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}