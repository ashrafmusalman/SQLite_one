package com.example.sqlite_one

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class rv_adapter(private var datalist: List<Note>, private val context: Context) : RecyclerView.Adapter<rv_adapter.myNoteViewHolder>() {

    // Initialize the database helper to interact with SQLite
    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    // Inner class to hold the view components of each item in the RecyclerView
    inner class myNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTv)
        val content: TextView = itemView.findViewById(R.id.contentTv)
        val updateButton: ImageView = itemView.findViewById(R.id.UpdateEditPen)
        val delete: ImageView = itemView.findViewById(R.id.UpdateDelete)
    }

    // Create a ViewHolder by inflating the item view layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myNoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.iitem_view, parent, false)
        return myNoteViewHolder(view)
    }

    // Return the total number of items in the dataset
    override fun getItemCount(): Int {
        return datalist.size
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: myNoteViewHolder, position: Int) {
        val currentNote = datalist[position]

        // Set the title and content of the note
        holder.title.text = currentNote.title
        holder.content.text = currentNote.content

        // Set long press listener to the item view
        holder.itemView.setOnLongClickListener {
            // Display options dialog for update or delete
            showOptionsDialog(currentNote)
            true // Consume the long press event
        }

        // Handle click on update button
        holder.updateButton.setOnClickListener {
            // Start UpdateActivity with the note ID
            val intent = Intent(context, UpdateActivity::class.java).apply {
                putExtra("note_id", currentNote.id)
            }
            context.startActivity(intent)
        }

        // Handle click on delete button
        holder.delete.setOnClickListener {
            // Delete the note from the database
            db.deleteNoteById(currentNote.id)
            // Refresh the RecyclerView with updated data
            refreshData(db.getAllNotes())
            // Show a toast message confirming the deletion
            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
        }
    }

    // Show options dialog for update or delete actions
    private fun showOptionsDialog(note: Note) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Choose Action")
        dialog.setItems(arrayOf("Update", "Delete")) { _, which ->
            when (which) {
                0 -> {
                    // Update action: Start UpdateActivity with the note ID
                    val intent = Intent(context, UpdateActivity::class.java).apply {
                        putExtra("note_id", note.id)
                    }
                    context.startActivity(intent)
                }
                1 -> {
                    // Delete action: Delete the note from the database
                    db.deleteNoteById(note.id)
                    // Refresh the RecyclerView with updated data
                    refreshData(db.getAllNotes())
                    // Show a toast message confirming the deletion
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    // Refresh the RecyclerView with new data
    fun refreshData(newNoteList: List<Note>) {
        datalist = newNoteList
        notifyDataSetChanged()
    }
}
