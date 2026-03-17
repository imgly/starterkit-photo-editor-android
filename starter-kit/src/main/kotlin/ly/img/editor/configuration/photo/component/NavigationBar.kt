@file:Suppress("UnusedReceiverParameter")

package ly.img.editor.configuration.photo.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.core.component.EditorTrigger
import ly.img.editor.core.component.NavigationBar
import ly.img.editor.core.component.remember
import ly.img.editor.core.component.rememberCloseEditor
import ly.img.editor.core.component.rememberExport
import ly.img.editor.core.component.rememberRedo
import ly.img.editor.core.component.rememberTogglePreviewMode
import ly.img.editor.core.component.rememberUndo

@Composable
fun PhotoConfigurationBuilder.rememberNavigationBar() = NavigationBar.remember {
    scope = {
        val historyTrigger by EditorTrigger.remember {
            editorContext.engine.editor.onHistoryUpdated()
        }
        remember(this, historyTrigger) {
            NavigationBar.Scope(parentScope = this)
        }
    }
    listBuilder = {
        NavigationBar.ListBuilder.remember {
            aligned(alignment = Alignment.Start) {
                add { NavigationBar.Button.rememberCloseEditor() }
            }
            aligned(alignment = Alignment.End) {
                add { NavigationBar.Button.rememberUndo() }
                add { NavigationBar.Button.rememberRedo() }
                add { NavigationBar.Button.rememberTogglePreviewMode() }
                add { NavigationBar.Button.rememberExport() }
            }
        }
    }
}
