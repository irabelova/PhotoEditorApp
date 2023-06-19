package com.example.photoeditorapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.photoeditorapp.editPhoto.EditPhotoViewModel
import com.example.photoeditorapp.editPhoto.EditType
import com.example.photoeditorapp.editPhoto.drawing.DrawingOptions
import com.example.photoeditorapp.editPhoto.text.TextModel
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
internal class EditPhotoViewModelTest {

    private lateinit var viewModel: EditPhotoViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var filter: PhotoFilter

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = EditPhotoViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState() {
        assertEquals(EditType.COMMON, viewModel.editType.value)
        assertEquals(DrawingOptions(), viewModel.drawingOptions.value)
        assertFalse(viewModel.isShowSaveButton())
        assertNull(viewModel.filter.value)
        assertNull(viewModel.textOptions.value)
    }

    @Test
    fun setFilter() {
        viewModel.setFilter(filter)
        assertEquals(filter, viewModel.filter.value)
        assertTrue(viewModel.isShowSaveButton())
    }

    @Test
    fun changeBrushColor() {
        val newColor = 473264783
        viewModel.changeBrushColor(newColor)
        assertEquals(DrawingOptions().copy(color = newColor), viewModel.drawingOptions.value)
    }

    @Test
    fun changeBrushSize() {
        val newSize = 77
        viewModel.changeBrushSize(newSize)
        assertEquals(DrawingOptions().copy(brushSize = newSize.toFloat()), viewModel.drawingOptions.value)
    }

    @Test
    fun `change text properties without confirm`() {
        val newColor = 473264783
        val newSize = 77
        val newText = "Test"
        viewModel.changeText(newText)
        viewModel.changeTextColor(newColor)
        viewModel.changeTextSize(newSize)
        assertNull(viewModel.textOptions.value)
    }

    @Test
    fun `change text properties with confirm`() {
        val newColor = 473264783
        val newSize = 77
        val newText = "Test"
        viewModel.changeText(newText)
        viewModel.changeTextColor(newColor)
        viewModel.changeTextSize(newSize)
        viewModel.confirmTextChange()
        assertEquals(TextModel(newText, newColor, newSize+15.toFloat()), viewModel.textOptions.value)
    }

    @Test
    fun setEditType() {
        viewModel.setEditType(EditType.DRAW)
        assertEquals(EditType.DRAW, viewModel.editType.value)
    }

    @Test
    fun applyChanges() {
        val newColor = 473264783
        val newSize = 77
        val newText = "Test"
        viewModel.setEditType(EditType.TEXT)
        viewModel.changeText(newText)
        viewModel.changeTextColor(newColor)
        viewModel.changeTextSize(newSize)
        viewModel.applyChanges()
        assertEquals(EditType.COMMON, viewModel.editType.value)
        assertEquals(TextModel(newText, newColor, newSize+15.toFloat()), viewModel.textOptions.value)
    }

    @Test
    fun incrementChanges() {
        viewModel.incrementChanges()
        assertTrue(viewModel.isShowSaveButton())
    }

    @Test
    fun `undo changes count is 0`() {
        viewModel.undo()
        assertNull(viewModel.undo.value)
    }

    @Test
    fun `undo changes count is not 0`() {
        viewModel.incrementChanges()
        viewModel.undo()
        assertFalse(viewModel.isShowSaveButton())
        assertEquals(1, viewModel.undo.value)
    }

    @Test
    fun cancelChanges() {
        viewModel.setEditType(EditType.DRAW)
        viewModel.incrementChanges()
        viewModel.cancelChanges()
        assertFalse(viewModel.isShowSaveButton())
        assertEquals(1, viewModel.undo.value)
        assertEquals(EditType.COMMON, viewModel.editType.value)
    }
}