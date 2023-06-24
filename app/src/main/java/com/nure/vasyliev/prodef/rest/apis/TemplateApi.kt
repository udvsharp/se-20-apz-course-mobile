package com.nure.vasyliev.prodef.rest.apis

import com.nure.vasyliev.prodef.model.template.Template
import com.nure.vasyliev.prodef.model.template.Templates
import com.nure.vasyliev.prodef.rest.payloads.template.TemplatePayload
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TemplateApi {
    @GET("api/pomodoro/templates/{userId}")
    suspend fun getAllTemplates(
        @Path("userId") userId: String
    ): Response<Templates>

    @POST("api/pomodoro/templates/{userId}")
    suspend fun createTemplate(
        @Path("userId") userId: String,
        @Body payload: TemplatePayload
    ): Response<Template>

    @PUT("api/pomodoro/templates/{templateId}")
    suspend fun updateTemplate(
        @Path("templateId") templateId: String,
        @Body payload: TemplatePayload
    ): Response<Template>

    @DELETE("api/pomodoro/templates/{templateId}")
    suspend fun deleteTemplate(
        @Path("templateId") templateId: String
    ) : Response<String>
}