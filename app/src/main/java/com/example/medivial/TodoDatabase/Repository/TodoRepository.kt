package com.example.medivial.TodoDatabase.Repository

import androidx.lifecycle.LiveData
import com.example.medivial.TodoDatabase.Todo
interface TodoRepository{
    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo:Todo)
    suspend fun updateTodo(todo:Todo)
    suspend fun getTodoById(id:Int):Todo?
    fun  getList(): LiveData<List<Todo>>
}