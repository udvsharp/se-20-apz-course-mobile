package com.nure.vasyliev.prodef.model.template

import com.google.gson.annotations.SerializedName

data class Templates(
    @SerializedName("templates") val templates: List<Template>
)