package com.android.boilerplate.model.repository.feedback

import androidx.lifecycle.MutableLiveData

/**
 * @author Abdul Rahman
 */
interface FeedbackRepository {

    fun getImproveDesign(): MutableLiveData<Boolean>

    fun getImproveExperience(): MutableLiveData<Boolean>

    fun getImproveFunctionality(): MutableLiveData<Boolean>

    fun getImprovePerformance(): MutableLiveData<Boolean>

    fun setImproveDesign(value: Boolean)

    fun setImproveExperience(value: Boolean)

    fun setImproveFunctionality(value: Boolean)

    fun setImprovePerformance(value: Boolean)
}