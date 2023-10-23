package com.android.boilerplate.model.repository.feedback

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

/**
 * @author Abdul Rahman
 */
class FeedbackRepositoryImp @Inject constructor() : FeedbackRepository {

    private val improveDesign = MutableLiveData(false)
    private val improveExperience = MutableLiveData(false)
    private val improveFunctionality = MutableLiveData(false)
    private val improvePerformance = MutableLiveData(false)

    override fun getImproveDesign(): MutableLiveData<Boolean> = improveDesign

    override fun getImproveExperience(): MutableLiveData<Boolean> = improveExperience

    override fun getImproveFunctionality(): MutableLiveData<Boolean> = improveFunctionality

    override fun getImprovePerformance(): MutableLiveData<Boolean> = improvePerformance

    override fun setImproveDesign(value: Boolean) = improveDesign.postValue(value)

    override fun setImproveExperience(value: Boolean) = improveExperience.postValue(value)

    override fun setImproveFunctionality(value: Boolean) = improveFunctionality.postValue(value)

    override fun setImprovePerformance(value: Boolean) = improvePerformance.postValue(value)
}