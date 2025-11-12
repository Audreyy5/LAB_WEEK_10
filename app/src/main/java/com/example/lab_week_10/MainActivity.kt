package com.example.lab_week_10

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.lab_week_10.viewmodels.TotalViewModel
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViewModel()
    }

    private fun prepareViewModel() {
        val textTotal = findViewById<TextView>(R.id.text_total)
        val buttonIncrement = findViewById<Button>(R.id.button_increment)

        // Observe LiveData dari ViewModel
        viewModel.total.observe(this, Observer { total ->
            textTotal.text = getString(R.string.text_total, total)
        })

        // Saat tombol diklik, tambah total
        buttonIncrement.setOnClickListener {
            viewModel.incrementTotal()
        }
    }
}
