package com.android.boilerplate.viewmodel.feedback

import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.repository.feedback.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
@HiltViewModel
class FeedbackViewModel @Inject constructor(private val repository: FeedbackRepository) :
    BaseViewModel() {

    val improveDesign = repository.getImproveDesign()
    val improveExperience = repository.getImproveExperience()
    val improveFunctionality = repository.getImproveFunctionality()
    val improvePerformance = repository.getImprovePerformance()

    fun setImproveDesign(value: Boolean) = repository.setImproveDesign(value)

    fun setImproveExperience(value: Boolean) = repository.setImproveExperience(value)

    fun setImproveFunctionality(value: Boolean) = repository.setImproveFunctionality(value)

    fun setImprovePerformance(value: Boolean) = repository.setImprovePerformance(value)
}