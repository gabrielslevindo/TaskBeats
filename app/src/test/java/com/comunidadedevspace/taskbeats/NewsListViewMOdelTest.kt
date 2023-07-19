package com.comunidadedevspace.taskbeats

import MainDispatcherRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.comunidadedevspace.taskbeats.data.remote.NewsDto
import com.comunidadedevspace.taskbeats.data.remote.NewsResponse
import com.comunidadedevspace.taskbeats.data.remote.NewsService
import com.comunidadedevspace.taskbeats.presentation.NewsListViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class NewsListViewMOdelTest {
    //como estamos utilizando o viewmodel scope pra trocar de tread temos que utilziar o main dispacherrule;
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    //Esse aqui é para corrigir o erro de mainlooper e vai pegar direto do live data sem precisar criar nenhuma instancia diferente do dispacher
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private val service: NewsService = mock()

    private lateinit var underTest: NewsListViewModel


    @Test
    fun `GIVEN request succed news WHEN fatch THEN return List`() {
        runBlocking {


            //given
            val expected = listOf<NewsDto>(

                NewsDto(
                    id = "id1",
                    content = "content1",
                    imgUrl = "image1",
                    title = "title1"
                )
            )

            val response = NewsResponse(data = expected, category = "tech")
            whenever(service.fetchNews()).thenReturn(response)



            //when
            underTest = NewsListViewModel(service)

            val result = underTest.newsListLiveData.getOrAwaitValue()


            //then
            assert(result == expected)
        }

    }
}