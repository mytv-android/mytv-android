package top.yogiczy.mytv.core.data.entities.subtitle

import android.graphics.Color
import androidx.compose.runtime.Immutable
import androidx.media3.ui.CaptionStyleCompat
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind

/**
 * 频道节目列表
 */
@Serializable
data class VideoPlayerSubtitleStyle(
    val textSize: Float = 10f,
    @Serializable(with = CaptionStyleCompatSerializer::class)
    val style: CaptionStyleCompat = CaptionStyleCompat(
                Color.WHITE,
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                Color.TRANSPARENT,
                null
            ),
) {
    companion object {
        val EXAMPLE = VideoPlayerSubtitleStyle(
            textSize = 10f,
            style = CaptionStyleCompat(
                Color.WHITE,
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                Color.TRANSPARENT,
                null
            )
        )
    }
}

object CaptionStyleCompatSerializer : KSerializer<CaptionStyleCompat> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("CaptionStyleCompat") {
        element("foregroundColor", PrimitiveSerialDescriptor("foregroundColor", PrimitiveKind.INT))
        element("backgroundColor", PrimitiveSerialDescriptor("backgroundColor", PrimitiveKind.INT))
        element("windowColor", PrimitiveSerialDescriptor("windowColor", PrimitiveKind.INT))
        element("edgeType", PrimitiveSerialDescriptor("edgeType", PrimitiveKind.INT))
        element("edgeColor", PrimitiveSerialDescriptor("edgeColor", PrimitiveKind.INT))
    }

    override fun serialize(encoder: Encoder, value: CaptionStyleCompat) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.foregroundColor)
            encodeIntElement(descriptor, 1, value.backgroundColor)
            encodeIntElement(descriptor, 2, value.windowColor)
            encodeIntElement(descriptor, 3, value.edgeType)
            encodeIntElement(descriptor, 4, value.edgeColor)
        }
    }

    override fun deserialize(decoder: Decoder): CaptionStyleCompat {
        return decoder.decodeStructure(descriptor) {
            var foregroundColor = 0
            var backgroundColor = 0
            var windowColor = 0
            var edgeType = 0
            var edgeColor = 0

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> foregroundColor = decodeIntElement(descriptor, 0)
                    1 -> backgroundColor = decodeIntElement(descriptor, 1)
                    2 -> windowColor = decodeIntElement(descriptor, 2)
                    3 -> edgeType = decodeIntElement(descriptor, 3)
                    4 -> edgeColor = decodeIntElement(descriptor, 4)
                    else -> break
                }
            }

            CaptionStyleCompat(
                foregroundColor,
                backgroundColor,
                windowColor,
                edgeType,
                edgeColor,
                null // 字体类型可以设置为 null
            )
        }
    }
}