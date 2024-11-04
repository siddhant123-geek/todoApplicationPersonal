package com.example.todoapplicationpersonal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoapplicationpersonal.data.models.TodoItem
import kotlinx.coroutines.flow.Flow


@Dao
interface TodosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(list: List<TodoItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItem)

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Query("DELETE FROM todos")
    suspend fun deleteTodos()

    @Query("DELETE FROM todos WHERE todoId = :todoId")
    suspend fun deleteTodos(todoId: Long)

    @Query("UPDATE todos SET status = :newStatus WHERE todoId = :id")
    suspend fun updateTodoStatus(id: Long, newStatus: Boolean)

    @Query("SELECT todoId FROM todos WHERE status = 0")
    suspend fun getIncompleteTasks(): List<Long>
}