package com.example.todoapplicationpersonal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapplicationpersonal.data.models.TodoItem

@Database(entities = [TodoItem::class], version = 1)
abstract class TodosDatabase: RoomDatabase() {

    abstract fun todosDao(): TodosDao
}