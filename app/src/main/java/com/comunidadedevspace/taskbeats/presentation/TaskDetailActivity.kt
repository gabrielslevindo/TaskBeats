package com.comunidadedevspace.taskbeats.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.Task
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

    private var task: Task? = null
    private lateinit var btnDone: Button


    //  A função do by é só utilizar o viewmodel quando é chamado;
    private val taskDetailViewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModel.getVMFactory(application)
    }


    companion object {

        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent {


            val intent = Intent(context, TaskDetailActivity::class.java)

                .apply {

                    putExtra(TASK_DETAIL_EXTRA, task)

                }

            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //Aqui chamamos o nosso menu no tool bar que criamos
        setSupportActionBar(findViewById(R.id.toolbar))


        //recuperar string da tela anterior.
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?


        // Aqui estou recuperando os edit text para descrição e para titulo da tarefa.
        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDesc = findViewById<EditText>(R.id.edt_task_description)

        //Aqui recupero o id do botão
        btnDone = findViewById<Button>(R.id.btn_done)

        if (task != null) {
            edtTitle.setText(task!!.title)
            edtDesc.setText(task!!.description)
        }

        btnDone.setOnClickListener {

            val title = edtTitle.text.toString()
            val desc = edtDesc.text.toString()


            if (title.isNotEmpty() && desc.isNotEmpty()) {

                if (task == null) {
                    //tarefa igual a vazio
                    //Cria a nova tarefa
                    addOrUpdateTask(0, title, desc, ActionType.CREATE)

                } else {
                    //atualizar a tarefa task.id é para pegar o id da tarefa anterior
                    addOrUpdateTask(task!!.id, title, desc, ActionType.UPDATE)

                }

            } else {

                showMessage(it, "Filds are requered")

            }
        }


    }

    //Aqui é a função de atualizar ou atualizar a tarefa, no if e else abaixo de btn done chamamos ela.
    private fun addOrUpdateTask(
        id: Int,
        title: String,
        description: String,
        actionType: ActionType
    ) {

        val task = Task(id, title, description)
        performAction(task, actionType)


    }


    //recuperar campo do xml
    //    tvTitle = findViewById<TextView>(R.id.tv_task_title_detail)


    // setar um novo texto na tela
    //     tvTitle.text = task?.title ?:"Adicione uma tarefa"
    // função criada para alocar o titulo e a descrição


    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // aqui estamos inflando o botão pra conseguir utilizar ele

        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu_task_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) { // aqui é para setar a ação de deletar
            R.id.delete_task -> {
                //Código para sretar o resultado na tela anterior.
                if (task != null) {
                    performAction(task!!, ActionType.DELETE)
                } else {
                    showMessage(btnDone, "Item not found")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performAction(task: Task, actionType: ActionType) {


        val taskAction = TaskAction(task, actionType.name)
        taskDetailViewModel.execute(taskAction)
        finish()

    }
}


private fun showMessage(view: View, message: String) {

    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show()

}
