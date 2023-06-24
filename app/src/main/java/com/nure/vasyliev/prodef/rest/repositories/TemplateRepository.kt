package com.nure.vasyliev.prodef.rest.repositories

import com.nure.vasyliev.prodef.model.template.Templates
import com.nure.vasyliev.prodef.rest.ApiClient
import com.nure.vasyliev.prodef.rest.apis.TemplateApi
import com.nure.vasyliev.prodef.rest.payloads.template.TemplatePayload

class TemplateRepository {

    suspend fun getAllUserTemplates(userId: String): Templates {
        val apiService = ApiClient.create().create(TemplateApi::class.java)
        val response = apiService.getAllTemplates(userId)

        return if (response.isSuccessful) {
            response.body() ?: Templates(listOf())
        } else if (response.code() == 401) {
            throw RuntimeException("Invalid token")
        } else {
            Templates(listOf())
        }
    }

    suspend fun createTemplate(userId: String, payload: TemplatePayload) {
        val apiService = ApiClient.create().create(TemplateApi::class.java)
        val response = apiService.createTemplate(userId, payload)

        if (!response.isSuccessful) {
            throw RuntimeException("Invalid token")
        }
    }

    suspend fun updateTemplate(templateId: String, payload: TemplatePayload) {
        val apiService = ApiClient.create().create(TemplateApi::class.java)
        val response = apiService.updateTemplate(templateId, payload)

        if (!response.isSuccessful) {
            throw RuntimeException("Invalid token")
        }
    }

    suspend fun deleteTemplate(templateId: String) {
        val apiService = ApiClient.create().create(TemplateApi::class.java)
        val response = apiService.deleteTemplate(templateId)

        if (!response.isSuccessful) {
            throw RuntimeException("Invalid token")
        }
    }
}