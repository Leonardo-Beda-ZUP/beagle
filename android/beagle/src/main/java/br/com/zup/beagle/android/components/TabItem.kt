/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.android.components

import android.view.View
import br.com.zup.beagle.android.utils.TabViewDeprecatedConstants
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent

/**
 * Define the view has in the tab view
 *
 * @param title displays the text on the TabView component. If it is null or not declared it won't display any text.
 * @param child
 *                  inflate a view on the TabView according to the Tab item clicked.
 *                  It could receive any view (Server Driven).
 * @param icon
 *                  display an icon image on the TabView component.
 *                  If it is left as null or not declared it won't display any icon.
 *
 */
@RegisterWidget
@Deprecated(message = TabViewDeprecatedConstants.TAB_ITEM,
    replaceWith = ReplaceWith(TabViewDeprecatedConstants.TAB_ITEM_REPLACE))
data class TabItem(
    val title: String? = null,
    override val child: ServerDrivenComponent,
    val icon: ImagePath.Local? = null
) : WidgetView(), SingleChildComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView, parent: View?): View {
        return viewFactory.makeBeagleFlexView(rootView).also {
            it.addServerDrivenComponent(child, parent)
        }
    }
}
