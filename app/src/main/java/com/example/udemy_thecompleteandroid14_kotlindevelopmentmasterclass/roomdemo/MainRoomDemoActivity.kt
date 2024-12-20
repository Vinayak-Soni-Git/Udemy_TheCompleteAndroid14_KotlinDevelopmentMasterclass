package com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.roomdemo

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.R
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.ActivityMainRoomDemoBinding
import com.example.udemy_thecompleteandroid14_kotlindevelopmentmasterclass.databinding.DialogUpdateBinding
import kotlinx.coroutines.launch

class MainRoomDemoActivity : AppCompatActivity() {
    
    private var binding:ActivityMainRoomDemoBinding? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRoomDemoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        
        binding?.btnAdd?.setOnClickListener{
            addRecord(employeeDao)
        }
        lifecycleScope.launch { 
            employeeDao.fetchAllEmployees().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, employeeDao)
            }
        }
    }
    
    fun addRecord(employeeDao: EmployeeDao){
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        
        if(name.isNotEmpty() && email.isNotEmpty()){
            lifecycleScope.launch { 
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT).show()
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        }else{
            Toast.makeText(applicationContext, "Name or email cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupListOfDataIntoRecyclerView(employeesList:ArrayList<EmployeeEntity>, employeeDao: EmployeeDao){
        if(employeesList.isNotEmpty()){
            val itemAdapter = EmployeeAdapter(employeesList, 
                {updateId -> updateRecordDialog(updateId, employeeDao)}, 
                {deleteId -> deleteRecordAlertDialog(deleteId, employeeDao)})
            
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        }else{
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
    private fun updateRecordDialog(id:Int, employeeDao: EmployeeDao){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCanceledOnTouchOutside(false)
        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)
        
        lifecycleScope.launch { 
            employeeDao.fetchEmployeeById(id).collect{
                if(it != null){
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
                
                
            }
        }
        binding.tvUpdate.setOnClickListener{
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            
            if(name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch { 
                    employeeDao.update(EmployeeEntity(id, name, email))
                    Toast.makeText(this@MainRoomDemoActivity, "Record Updated.", Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(this@MainRoomDemoActivity, "name or email cannot be blank", Toast.LENGTH_LONG).show()
            }
        }
        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()
    }
    
    private fun deleteRecordAlertDialog(id:Int, employeeDao: EmployeeDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        
        builder.setPositiveButton("Yes"){dialogInterface,_ ->
            lifecycleScope.launch { 
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(this@MainRoomDemoActivity, "Record deleted successfully", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}