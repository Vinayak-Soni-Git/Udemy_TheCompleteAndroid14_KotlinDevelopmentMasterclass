package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityAddHappyPlaceBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.database.DatabaseHandler
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.models.HappyPlaceModel
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.utils.GetAddressFromLatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding:ActivityAddHappyPlaceBinding
    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    private var saveImageToStorage:Uri? = null
    private var mLatitude:Double = 0.0
    private var mLongitude:Double = 0.0
    private var mHappyPlaceDetails:HappyPlaceModel?= null
    private lateinit var mFusedLocationClient:FusedLocationProviderClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbarAddHappyPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddHappyPlace.setNavigationOnClickListener{
            onBackPressed()
        }
        
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        
        if(!Places.isInitialized()){
            Places.initialize(this, resources.getString(R.string.google_maps_api_key))
        }
        
        
        if(intent.hasExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)){
            mHappyPlaceDetails = intent.getParcelableExtra(MainHappyPlaceActivity.EXTRA_PLACE_DETAILS)
            
        }
        
        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        updateDateInView()
        if(mHappyPlaceDetails != null){
            supportActionBar?.title = "Edit Happy Place"

            binding.etTitle.setText(mHappyPlaceDetails!!.title)
            binding.etDescription.setText(mHappyPlaceDetails!!.description)
            binding.etDate.setText(mHappyPlaceDetails!!.date)
            binding.etLocation.setText(mHappyPlaceDetails!!.location)
            mLatitude = mHappyPlaceDetails!!.latitude
            mLongitude = mHappyPlaceDetails!!.longitude

            saveImageToStorage = Uri.parse(mHappyPlaceDetails!!.image)

            binding.ivPlaceImage.setImageURI(saveImageToStorage)

            binding.btnSave.text = "UPDATE"
            
        }
        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.etLocation.setOnClickListener(this)
        binding.tvSelectCurrentLocation.setOnClickListener(this)
    }
    
    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    
    private fun requestNewLocationData(){
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1000
        mLocationRequest.numUpdates = 1
        
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }
    
    private val mLocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation:Location = locationResult!!.lastLocation!!
            mLatitude = mLastLocation.latitude
            mLongitude = mLastLocation.longitude
            val addressTask = GetAddressFromLatLng(this@AddHappyPlaceActivity, mLatitude, mLongitude)
            addressTask.setAddressListener(object : GetAddressFromLatLng.AddressListener{
                override fun onAddressFound(address: String) {
                    binding.etLocation.setText(address)
                }

                override fun onError() {
                    Log.e("Get Address: ", "Something went wrong")
                }
            })
            addressTask.getAddress()
        }
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.et_date->{
                DatePickerDialog(this@AddHappyPlaceActivity,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.tv_add_image->{
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
                pictureDialog.setItems(pictureDialogItems){_, which->
                    run {
                        when (which) {
                            0 -> {
                                choosePhotoFromGallery()
                            }

                            1 -> {
                                takePhotoFromCamera()
                            }
                        }
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save->{
                when{
                    binding.etTitle.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    }
                    binding.etDescription.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
                    }
                    binding.etLocation.text.isNullOrEmpty()->{
                        Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToStorage == null->{
                        Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        val happyPlaceModel = HappyPlaceModel(
                            if(mHappyPlaceDetails == null) 0 else mHappyPlaceDetails!!.id,
                            binding.etTitle.text.toString(),
                            saveImageToStorage.toString(),
                            binding.etDescription.text.toString(),
                            binding.etDate.text.toString(),
                            binding.etLocation.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                        val dbHandler = DatabaseHandler(this)
                        if(mHappyPlaceDetails == null){
                            val addHappyPlaceResult = dbHandler.addHappyPlace(happyPlaceModel)
                            if(addHappyPlaceResult > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }else{
                            val updateHappyPlaceResult = dbHandler.updateHappyPlace(happyPlaceModel)
                            if(updateHappyPlaceResult > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }
            R.id.et_location->{
                try{
                    val fields = listOf(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS)
                    val intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this@AddHappyPlaceActivity)
                    startActivityForResult(intent, PLACE_AUTO_COMPLETE_REQUEST_CODE)
                    
                }catch(e:Exception){
                    e.printStackTrace()
                }
            }
            R.id.tv_select_current_location->{
                if(!isLocationEnabled()){
                    Toast.makeText(this, "Your location provider is turned off.Please turn it on.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }else{
                    Dexter.withActivity(this).withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(object : MultiplePermissionsListener{
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            if(report!!.areAllPermissionsGranted()){
                                requestNewLocationData()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: MutableList<PermissionRequest>?,
                            token: PermissionToken?
                        ) {
                            showRationalDialogForPermission()
                        }

                    })
                }
            }
        }
    }
    
    private fun choosePhotoFromGallery(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermission()
            }
        }).onSameThread().check()
    }
    
    private fun takePhotoFromCamera(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener{
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermission()
            }
        }).onSameThread().check()
    }
    
    private fun saveImageToInternalStorage(bitmap:Bitmap):Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${ UUID.randomUUID()}.jpg")
        try{
            val stream:OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY){
                if(data != null){
                    val contentUri = data.data
                    try{
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                        saveImageToStorage = saveImageToInternalStorage(selectedImageBitmap)
                        
                        binding.ivPlaceImage.setImageBitmap(selectedImageBitmap)
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this@AddHappyPlaceActivity, "Failed to load the image from the gallery", Toast.LENGTH_SHORT).show()
                    }
                }
            }else if(requestCode == CAMERA){
                val thumbnail:Bitmap = data!!.extras!!.get("data") as Bitmap
                saveImageToStorage = saveImageToInternalStorage(thumbnail)
                binding.ivPlaceImage.setImageBitmap(thumbnail)
            }else if(requestCode == PLACE_AUTO_COMPLETE_REQUEST_CODE){
                val place:Place = Autocomplete.getPlaceFromIntent(data!!)
                binding.etLocation.setText(place.address)
                mLatitude = place.latLng!!.latitude
                mLongitude = place.latLng!!.longitude
            }
        }
    }
    
    private fun showRationalDialogForPermission(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission" +
                " required for this feature. It can be enable under the application settings."
        ).setPositiveButton("GO TO SETTINGS"){_, _->
            run {
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
        }.setNegativeButton("Cancel"){dialog, _->
            run {
                dialog.dismiss()
            }
        }.show()
    }
    
    private fun updateDateInView(){
        val dateFormat = "dd.MM.yyyy"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
        binding.etDate.setText(simpleDateFormat.format(calendar.time).toString())
    }
    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
        private const val PLACE_AUTO_COMPLETE_REQUEST_CODE = 3
    }
}