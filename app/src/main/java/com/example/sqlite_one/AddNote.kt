package com.example.sqlite_one

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlite_one.databinding.ActivityAddNoteBinding

class AddNote : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        // Save button click listener
        binding.save.setOnClickListener{
            // Get title and content from EditText fields
            val title = binding.titleEdt.text.toString()
            val content = binding.etDescription.text.toString()

            // Check if both title and content are empty
            if (title.isEmpty() && content.isEmpty()) {
                // Display error message if both are empty
                Toast.makeText(this, "Please write something in the note", Toast.LENGTH_SHORT).show()
            } else {
                // At least one of title or content is not empty, proceed with saving the note
                val note = Note(0, title, content)
                db.insertNote(note)
                finish() // Finish the activity after saving
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
