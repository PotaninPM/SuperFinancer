package com.potaninpm.data.remote.dto.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubProfileDto(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?
)
