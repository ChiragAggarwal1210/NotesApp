package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

lateinit var viewModel: NoteViewModel


class MainActivity : AppCompatActivity(), INotesRVAdapter {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager= LinearLayoutManager(this)
        val adapter = NotesRVAdapter(this,this)
        recyclerView.adapter = adapter
        fab.setOnClickListener {_ ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Add Note Items")
            alert.setMessage("Enter Note To Add")
            val editTask = EditText(this)
            alert.setView(editTask)

            alert.setPositiveButton("Add"){dialog, _ ->
                val noteText= editTask.text.toString()
                if(noteText.isNotEmpty()){
                    viewModel.insertNote(Note(noteText))
                    Toast.makeText(this,"${noteText} Inserted",Toast.LENGTH_SHORT).show()
                }
                editTask.text.clear()
                dialog.dismiss()
            }
            alert.setNegativeButton("Cancel"){dialog, _ ->
                Toast.makeText(this,"Clicked Cancel",Toast.LENGTH_SHORT).show()
                editTask.text.clear()
                dialog.dismiss()
            }
            alert.show()

        }
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
            viewModel.allNotes.observe(this, Observer {list->
                list?.let {
                    adapter.updateList(it)
                }
            })


    }

    override fun onItemClicked(note: Note) {
        viewModel.deleteNote(note)
        Toast.makeText(this,"${note.text} Deleted",Toast.LENGTH_SHORT).show()
    }


}