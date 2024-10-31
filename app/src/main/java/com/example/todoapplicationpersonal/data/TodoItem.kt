package com.example.todoapplicationpersonal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoItem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "todoId")
    val id: Long,
    @ColumnInfo(name = "todoName")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "status")
    val isCompleted: Boolean
)