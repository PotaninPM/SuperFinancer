package com.potaninpm.data.remote.dto.home.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesResponse(
    @SerializedName("docs") val docs: List<NYTimesArticleDto>
)
