@file:Suppress("UnusedReceiverParameter")

package ly.img.editor.configuration.photo.callback

import android.net.Uri
import android.util.SizeF
import androidx.core.net.toUri
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ly.img.editor.configuration.photo.PhotoConfigurationBuilder
import ly.img.editor.core.library.data.TextAssetSource
import ly.img.editor.core.library.data.TypefaceProvider
import ly.img.engine.DefaultAssetSource
import ly.img.engine.DemoAssetSource
import ly.img.engine.DesignBlock
import ly.img.engine.SceneLayout
import ly.img.engine.populateAssetSource

suspend fun PhotoConfigurationBuilder.onCreate(
    preCreateScene: suspend PhotoConfigurationBuilder.() -> Unit = {
        onPreCreateScene()
    },
    createScene: suspend PhotoConfigurationBuilder.() -> Unit = {
        onCreateScene()
    },
    loadAssetSources: suspend PhotoConfigurationBuilder.() -> Unit = {
        onLoadAssetSources()
    },
    postCreateScene: suspend PhotoConfigurationBuilder.() -> Unit = {
        onPostCreateScene()
    },
    finally: suspend PhotoConfigurationBuilder.() -> Unit = {
        onCreateFinally()
    },
) {
    try {
        preCreateScene()
        createScene()
        loadAssetSources()
        postCreateScene()
    } finally {
        finally()
    }
}

fun PhotoConfigurationBuilder.onPreCreateScene() {
    showLoading = true
    editorContext.engine.editor.setSettingBoolean(keypath = "page/moveChildrenWhenCroppingFill", value = true)
    editorContext.engine.editor.setSettingBoolean(keypath = "page/selectWhenNoBlocksSelected", value = true)
    editorContext.engine.editor.setSettingBoolean(keypath = "page/highlightWhenCropping", value = true)
    editorContext.engine.editor.setSettingBoolean(keypath = "doubleClickToCropEnabled", value = false)
}

suspend fun PhotoConfigurationBuilder.onCreateScene() {
    getOrCreateSceneFromImage("https://cdn.img.ly/assets/demo/v3/ly.img.image/images/sample_1.jpg".toUri())
}

suspend fun PhotoConfigurationBuilder.getOrCreateSceneFromImage(
    imageUri: Uri,
    size: SizeF? = null,
    dpi: Float = 300F,
    pixelScaleFactor: Float = 1F,
    sceneLayout: SceneLayout = SceneLayout.FREE,
): DesignBlock = editorContext.engine.scene.get() ?: editorContext.engine.scene.createFromImage(
    imageUri = imageUri,
    dpi = dpi,
    pixelScaleFactor = pixelScaleFactor,
    sceneLayout = sceneLayout,
).also {
    size?.let {
        val page = requireNotNull(editorContext.engine.scene.getCurrentPage())
        editorContext.engine.block.setWidth(block = page, value = size.width)
        editorContext.engine.block.setHeight(block = page, value = size.height)
    }
}

suspend fun PhotoConfigurationBuilder.onLoadAssetSources() {
    // Load asset sources in parallel from content.json files
    coroutineScope {
        listOf(
            DefaultAssetSource.STICKER.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.VECTOR_PATH.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.FILTER_LUT.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.FILTER_DUO_TONE.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.CROP_PRESETS.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.PAGE_PRESETS.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.EFFECT.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.BLUR.key to defaultAssetSourceBaseUri,
            DefaultAssetSource.TYPEFACE.key to defaultAssetSourceBaseUri,
            DemoAssetSource.TEXT_COMPONENTS.key to demoAssetSourceBaseUri,
        ).forEach { (assetSource, basePath) ->
            launch {
                editorContext.engine.populateAssetSource(
                    id = assetSource,
                    jsonUri = "$basePath/$assetSource/content.json".toUri(),
                    replaceBaseUri = basePath,
                )
            }
        }
    }

    // Register text asset source
    TypefaceProvider().provideTypeface(
        engine = editorContext.engine,
        name = "Roboto",
    )?.let {
        val textAssetSource = TextAssetSource(engine = editorContext.engine, typeface = it)
        editorContext.engine.asset.addSource(textAssetSource)
    }
}

fun PhotoConfigurationBuilder.onPostCreateScene() {
    // Do nothing
}

fun PhotoConfigurationBuilder.onCreateFinally() {
    showLoading = false
}
