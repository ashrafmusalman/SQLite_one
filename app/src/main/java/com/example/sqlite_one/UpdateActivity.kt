package com.example.sqlite_one

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlite_one.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)

        setContentView(binding.root)


        db = NoteDatabaseHelper(this)
        noteId = intent.getIntExtra("note_id", -1)//checking null value
        if (noteId == -1) {
            finish()
            return
        }

        val note =
            db.getNoteById(noteId)//this helps to know which note is clicked ,because afteer knowing the note id we can do the update
        binding.UpdatetitleEdt.setText(note.title)//this will helps to retrive the previous title and descriotpion
        binding.UpdateetDescription.setText(note.content)



        binding.save.setOnClickListener{
            val newTitle=binding.UpdatetitleEdt.text.toString()
            val newUpdateDescription=binding.UpdateetDescription.text.toString()

            val updatedNote=Note(noteId,newTitle,newUpdateDescription)
            db.updateNote(updatedNote)
            finish()

            Toast.makeText(this, "Changes made are saved", Toast.LENGTH_SHORT).show()

        }



    }
}