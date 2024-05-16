package com.capstone.jachulsa.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class NaverRes @JsonCreator constructor(
        @JsonProperty("resultcode") val resultCode: String,
        @JsonProperty("message") val message: String,
        @JsonProperty("response") val response: Response
) {
    data class Response @JsonCreator constructor(
            @JsonProperty("id") val id: String,
            @JsonProperty("nickname") val nickname: String,
            @JsonProperty("age") val age: String,
            @JsonProperty("gender") val gender: String,
            @JsonProperty("email") val email: String,
            @JsonProperty("name") val name: String,
            @JsonProperty("profile_image") val profileImage: String?,
            @JsonProperty("birthday") val birthday: String,
            @JsonProperty("birthyear") val birthyear: String
    )
}