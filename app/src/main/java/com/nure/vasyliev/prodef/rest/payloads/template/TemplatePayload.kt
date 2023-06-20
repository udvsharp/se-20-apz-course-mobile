package com.nure.vasyliev.prodef.rest.payloads.template

import com.google.gson.annotations.SerializedName

data class TemplatePayload(
    @SerializedName("durationMins") val durationMins: Int
)