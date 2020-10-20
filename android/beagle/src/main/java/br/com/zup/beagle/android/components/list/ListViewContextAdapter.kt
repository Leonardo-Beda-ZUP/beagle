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

package br.com.zup.beagle.android.components.list

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.ActionStatus
import br.com.zup.beagle.android.action.AsyncAction
import br.com.zup.beagle.android.context.ContextActionExecutor
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.setIsAutoGenerateIdEnabled
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.OnInitiableWidget
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

internal class ListViewContextAdapter(
    val template: ServerDrivenComponent,
    val iteratorName: String,
    val key: String? = null,
    val viewFactory: ViewFactory,
    val rootView: RootView
) : RecyclerView.Adapter<ContextViewHolder>() {

    // ViewModels to manage ids and contexts
    private val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
    private val listViewIdViewModel = rootView.generateViewModelInstance<ListViewIdViewModel>()
    private val generateIdViewModel = rootView.generateViewModelInstance<GenerateIdViewModel>()

    // Recyclerview id for post config changes id management
    private var recyclerId = View.NO_ID

    private var parentListViewSuffix: String? = null

    // Serializer to provide new template instances
    private val serializer = BeagleSerializer()

    // Items captured by ListView
    private var listItems: List<Any> = mutableListOf()

    // Struct that holds all data of each item
    private var adapterItems = listOf<BeagleAdapterItem>()

    // ViewHolders who called onInit but did not finish
    private val onInitiableWidgetsOnHolders = mutableMapOf<ContextViewHolder, MutableList<OnInitiableWidget>>()

    // Struct to manage recycled ViewHolders
    private val recycledViewHolders = mutableListOf<ContextViewHolder>()

    // Struct to manage created ViewHolders
    private val createdViewHolders = mutableListOf<ContextViewHolder>()

    // Each access generate a new instance of the template to avoid reference conflict
    private val templateJson = serializer.serializeComponent(template)

    init {
        ContextActionExecutor.asyncActionExecuted.observe(rootView.getLifecycleOwner(), {
            manageIfInsideRecyclerView(it.origin, it.asyncAction)
        })
    }

    private fun manageIfInsideRecyclerView(origin: View, asyncAction: AsyncAction) {
        if (origin.parent == null) {
            return
        }
        (origin.parent as? RecyclerView)?.let { recyclerView ->
            val adapter = recyclerView.adapter as? ListViewContextAdapter
            adapter?.markViewToNotRecycle(origin, asyncAction)
            return
        } ?: (origin.parent as? View)?.let { parent ->
            manageIfInsideRecyclerView(parent, asyncAction)
        }
    }

    private fun markViewToNotRecycle(viewCallingAsyncAction: View, asyncAction: AsyncAction) {
        createdViewHolders.forEach {
            val holderFound = viewCallingAsyncAction == it.itemView
            if (holderFound) {
                asyncAction.status.observe(rootView.getLifecycleOwner(), { actionStatus ->
                    if (actionStatus == ActionStatus.STARTED) {
                        it.setIsRecyclable(false)
                    } else if (actionStatus == ActionStatus.FINISHED) {
                        it.setIsRecyclable(true)
                        adapterItems[it.adapterPosition].completelyInitialized = it.isRecyclable
                    }
                })
                return
            }
        }
    }

    fun setParentSuffix(itemSuffix: String) {
        parentListViewSuffix = itemSuffix
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContextViewHolder {
        val newTemplate = serializer.deserializeComponent(templateJson)
        val view = generateView(newTemplate)
        val viewHolder = ContextViewHolder(
            view,
            newTemplate,
            serializer,
            viewModel,
            listViewIdViewModel,
            templateJson,
            iteratorName
        )
        createdViewHolders.add(viewHolder)
        return viewHolder
    }

    private fun generateView(newTemplate: ServerDrivenComponent) = viewFactory.makeBeagleFlexView(rootView).apply {
        setIsAutoGenerateIdEnabled(false)
        addServerDrivenComponent(newTemplate, false)
        setWidthAutoAndDirtyAllViews()
    }

    override fun onBindViewHolder(holder: ContextViewHolder, position: Int) {
        val isRecycled = recycledViewHolders.contains(holder)
        // Handle context, ids and direct nested adapters
        holder.onBind(parentListViewSuffix, key, adapterItems[position], isRecycled, position, recyclerId)
        // Handle widgets with onInit
        if (!adapterItems[position].completelyInitialized) {
            handleInitiableWidgets(holder, isRecycled)
        }
    }

    private fun handleInitiableWidgets(holder: ContextViewHolder, shouldRerunOnInit: Boolean = false) {
        // For each OnInitiableWidget
        holder.initiableWidgets.forEach { widget ->
            // Add widget to list of widgets with onInit running
            if (!onInitiableWidgetsOnHolders.containsKey(holder)) {
                onInitiableWidgetsOnHolders[holder] = mutableListOf()
            }
            onInitiableWidgetsOnHolders[holder]?.add(widget)
            // When the view is recycled we must call onInit again
            if (shouldRerunOnInit) {
                widget.executeOnInit()
            }
        }
    }

    override fun onViewRecycled(holder: ContextViewHolder) {
        super.onViewRecycled(holder)
        recycledViewHolders.add(holder)
        // Removes the ids of each view previously set to receive new ones
        clearIds(holder.itemView)
        // Iterate over the ImageViews inside each holder and release the downloaded resources
        // before the new image is set
        holder.directNestedImageViews.forEach {
            it.setImageDrawable(null)
        }
    }

    private fun clearIds(view: View) {
        view.id = View.NO_ID
        if (view is ViewGroup) {
            view.children.forEach {
                clearIds(it)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ContextViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.directNestedTextViews.forEach {
            it.requestLayout()
        }
    }

    fun setList(list: List<Any>?, recyclerId: Int) {
        list?.let {
            if (list != listItems) {
                clearAdapterContent()
                setRecyclerId(recyclerId)
                listItems = list
                adapterItems = list.map { BeagleAdapterItem(data = it.normalizeContextValue()) }
                notifyDataSetChanged()
            }
        } ?: clearList()
    }

    private fun clearAdapterContent() {
        adapterItems = emptyList()
        onInitiableWidgetsOnHolders.clear()
        recycledViewHolders.clear()
        createdViewHolders.clear()
    }

    private fun setRecyclerId(incomingRecyclerId: Int) {
        val recyclerIdToUse = when {
            incomingRecyclerId != View.NO_ID -> incomingRecyclerId
            recyclerId != View.NO_ID -> recyclerId
            else -> generateIdViewModel.getViewId(rootView.getParentId())
        }
        recyclerId = listViewIdViewModel.createSingleManagerByListViewId(recyclerIdToUse, listItems.isEmpty())
    }

    private fun clearList() {
        val initialSize = adapterItems.size
        clearAdapterContent()
        notifyItemRangeRemoved(0, initialSize)
    }

    override fun getItemCount() = adapterItems.size
}