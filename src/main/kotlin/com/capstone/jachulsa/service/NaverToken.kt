package com.capstone.jachulsa.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class NaverToken @JsonCreator constructor(
        @JsonProperty("access_token") val access_token: String,
        @JsonProperty("refresh_token") val refresh_token: String?,
        @JsonProperty("token_type") val token_type: String,
        @JsonProperty("expires_in") val expires_in: String
)