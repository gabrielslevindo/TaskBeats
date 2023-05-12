package com.comunidadedevspace.taskbeats

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.launch
import java.io.Serializable


class MainActivity : AppCompatActivity() {


    private lateinit var ctnContent: LinearLayout

    private val adapter: TaskListAdapter by lazy {

        TaskListAdapter(::onListItemClicked)
    }

    //Banco de dados;
    val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "Taskbeats-database"
        ).build()
    }


    private val dao by lazy { dataBase.taskDao() }


    // adapter
    private val startForResult = registerForActivityResult(

        ActivityResultContracts.StartActivityForResult()
    )
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            //Pegando Resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TeskAction?
            val task: Task = taskAction!!.task

            if (taskAction.actiontype == ActionType.DELETE.name) {
                deleteById(task.id)
            } else if (taskAction.actiontype == ActionType.CREATE.name) {
                insertIntoDataBase(task)
            } else if (taskAction.actiontype == ActionType.UPDATE.name) {
                updateIntoDataBase(task)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar)) // para adicionar o menu no toolbar




        listFromDataBase()
        // Aqui chamamos a imagem de quando não temos nada na lista.
        ctnContent = findViewById(R.id.ctn_content)


        //Aqui vou chamar o adapter:


        //RecyclerView está aqui:
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)

        }
    }

    private fun insertIntoDataBase(task: Task) { // inserir na base de dados

        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDataBase()
        }
    }

    private fun updateIntoDataBase(task: Task) { // fazer o update

        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDataBase()
        }
    }
 private fun deleteAll(){ // deletar todos

     CoroutineScope(IO).launch {

         dao.deleteAll()
         listFromDataBase()

     }}
     private fun deleteById(id:Int){ // deletar todos

     CoroutineScope(IO).launch {
         dao.deleteById(id)
         listFromDataBase()

     }

 }

    private fun listFromDataBase() { // esse pega todas as listas e atualiza no adapter


        CoroutineScope(IO).launch {

            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)


        }
    }

    private fun showMessage(view: View, message: String) {

        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

    }


    private fun onListItemClicked(task: Task) {

        openTaskListDetail(task)

    }

    //deful argument = posso chamar a ms função poremcomo ela tem o defult argument nã preciso passar a tareja

    private fun openTaskListDetail(task: Task? = null) {

        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu_task_tasklist, menu)

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) { // aqui é para setar a ação de deletar
            R.id.delete_all_task -> {
                deleteAll() //para deletar todas as tarefas
                true
            }else  -> super.onOptionsItemSelected(item)

        } }
}

//CRUD
//CREATE
//READ
//UPDATE
//DELETE
enum class ActionType {


    DELETE,
    UPDATE,
    CREATE

}

data class TeskAction(
    val task: Task,
    val actiontype: String

) : Serializable


const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT "
