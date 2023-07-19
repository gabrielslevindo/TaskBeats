package com.comunidadedevspace.taskbeats.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.local.News


/**
 * A simple [Fragment] subclass.
 * Use the [NewsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsListFragment : Fragment() {

    private val viewModel by lazy {

        NewsListViewModel.create()

    }

    private val adapter = NewsListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rvNews: RecyclerView = view.findViewById(R.id.rv_newsList)
        rvNews.adapter = adapter


        viewModel.newsListLiveData.observe(viewLifecycleOwner) { newsList ->

            val newsList = newsList.map { newsDto ->
                News(
                    title = newsDto.title,
                    imgUrl = newsDto.imgUrl

                )
            }

            adapter.submitList(newsList)

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NewsListFragment.
         */

        @JvmStatic
        fun newInstance() =
            NewsListFragment()
    }
}
