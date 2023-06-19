package com.example.photoeditorapp

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.photoeditorapp.mainMenu.MainMenuViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainMenuViewModelTest{

    @Mock
    private lateinit var uri: Uri

    private lateinit var viewModel: MainMenuViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()



    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = MainMenuViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun setUri() {
        viewModel.setUri(uri)
        assertEquals(uri, viewModel.uri.value)
    }

    @Test
    fun `setTmpUri without confirm`() {
        viewModel.setTmpUri(uri)
        assertEquals(null, viewModel.uri.value)
    }

    @Test
    fun `setTmpUri with confirm`() {
        viewModel.setTmpUri(uri)
        viewModel.confirmTmpUri()
        assertEquals(uri, viewModel.uri.value)
    }


}