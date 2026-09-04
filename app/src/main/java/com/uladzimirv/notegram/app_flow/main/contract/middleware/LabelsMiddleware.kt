package com.uladzimirv.notegram.app_flow.main.contract.middleware

import com.uladzimirv.notegram.app_flow.main.contract.ApplicationMiddleware
import com.uladzimirv.notegram.app_flow.main.contract.ApplicationViewState
import com.uladzimirv.notegram.domain.model.label.LabelId
import com.uladzimirv.notegram.ui.layout.main.com.LabelColorPref
import com.uladzimirv.notegram.ui.model.LabelUI


sealed interface LabelsMiddleware : ApplicationMiddleware {
    override fun reduce(viewState: ApplicationViewState): ApplicationViewState {
        return when (this) {
            is Show -> viewState.copy(
                labelsState = viewState.labelsState.copy(
                    show = show
                )
            )

            is SelectLabel -> {
                val label = viewState.labelsState.labels.find { it.id == labelId }
                if (label == null) viewState.copy(
                    labelsState = viewState.labelsState.copy(
                        label = LabelUI.empty()
                    )
                ) else viewState.copy(
                    labelsState = viewState.labelsState.copy(
                        label = label
                    )
                )
            }

            DropLabel -> viewState.copy(
                labelsState = viewState.labelsState.copy(
                    label = null
                )
            )

            is EditColorPref -> viewState.copy(
                labelsState = viewState.labelsState.copy(
                    label = viewState.labelsState.label?.copy(
                        colorPref = colorPref
                    )
                )
            )

            is EditName -> viewState.copy(
                labelsState = viewState.labelsState.copy(
                    label = viewState.labelsState.label?.copy(
                        name = name
                    )
                )
            )
        }
    }

    data object DropLabel : LabelsMiddleware

    data class Show(
        val show: Boolean
    ) : LabelsMiddleware

    data class SelectLabel(
        val labelId: LabelId?
    ) : LabelsMiddleware

    data class EditName(
        val name: String
    ) : LabelsMiddleware

    data class EditColorPref(
        val colorPref: LabelColorPref
    ) : LabelsMiddleware

}
