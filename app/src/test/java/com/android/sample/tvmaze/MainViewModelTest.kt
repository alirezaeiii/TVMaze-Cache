package com.android.sample.tvmaze

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.tvmaze.database.ShowDao
import com.android.sample.tvmaze.network.TVMazeService
import com.android.sample.tvmaze.repository.ShowRepository
import com.android.sample.tvmaze.util.Resource
import com.android.sample.tvmaze.util.contextProvider.TestContextProvider
import com.android.sample.tvmaze.util.isNetworkAvailable
import com.android.sample.tvmaze.viewmodel.MainViewModel
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: TVMazeService

    @Mock
    private lateinit var dao: ShowDao

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        mockkStatic("com.android.sample.tvmaze.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.fetchShowList()).thenReturn(emptyList())
            `when`(dao.getShows()).thenReturn(emptyList())
        }

        val repository = ShowRepository(dao, api, context, TestContextProvider())

        testCoroutineRule.pauseDispatcher()

        val viewModel = MainViewModel(repository)

        assertThat(viewModel.shows.value, `is`(Resource.loading()))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.shows.value, `is`(Resource.success(emptyList())))
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(Mockito.anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.tvmaze.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.fetchShowList()).thenThrow(RuntimeException(""))
            `when`(dao.getShows()).thenReturn(emptyList())
        }
        val repository = ShowRepository(dao, api, context, TestContextProvider())

        testCoroutineRule.pauseDispatcher()

        val viewModel = MainViewModel(repository)

        assertThat(viewModel.shows.value, `is`(Resource.loading()))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.shows.value, `is`(Resource.error(errorMsg)))
    }

    @Test
    fun givenNetworkUnAvailable_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(Mockito.anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.tvmaze.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns false
        testCoroutineRule.runBlockingTest {
            `when`(dao.getShows()).thenReturn(emptyList())
        }
        val repository = ShowRepository(dao, api, context, TestContextProvider())

        testCoroutineRule.pauseDispatcher()

        val viewModel = MainViewModel(repository)

        assertThat(viewModel.shows.value, `is`(Resource.loading()))

        testCoroutineRule.resumeDispatcher()

        assertThat(viewModel.shows.value, `is`(Resource.error(errorMsg)))
    }
}