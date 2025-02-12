package com.potaninpm.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesArticleResponseDto(
    @SerializedName("response") val response: NYTimesResponse
)
