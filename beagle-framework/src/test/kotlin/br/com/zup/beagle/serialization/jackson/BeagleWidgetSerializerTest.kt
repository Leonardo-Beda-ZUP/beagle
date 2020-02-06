package br.com.zup.beagle.serialization.jackson

import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.ComposeWidget
import br.com.zup.beagle.widget.core.Widget
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text
import com.fasterxml.jackson.core.JsonGenerator
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

private const val TYPE = "_beagleType_"
private const val APP_NAME = "Test"

class BeagleWidgetSerializerTest {

    @MockK
    private lateinit var objectFieldSerializer: ObjectFieldSerializer
    @MockK
    private lateinit var jsonGenerator: JsonGenerator

    private lateinit var beagleWidgetSerializer: BeagleWidgetSerializer

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        beagleWidgetSerializer = BeagleWidgetSerializer()

        every { jsonGenerator.writeStartObject() } just Runs
        every { jsonGenerator.writeStringField(any(), any()) } just Runs
        every { jsonGenerator.writeObjectField(any(), any()) } just Runs
        every { jsonGenerator.writeEndObject() } just Runs
        every { jsonGenerator.writeObject(any()) } just Runs
        every { objectFieldSerializer.serializeFields(any(), any()) } just Runs
    }

    @Test
    fun serialize_should_serialize_a_Widget() {
        // Given
        val textValue = "Hello"
        val widget = Text(textValue)

        // When
        beagleWidgetSerializer.serialize(widget, jsonGenerator, null)

        // Then
        verify(exactly = 1) { jsonGenerator.writeStartObject() }
        verify(exactly = 1) { jsonGenerator.writeStringField(TYPE, "beagle:widget:text") }
        verify(exactly = 1) { objectFieldSerializer.serializeFields(widget, jsonGenerator) }
        verify(exactly = 1) { jsonGenerator.writeEndObject() }
    }

    @Test
    fun serialize_should_serialize_a_CustomWidget() {
        // Given
        val testValue = "Hello"
        val widget = CustomWidget(testValue)

        // When
        beagleWidgetSerializer.serialize(widget, jsonGenerator, null)

        // Then
        verify(exactly = 1) { jsonGenerator.writeStartObject() }
        verify(exactly = 1) { jsonGenerator.writeStringField(TYPE, "beagle:widget:button") }
        verify(exactly = 1) { objectFieldSerializer.serializeFields(widget.build(), jsonGenerator) }
        verify(exactly = 1) { jsonGenerator.writeEndObject() }
    }

    @Test
    fun serialize_should_serialize_a_CustomNativeWidget() {
        // Given
        val widget = CustomNativeWidget()

        // When
        beagleWidgetSerializer.serialize(widget, jsonGenerator, null)

        // Then
        verify(exactly = 1) { jsonGenerator.writeStartObject() }
        verify(exactly = 1) { jsonGenerator.writeStringField(TYPE, "$APP_NAME:widget:customnativewidget") }
        verify(exactly = 1) { objectFieldSerializer.serializeFields(widget, jsonGenerator) }
        verify(exactly = 1) { jsonGenerator.writeEndObject() }
    }
}

class CustomWidget(
    private val value: String
) : ComposeWidget {
    override fun build(): Widget = Button(value)
}

@RegisterWidget
class CustomNativeWidget : Widget
