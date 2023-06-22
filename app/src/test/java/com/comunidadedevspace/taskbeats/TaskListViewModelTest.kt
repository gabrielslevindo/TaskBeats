package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import com.comunidadedevspace.taskbeats.presentation.ActionType
import com.comunidadedevspace.taskbeats.presentation.TaskAction
import com.comunidadedevspace.taskbeats.presentation.TaskListViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TaskListViewModelTest {
    //Strategy
    private val taskDao: TaskDao = mock()

    private val underTest: TaskListViewModel by lazy {

        TaskListViewModel(
            taskDao, UnconfinedTestDispatcher()
        )
    }
    @Test
    fun delete_all() = runTest {

        val taskAction = TaskAction(

            task = null, actiontype = ActionType.DELETE_ALL.name
        )

        underTest.execute(taskAction)

        verify(taskDao).deleteAll()
    }

    @Test
    fun Updade_task() = runTest {

        val task = Task(

            id = 1, title = "title", description = "description"
        )

        //Given

        val taskAction = TaskAction(

            task = task, actiontype = ActionType.UPDATE.name

        )


        // when

        underTest.execute(taskAction)


        //then

        verify(taskDao).update(task)

    }

    @Test
    fun Delete_task() = runTest {

        val task = Task(

            id = 1, title = "title", description = "description"
        )

        //Given

        val taskAction = TaskAction(

            task = task, actiontype = ActionType.DELETE.name

        )


        // when

        underTest.execute(taskAction)


        //then

        verify(taskDao).deleteById(task.id)

    }


    @Test
    fun Insert_task() = runTest {

        val task = Task(

            id = 1, title = "title", description = "description"
        )

        //Given

        val taskAction = TaskAction(

            task = task, actiontype = ActionType.CREATE.name

        )


        // when

        underTest.execute(taskAction)


        //then

        verify(taskDao).insert(task)

    }


}

