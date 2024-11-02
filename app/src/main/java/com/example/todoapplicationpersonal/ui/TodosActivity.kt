package com.example.todoapplicationpersonal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapplicationpersonal.TodosApplication
import com.example.todoapplicationpersonal.data.models.QuoteItem
import com.example.todoapplicationpersonal.data.models.TodoItem
import com.example.todoapplicationpersonal.databinding.ActivityTodosBinding
import com.example.todoapplicationpersonal.di.component.DaggerActivityComponent
import com.example.todoapplicationpersonal.di.module.ActivityModule
import com.example.todoapplicationpersonal.ui.viewModel.TodosViewModel
import com.example.todoapplicationpersonal.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodosActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTodosBinding

    private lateinit var quoteOfTheDay: QuoteItem

    @Inject
    lateinit var todosAdapter: TodosAdapter

    @Inject
    lateinit var viewModel: TodosViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()

        binding = ActivityTodosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupUi()
        setUpObserver()
    }

    private fun setupUi() {
        binding.todosRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodosActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@TodosActivity,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = todosAdapter
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateTodos.collect {
                    when(it) {
                        is UiState.Success -> {
                            binding.todosRecyclerView.visibility = View.VISIBLE
                            renderList(it.data)
                            binding.progressBar.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.todosRecyclerView.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@TodosActivity,
                                "There was an error in getting the todos",
                                Toast.LENGTH_LONG).show()
                        }
                        is UiState.Loading -> {
                            Log.d(this@TodosActivity.javaClass.simpleName,
                                "setupObserver: uiState loading ")
                            binding.progressBar.visibility = View.VISIBLE
                            binding.todosRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun renderList(list: List<TodoItem>) {
        todosAdapter.addTodoItems(list)
        todosAdapter.notifyDataSetChanged()
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .applicationComponent((application as TodosApplication).applicationComponent)
            .build()
            .inject(this)
    }
}