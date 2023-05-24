package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsAplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskListViewModel(private val taskDao: TaskDao) : ViewModel() {


    //dessa forma temos o livedata dentro do viewmodel.
    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()

    fun execute(taskAction: TaskAction) {
        when (taskAction.actiontype) {
            ActionType.DELETE.name -> deleteById(taskAction.task!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)
            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
            ActionType.DELETE_ALL.name -> deleteAll()

        }
    }


    private fun deleteAll() { // deletar todos

        viewModelScope.launch(Dispatchers.IO) {


            taskDao.deleteAll()

        }


    }

    private fun deleteById(id: Int) { // deletar todos

        viewModelScope.launch(Dispatchers.IO) {

            taskDao.deleteById(id)

        }

    }

    private fun insertIntoDataBase(task: Task) { // inserir na base de dados


        viewModelScope.launch(Dispatchers.IO) {


            taskDao.insert(task)

        }
    }

    private fun updateIntoDataBase(task: Task) { // fazer o update

        viewModelScope.launch(Dispatchers.IO) {

            taskDao.update(task)

        }
    }


    companion object {


        fun create(application: Application): TaskListViewModel {

            val dataBaseInstance = (application as TaskBeatsAplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return TaskListViewModel(dao)


        }


    }


}