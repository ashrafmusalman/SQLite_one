package com.example.sqlite_one

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlite_one.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var db: NoteDatabaseHelper
    private lateinit var notesAdapter: rv_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        notesAdapter = rv_adapter(db.getAllNotes(), this)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = notesAdapter

        binding.addButton.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }
}
