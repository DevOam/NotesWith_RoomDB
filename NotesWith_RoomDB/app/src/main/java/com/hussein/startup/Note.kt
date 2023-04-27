package com.hussein.startup

import android.content.Context
import androidx.room.*


//TODO:  1- Convert Class to Entity
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val ID: Int,
   @ColumnInfo(name = "title") val Title: String,
    @ColumnInfo(name = "descreption")  val Description: String )

//TODO: 2- Define Doa
@Dao
interface NotesDao{
    @Insert
    fun insert(vararg note: Note)
    @Delete
    fun delete( note: Note)
    @Update
    fun update(note: Note)

    @Query("Select * From Note Where Title Like :title  ")
    fun loadByTiltle(title :String):List<Note>
}

// TODO: 3- create database
@Database(entities = arrayOf(Note::class), version = 1)
abstract class NotesDataBase:RoomDatabase(){
    abstract fun NoteDao():NotesDao
}



//TODO: 4- Create database instance
class DBManager{
    @Volatile
    private var instance:NotesDataBase?=null
    fun getDataBase(context: Context):NotesDataBase?{
        if(instance==null){
            synchronized(NotesDataBase::class.java){
                if (instance==null){
                 instance= Room.databaseBuilder(context.applicationContext,
                 NotesDataBase::class.java!!,"MyNotesDB")
                     .allowMainThreadQueries()
                     .build()
                }
            }
        }
        return instance
    }
}


