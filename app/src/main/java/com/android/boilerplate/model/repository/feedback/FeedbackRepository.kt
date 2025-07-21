package com.android.boilerplate.model.repository.feedback

import androidx.lifecycle.LiveData

/**
 * @author Abdul Rahman
 */
interface FeedbackRepository {

    fun getImproveDesign(): LiveData<Boolean>

    fun getImproveExperience(): LiveData<Boolean>

    fun getImproveFunctionality(): LiveData<Boolean>

    fun getImprovePerformance(): LiveData<Boolean>

    fun setImproveDesign(value: Boolean)

    fun setImproveExperience(value: Boolean)

    fun setImproveFunctionality(value: Boolean)

    fun setImprovePerformance(value: Boolean)
}