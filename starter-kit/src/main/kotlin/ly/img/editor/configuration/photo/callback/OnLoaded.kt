package ly.img.editor.configuration.photo.callback

import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.core.component.data.Insets
import ly.img.editor.core.state.EditorViewMode

suspend fun PhotoConfigurationBuilder.onLoaded() {
    coroutineScope {
        launch { observeEditorViewMode() }
        launch {
            observeEditorEditMode(
                extraInsets = { Insets(value = if (it == "Crop") 24.dp else 0.dp) },
            ) { editMode ->
                editorContext.engine.editor.setSettingBoolean(
                    keypath = "page/allowResizeInteraction",
                    value = editMode == "Crop",
                )
            }
        }
    }
}

suspend fun PhotoConfigurationBuilder.observeEditorViewMode() {
    editorContext.state
        .distinctUntilChangedBy { it.viewMode to it.insets }
        .filter { it.viewMode is EditorViewMode.Preview }
        .collect { state ->
            editorContext.engine.scene.immediateZoomToBlock(
                block = requireNotNull(editorContext.engine.scene.getCurrentPage()),
                paddingLeft = state.insets.left.value,
                paddingTop = state.insets.top.value,
                paddingRight = state.insets.right.value,
                paddingBottom = 0F,
                forceUpdate = true,
            )
        }
}
