package com.android.boilerplate.model.data.remote

/**
 * @author Abdul Rahman
 */
object NetworkParams {
    // network config
    const val CONNECT_TIMEOUT: Long = 300
    const val WRITE_TIMEOUT: Long = 300
    const val READ_TIMEOUT: Long = 300

    // network request headers
    const val ACCEPT: String = "Accept"
    const val CONTENT_TYPE: String = "Content-Type"
    const val APPLICATION_JSON: String = "application/json"
    const val AUTHORIZATION: String = "Authorization"
    const val BEARER: String = "Bearer "

    // network request params
    // booked inspections
    const val MECHANIC_ID: String = "mechanicId"
    const val JOB_STATUS: String = "jobStatus"
    const val PAGE_NUMBER: String = "pageNumber"
    const val MEDIA_TYPE: String = "mediaType"
    const val INSPECTION_ID: String = "inspectionId"
    const val CATEGORY_ID: String = "categoryId"
    const val DATA: String = "data"
}