package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.lab_week_10.database.Total

class MainActivity : AppCompatActivity() {

    private val db by lazy { TotalDatabase.getDatabase(this) }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeValueFromDatabase()
        prepareViewModel()
    }

    // Create and build the TotalDatabase with the name 'total-database'
    // allowMainThreadQueries() is used to allow queries to be run on the main thread
    // This is not recommended, but for simplicity it's used here
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }
    // Initialize the value of the total from the database
    // If the database is empty, insert a new Total object with the value of 0
    // If the database is not empty, get the value of the total from the database
    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            db.totalDao().insert(Total(id = 1, total = 0))
        } else {
            viewModel.setTotal(total.first().total)
        }
    }
    // The ID of the Total object in the database
// For simplicity, we only have one Total object in the database
// So the ID is always 1
    companion object {
        const val ID: Long = 1
    }

    private fun prepareViewModel() {
        val textTotal = findViewById<TextView>(R.id.text_total)
        val buttonIncrement = findViewById<Button>(R.id.button_increment)

        viewModel.total.observe(this, Observer { total ->
            textTotal.text = getString(R.string.text_total, total)
        })

        buttonIncrement.setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Update the value of the total in the database
    // whenever the activity is paused
    // This is done to ensure that the value of the total is always up to date
    // even if the app is closed
    override fun onPause() {
        super.onPause()
        db.totalDao().update(Total(ID, viewModel.total.value!!))
    }
}
