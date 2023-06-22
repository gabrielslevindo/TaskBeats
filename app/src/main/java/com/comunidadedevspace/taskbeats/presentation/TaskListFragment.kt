package com.comunidadedevspace.taskbeats.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task


/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {

    //Linear layout para mostrar estado de vazio
    private lateinit var ctnContent: LinearLayout

   //Adaapter
    private val adapter: TaskListAdapter by lazy {

        TaskListAdapter(::openTaskListDetail)
    }

    //ViewModel - fomra diferente de pegar o aplication precisamos fazer o requerimento da activity;

    private val viewModel: TaskListViewModel by lazy{

        TaskListViewModel.create(requireActivity().application)
    }




   // Aqui é onde criamos a view do fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    // Aqui é o tratamento depois que a view ja foi criada;
    // Fragment é como se fosse um conteiner , sempre que queremos encontrar a view vamos precisar chamar a view. o que precisamos
   // sempre que precisamos chamar algo para termos acesso ex viewmodel, adapter pelo id,
    //precisamos chamar onde a view ja foi criada ou seja no onviewcreated;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        // Aqui chamamos a imagem de quando não temos nada na lista.
        ctnContent = view.findViewById(R.id.ctn_content)


        //RecyclerView está aqui:
        val rvTasks: RecyclerView = view.findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

    }


//Para dar start na lista no fragment;
    override fun onStart() {
        super.onStart()
        listFromDataBase()
    }


    private fun listFromDataBase() { // esse pega todas as listas e atualiza no adapter

        val listObserver = Observer<List<Task>>{listTasks ->
            //obeserver

            if(listTasks.isEmpty()){

                ctnContent.visibility = View.VISIBLE

            }else {

                ctnContent.visibility = View.GONE

            }
            adapter.submitList(listTasks)
        }
        //livedata
        viewModel.taskListLiveData.observe(this, listObserver)

    }


    //Função para abrir a tela de detalhe; porem mudamos pois não preciso do resultado de outra activity,
    // apenas pedimos para abrir a próxima, pois o detalhe vai ter seu proprio Viewmodel;
    //Alem disso ao envez de passar o this da activity precisamos passar o requirecontext pois
    // o fragment não tem suporte ao this como contexto assim como na actitity;
    //Ela não passa mais uma tarefa opicional, pois essa função só funciona se clicar em uma tarefa;


    private fun openTaskListDetail(task: Task) {

        val intent = TaskDetailActivity.start(requireContext(), task)
       requireActivity().startActivity(intent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment TaskListFragment.
         */
        //
        @JvmStatic
        fun newInstance() =
            TaskListFragment()
            }
    }
