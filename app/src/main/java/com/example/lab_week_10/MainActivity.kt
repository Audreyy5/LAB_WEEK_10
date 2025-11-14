package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val db by lazy { TotalDatabase.getDatabase(this) }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    companion object {
        const val ID: Long = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    // Load nilai dari database
    private fun initializeValueFromDatabase() {
        val result = db.totalDao().getTotal(ID)

        if (result.isEmpty()) {
            // Insert pertama kali
            db.totalDao().insert(
                Total(
                    id = ID,
                    total = TotalObject(
                        value = 0,
                        date = Date().toString()
                    )
                )
            )
            viewModel.setTotal(0)
        } else {
            val obj = result.first().total
            viewModel.setTotal(obj.value)
        }
    }

    private fun prepareViewModel() {
        val textTotal = findViewById<TextView>(R.id.text_total)
        val btnIncrement = findViewById<Button>(R.id.button_increment)

        viewModel.total.observe(this) { value ->
            textTotal.text = getString(R.string.text_total, value)
        }

        btnIncrement.setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // Update value & date setiap kali app masuk background
    override fun onPause() {
        super.onPause()

        val value = viewModel.total.value ?: 0
        val date = Date().toString()

        db.totalDao().update(
            Total(
                id = ID,
                total = TotalObject(
                    value = value,
                    date = date
                )
            )
        )
    }

    // Show Toast saat app dibuka
    override fun onStart() {
        super.onStart()

        val result = db.totalDao().getTotal(ID)
        if (result.isNotEmpty()) {
            val savedDate = result.first().total.date
            Toast.makeText(this, "Last Updated: $savedDate", Toast.LENGTH_LONG).show()
        }
    }
}
