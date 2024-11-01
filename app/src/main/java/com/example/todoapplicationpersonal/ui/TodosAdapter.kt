package com.example.todoapplicationpersonal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapplicationpersonal.data.models.TodoItem
import com.example.todoapplicationpersonal.databinding.TodoItemBinding

class TodosAdapter(private val todosList: List<TodoItem>):
    RecyclerView.Adapter<TodosAdapter.DataViewHolder>(){

    inner class DataViewHolder(private val binding: TodoItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoItem) {
            binding.todoName.text = todo.name
            binding.todoDescription.text = todo.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosAdapter.DataViewHolder {
        return DataViewHolder(
            TodoItemBinding.inflate(LayoutInflater.from(parent.context),
                                    parent,
                                    false),

        )
    }

    override fun onBindViewHolder(holder: TodosAdapter.DataViewHolder, position: Int) {
        holder.bind(todosList[position])
    }

    override fun getItemCount(): Int {
        return todosList.size
    }
}