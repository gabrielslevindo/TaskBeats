package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsAplication
import com.comunidadedevspace.taskbeats.data.local.Task
import com.comunidadedevspace.taskbeats.data.local.TaskDao
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val taskDao: TaskDao
) : ViewModel() {


    fun execute(taskAction: TaskAction) {
        when (taskAction.actiontype) {
            ActionType.DELETE.name -> deleteById(taskAction.task!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)
            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
        }
    }


    private fun deleteAll() { // deletar todos

        viewModelScope.launch {
            taskDao.deleteAll()
        }
    }

    private fun deleteById(id: Int) { // deletar todos

        viewModelScope.launch {
            taskDao.deleteById(id)
        }
    }

    private fun insertIntoDataBase(task: Task) { // inserir na base de dados
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    private fun updateIntoDataBase(task: Task) { // fazer o update

        viewModelScope.launch {

            taskDao.update(task)

        }
    }

    companion object {


        fun getVMFactory(application: Application): ViewModelProvider.Factory {

            val dataBaseInstance = (application as TaskBeatsAplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()


            val factory = object : ViewModelProvider.Factory {

                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskDetailViewModel(dao) as T
                }
            }



            return factory


        }


    }
}


