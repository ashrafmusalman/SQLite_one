package com.example.sqlite_one

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.coroutines.coroutineContext

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {

        //since we need all these thing time and again so we have made it under companion object and we dont need to call them since the  companion object is like
        // the statci keyword so we dont need to call  them they will be called itself during object instantiation of the class
        private const val DATABASE_NAME = "notesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnote"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"

        //we have here taken the varibale name as private so that later on no one can change our table name ,database name and so on
    }

    override fun onCreate(db: SQLiteDatabase?) {//here we create the table
        val createTableQuerry =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"

        db?.execSQL(createTableQuerry)//this is to execute the table querry

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuerry = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuerry)
        onCreate(db)

    }


    //➡️➡️THIS IS THE INSERT COMMAND USED IN THE DATABASE
    fun insertNote(note: Note) {
        val db =
            writableDatabase//this is opening of database and it allows to make changes in database schema
        val values = ContentValues().apply {
            put(
                COLUMN_TITLE,
                note.title
            )//we don't need to put on the id as the id has autoIncrement constraint
            put(COLUMN_CONTENT, note.content)


        }
        db.insert(
            TABLE_NAME,
            null,
            values
        )//this insert command help to put the table name and the contentValue i.e the column name of the table
        db.close()

    }


    //➡️THIS IS THE SELECT COMMAND USED IN TABLE
    fun getAllNotes(): List<Note> {//here we have prepared a gettallNote function and the return type will be of kind Note which is a list type
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val querry = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(querry, null)

        while (cursor.moveToNext()) {//this is moving cursor from one row to another to go through all the row and   and if true the below code will execute ohter wise the
            // the condition become false and the cruson doesnot move to the next line


            //if while loop execute then this code will execute ,where we go the column of the following  and put them in their respective varibale
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))


            ///after taking the data of the title we just  put these column data into the note
            val note = Note(
                id,
                title,
                content
            )//Create a Note Object: With this information, we create a Note object. It's like writing down all the details of a person on a small card.
            noteList.add(note)//Add the Note to the List: We put this small card (the Note object) into our backpack (the noteList). This way, we're collecting all the notes we find.


        }
        cursor.close()
        db.close()
        return noteList//Return the List of Notes: Finally, we give back our backpack full of notes to whoever asked for it. They now have all the information about every note in our list.
    }


    //➡️➡️THIS IS THE update COMMAND USED IN THE DATABASE

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values =
            ContentValues().apply {//the contenet values class is used to keep the column that is used to insert or update the column of the database


                put(COLUMN_TITLE, note.title)//putting the values in the respective column
                put(COLUMN_CONTENT, note.content)
            }

        val wherClause =
            "$COLUMN_ID=?"//varibale used to identify the column to be updated with the help of the column id
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, wherClause, whereArgs)
        db.close()

    }


    //➡️➡️THIS IS THE update COMMAND USED IN THE DATABASE

    fun getNoteById(noteId: Int): Note {
        val db = readableDatabase
        val querry = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$noteId"
        val cursor = db.rawQuery(querry, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id, title, content)
    }


    //➡️➡️THIS IS THE DELETE COMMAND USED IN THE DATABASE


    fun deleteNoteById(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()

    }
}