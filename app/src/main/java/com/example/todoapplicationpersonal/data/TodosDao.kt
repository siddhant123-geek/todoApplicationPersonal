package com.example.todoapplicationpersonal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapplicationpersonal.data.models.TodoItem


@Dao
interface TodosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(list: List<TodoItem>)

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoItem>

    @Query("DELETE FROM todos")
    suspend fun deleteTodos()

    @Query("DELETE FROM todos WHERE todoId = :todoId")
    suspend fun deleteTodos(todoId: Long)
}