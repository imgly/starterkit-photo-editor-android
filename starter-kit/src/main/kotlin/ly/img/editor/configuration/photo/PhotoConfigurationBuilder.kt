package ly.img.editor.configuration.photo

import androidx.compose.runtime.Stable
import ly.img.editor.BasicConfigurationBuilder
import ly.img.editor.configuration.photo.callback.onCreate
import ly.img.editor.configuration.photo.callback.onExport
import ly.img.editor.configuration.photo.callback.onLoaded
import ly.img.editor.configuration.photo.component.rememberCanvasMenu
import ly.img.editor.configuration.photo.component.rememberDock
import ly.img.editor.configuration.photo.component.rememberInspectorBar
import ly.img.editor.configuration.photo.component.rememberNavigationBar
import ly.img.editor.configuration.photo.component.rememberOverlay
import ly.img.editor.core.EditorScope
import ly.img.editor.core.ScopedProperty
import ly.img.editor.core.component.EditorComponent

@Stable
class PhotoConfigurationBuilder : BasicConfigurationBuilder() {
    override var onCreate: (suspend EditorScope.() -> Unit)? = {
        onCreate()
    }

    override var onLoaded: (suspend EditorScope.() -> Unit)? = {
        onLoaded()
    }

    override var onExport: (suspend EditorScope.() -> Unit)? = {
        onExport()
    }

    override var onClose: (suspend EditorScope.() -> Unit)? = {
        showConfirmationOrCloseEditor()
    }

    override var onError: (suspend EditorScope.(Throwable) -> Unit)? = {
        error = it
    }

    override var dock: ScopedProperty<EditorScope, EditorComponent<*>>? = {
        rememberDock()
    }

    override var navigationBar: ScopedProperty<EditorScope, EditorComponent<*>>? = {
        rememberNavigationBar()
    }

    override var inspectorBar: ScopedProperty<EditorScope, EditorComponent<*>>? = {
        rememberInspectorBar()
    }

    override var canvasMenu: ScopedProperty<EditorScope, EditorComponent<*>>? = {
        rememberCanvasMenu()
    }

    override var overlay: ScopedProperty<EditorScope, EditorComponent<*>>? = {
        rememberOverlay()
    }
}
