package ly.img.editor.configuration.photo.component

import androidx.compose.runtime.Composable
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.core.component.EditorComponent
import ly.img.editor.core.component.remember

@Composable
fun PhotoConfigurationBuilder.rememberOverlay() = EditorComponent.remember {
    decoration = { Overlay() }
}
