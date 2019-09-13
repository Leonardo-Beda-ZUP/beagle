package br.com.zup.beagleui.framework.ui

import br.com.zup.beagleui.framework.core.NativeWidget
import br.com.zup.beagleui.framework.core.Widget

data class DropDown(
    val header: Widget,
    val child: Widget
) : NativeWidget()