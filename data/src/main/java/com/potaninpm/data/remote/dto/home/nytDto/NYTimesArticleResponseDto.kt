package com.potaninpm.data.remote.dto.home.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesArticleResponseDto(
    @SerializedName("response") val response: NYTimesResponse
)
