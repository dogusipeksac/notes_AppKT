package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteFragment : BaseFragment() {

    var currentData:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{}

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf=SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentData=sdf.format(Date())
        tvDateTime.text=currentData
        imgDone.setOnClickListener(View.OnClickListener {
        saveNote()

        })
        imageBack.setOnClickListener {
            replaceFragment(HomeFragment.newInstance(),false)
        }

    }
    private fun saveNote(){
        if(etNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"title required",Toast.LENGTH_LONG).show()
        }
        if(etSubTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"sub title required",Toast.LENGTH_LONG).show()
        }
        if(etNoteDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"note desc required",Toast.LENGTH_LONG).show()
        }

        launch{
            val notes= Notes()
            notes.title=etNoteTitle.text.toString()
            notes.subTitle=etSubTitle.text.toString()
            notes.noteText=etNoteDesc.text.toString()
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                etNoteDesc.setText("")
                etNoteTitle.setText("")
                etSubTitle.setText("")
            }
        }

    }
    fun replaceFragment(fragment: Fragment,istrantion:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istrantion){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }
}