package com.potaninpm.data.remote.dto.home.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubProfileDto(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?
)
