package com.example.todoapplicationpersonal.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapplicationpersonal.TodosApplication
import com.example.todoapplicationpersonal.data.TodosDatabase
import com.example.todoapplicationpersonal.data.models.TodoItem
import com.example.todoapplicationpersonal.databinding.ActivityTodosBinding
import com.example.todoapplicationpersonal.di.component.DaggerActivityComponent
import com.example.todoapplicationpersonal.di.module.ActivityModule
import com.example.todoapplicationpersonal.ui.viewModel.TodosViewModel
import com.example.todoapplicationpersonal.utils.AppConstants.SHARED_PREFS_NAME
import com.example.todoapplicationpersonal.utils.AppConstants.TASK_PENDING
import com.example.todoapplicationpersonal.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodosBinding
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    @Inject
    lateinit var todosAdapter: TodosAdapter

    @Inject
    lateinit var viewModel: TodosViewModel

    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var db: TodosDatabase

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setPositiveButton("Got it!") { dialog, _ -> dialog.dismiss() }
            .create()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

        binding = ActivityTodosBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupUi()
        setUpObserver()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
        setupClickListeners()
    }

    private fun setupUi() {
        binding.todosRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodosActivity)
            adapter = todosAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupClickListeners() {
        binding.addTodoButton.setOnClickListener {
            val todo = binding.todoNameInput.text.toString()
            val todoDesc = binding.todoDescriptionInput.text.toString()

            // clear the edit texts
            binding.todoNameInput.setText("")
            binding.todoDescriptionInput.setText("")

            if (todo.isEmpty()) {
                Toast.makeText(this, "Please enter a valid todo text", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (todoDesc.isEmpty()) {
                Toast.makeText(this, "Please enter a valid todo description", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val todoItem = TodoItem(
                name = todo,
                description = todoDesc,
                isCompleted = false
            )

            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.addTodoItem(todoItem)
            }
        }

        binding.deleteAllTodos.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.deleteAllTodos()
            }
        }

        todosAdapter.checkChangedListener = { todoId, isChecked ->
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.updateTodo(todoId, isChecked)
            }
        }

        todosAdapter.deleteTodo = {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.deleteTodo(it)
            }
        }

        binding.motivateMe.setOnClickListener {
            viewModel.fetchQuote()
            viewModel.isMotivateMeBtnClicked = true
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateTodos.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.todosRecyclerView.visibility = View.VISIBLE
                            renderList(it.data)
                            binding.progressBar.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            binding.todosRecyclerView.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@TodosActivity,
                                "There was an error in getting the todos",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.todosRecyclerView.visibility = View.GONE
                        }
                    }
                    withContext(Dispatchers.IO) {
                        val pendingTasks = db.todosDao().getIncompleteTasks()
                        if (pendingTasks.isNotEmpty()) {
                            sharedPreferences.edit().putBoolean(TASK_PENDING, true)
                                .apply()
                        } else {
                            sharedPreferences.edit().putBoolean(TASK_PENDING, false)
                                .apply()
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateQuote
                    .collect {
                        when (it) {
                            is UiState.Success -> {
                                dialog.setTitle("${it.data.author} once said -")
                                dialog.setMessage(it.data.quote)
                                if (viewModel.isMotivateMeBtnClicked) {
                                    binding.progressBar.visibility = View.GONE
                                    dialog.show()
                                }
                            }

                            is UiState.Error -> {
                                binding.progressBar.visibility = View.GONE
                                val errorMsg = it.error
                                if (viewModel.isMotivateMeBtnClicked) {
                                    Toast.makeText(
                                        this@TodosActivity,
                                        "There was an error in getting your Quote, please try again. Error - $errorMsg",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            is UiState.Loading -> {
                                if (viewModel.isMotivateMeBtnClicked) {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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