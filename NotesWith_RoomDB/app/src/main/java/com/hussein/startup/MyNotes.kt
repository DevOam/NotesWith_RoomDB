package com.hussein.startup


import android.os.Bundle

import android.view.*
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_my_notes.*
import kotlinx.android.synthetic.main.fragment_my_notes.view.*
import kotlinx.android.synthetic.main.note.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class MyNotes : Fragment() {


    //TODO:  9- Define DB instance
    var dbManager:NotesDataBase?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView= inflater.inflate(R.layout.fragment_my_notes, container, false)

        setHasOptionsMenu(true)
        //TODO:  10- init DB instance
        dbManager=DBManager().getDataBase(context!!.applicationContext)


        //Load from DB
        rootView.lvNotes.adapter=MyNotesAdpater(loadQuery( "%"))


        return rootView

    }


    fun loadQuery(title:String):ArrayList<Note>{



        //TODO:  11- Load all notes from DB that has  search 'title' token


        return dbManager!!.NoteDao().loadByTiltle(title) as ArrayList<Note>



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(context, query, Toast.LENGTH_LONG).show()
                lvNotes.adapter=MyNotesAdpater(loadQuery( "%$query%"))
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId){
                R.id.addNote->{
                    //Got to add page
                    view!!.findNavController().navigate(R.id.gotToDetails)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    inner class  MyNotesAdpater: BaseAdapter {
        var listOfNotesAdpater=ArrayList<Note>()
        constructor(listOfNotesAdpater:ArrayList<Note>):super(){
            this.listOfNotesAdpater=listOfNotesAdpater
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var myView=layoutInflater.inflate(R.layout.note,null)

            var myNote=listOfNotesAdpater[p0]
            myView.tvTitle.text=myNote.Title
            myView.tvDes.text=myNote.Description
            myView.ivDelete.setOnClickListener{

                //TODO:  12-  Delete Notes
                dbManager!!.NoteDao().delete(myNote)
               lvNotes.adapter=MyNotesAdpater(loadQuery( "%"))

            }
            myView.ivEdit.setOnClickListener{

                goToUpdate(myNote)

            }
            return myView
        }

        override fun getItem(p0: Int): Any {
            return listOfNotesAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return listOfNotesAdpater.size
        }



    }


    fun goToUpdate(note:Note){

        var bundle=  Bundle()
        bundle.putInt("ID",note.ID)
        bundle.putString("name",note.Title)
        bundle.putString("des",note.Description)
        view!!.findNavController().navigate(R.id.gotToDetails,bundle)


    }


}
