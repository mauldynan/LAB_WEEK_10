package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.database.TotalObject
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    companion object {
        const val ID: Long = 1
    }

    private val db by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeValueFromDatabase()
        prepareViewModel()
    }

    override fun onPause() {
        super.onPause()

        val latestObject = viewModel.total.value ?: TotalObject()
        val updatedObject = latestObject.copy(
            date = Date().toString()
        )

        db.totalDao().update(Total(ID, updatedObject))
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val totalList = db.totalDao().getTotal(ID)

        if (totalList.isEmpty()) {
            db.totalDao().insert(Total(id = ID, total = TotalObject()))
        } else {
            val savedTotalObject = totalList.first().total

            viewModel.setTotal(savedTotalObject)

            showToastDate(savedTotalObject.date)
        }
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { totalObject ->
            updateText(totalObject.value)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun showToastDate(date: String) {
        if (date.isNotEmpty()) {
            Toast.makeText(this, date, Toast.LENGTH_LONG).show()
        }
    }
}