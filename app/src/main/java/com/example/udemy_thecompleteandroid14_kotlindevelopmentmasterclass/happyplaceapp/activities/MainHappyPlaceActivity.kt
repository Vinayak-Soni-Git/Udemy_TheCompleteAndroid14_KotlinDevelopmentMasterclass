package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMainHappyPlaceBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.adapters.HappyPlacesAdapter
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.database.DatabaseHandler
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.happyplaceapp.models.HappyPlaceModel
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainHappyPlaceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainHappyPlaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlacesListFromLocalDB()
    }
    
    private fun setupHappyPlacesRecyclerView(happyPlaceList:ArrayList<HappyPlaceModel>){
        binding.happyPlacesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.happyPlacesRecyclerView.setHasFixedSize(true)
        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        binding.happyPlacesRecyclerView.adapter = placesAdapter
        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainHappyPlaceActivity, HappyPlaceDetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })
        val editSwipeHandler = object:SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.happyPlacesRecyclerView.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainHappyPlaceActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
                
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.happyPlacesRecyclerView)

        val deleteSwipeHandler = object:SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.happyPlacesRecyclerView.adapter as HappyPlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                getHappyPlacesListFromLocalDB()

            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.happyPlacesRecyclerView)
    }
    
    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList:ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        if(getHappyPlaceList.size > 0 ){
            binding.happyPlacesRecyclerView.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }else{
            binding.happyPlacesRecyclerView.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                getHappyPlacesListFromLocalDB()
            }else{
                Log.e("MainHappyPlaceActivity", "Cancelled or Back Pressed")
            }
        }
    }
    
    companion object{
        val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extraPlaceDetails"
    }
}