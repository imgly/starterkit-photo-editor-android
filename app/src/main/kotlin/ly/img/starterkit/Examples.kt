package ly.img.starterkit

import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import ly.img.editor.Editor
import ly.img.editor.EditorUiMode
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.configuration.photo.callback.getOrCreateSceneFromImage
import ly.img.editor.configuration.photo.callback.onCreate
import ly.img.editor.configuration.photo.callback.onPostCreateScene
import ly.img.editor.core.component.data.ForceCropConfiguration
import ly.img.editor.core.component.data.ForceCropMode
import ly.img.editor.core.component.data.ForceCropPresetCandidate
import ly.img.editor.core.configuration.EditorConfiguration
import ly.img.editor.core.configuration.remember
import ly.img.editor.core.engine.EngineRenderTarget
import ly.img.editor.core.event.EditorEvent

@Composable
private fun ExampleInlineModification(onClose: (error: Throwable?) -> Unit) {
    // highlight-starter-kit-inline-modification
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder) {
                onCreate = {
                    onCreate(
                        createScene = {
                            getOrCreateSceneFromImage(
                                imageUri = "https://cdn.img.ly/assets/demo/v3/ly.img.image/images/sample_2.jpg".toUri(),
                            )
                        },
                    )
                }
            }
        },
        onClose = onClose,
    )
    // highlight-starter-kit-inline-modification
}

@Composable
private fun ExampleForceCrop(onClose: (error: Throwable?) -> Unit) {
    // highlight-starter-kit-force-crop
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder) {
                onCreate = {
                    onCreate(
                        postCreateScene = {
                            // Either modify the content of OnCreate.kt directly or make inline modification like here.
                            onPostCreateScene()
                            // Apply force crop
                            val configuration = ForceCropConfiguration(
                                sourceId = "ly.img.crop.presets",
                                presetId = "aspect-ratio-1-1",
                                presetCandidates = listOf(
                                    ForceCropPresetCandidate(
                                        sourceId = "ly.img.crop.presets",
                                        presetId = "aspect-ratio-16-9",
                                    ),
                                    ForceCropPresetCandidate(
                                        sourceId = "ly.img.crop.presets",
                                        presetId = "aspect-ratio-9-16",
                                    ),
                                ),
                                mode = ForceCropMode.IfNeeded(threshold = 0.01f),
                            )
                            editorContext.eventHandler.send(
                                event = EditorEvent.ApplyForceCrop(
                                    designBlock = requireNotNull(editorContext.engine.scene.getCurrentPage()),
                                    configuration = configuration,
                                ),
                            )
                        },
                    )
                }
            }
        },
        onClose = onClose,
    )
    // highlight-starter-kit-force-crop
}

@Composable
private fun ExampleBaseUri(onClose: (error: Throwable?) -> Unit) {
    // highlight-starter-kit-base-uri
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        baseUri = "file:///android_asset".toUri(), // this points to android assets
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder)
        },
        onClose = onClose,
    )
    // highlight-starter-kit-base-uri
}

@Composable
private fun ExampleUIMode(onClose: (error: Throwable?) -> Unit) {
    // highlight-starter-kit-ui-mode
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        uiMode = EditorUiMode.SYSTEM, // EditorUiMode.SYSTEM, EditorUiMode.LIGHT, EditorUiMode.DARK
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder)
        },
        onClose = onClose,
    )
    // highlight-starter-kit-ui-mode
}

@Composable
private fun ExampleEngineRenderTarget(onClose: (error: Throwable?) -> Unit) {
    // highlight-starter-kit-engine-render-target
    Editor(
        license = null, // pass null or empty for evaluation mode with watermark
        engineRenderTarget = EngineRenderTarget.SURFACE_VIEW, // EngineRenderTarget.SURFACE_VIEW, EngineRenderTarget.TEXTURE_VIEW
        configuration = {
            EditorConfiguration.remember(::PhotoConfigurationBuilder)
        },
        onClose = onClose,
    )
    // highlight-starter-kit-engine-render-target
}
