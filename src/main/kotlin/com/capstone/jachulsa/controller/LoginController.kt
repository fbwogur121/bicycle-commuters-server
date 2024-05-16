package com.capstone.jachulsa.controller

import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.service.*
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URISyntaxException

@Tag(name = "Naver login API", description = "네이버로그인 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
class LoginController(
        private val loginService: LoginService
) {

    @Operation(summary = "naver user 정보 return api")
    @GetMapping()
    @ResponseBody
    @Throws(
            MalformedURLException::class,
            UnsupportedEncodingException::class,
            URISyntaxException::class
    )
    fun authLogin(
            @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {
        println("Received code: ${loginRequest.code}")
        println("Received state: ${loginRequest.state}")
        val responseToken: String? = loginService.getNaverTokenUrl(loginRequest.code, loginRequest.state)
        println(responseToken)
        return if (responseToken != null) {
            val mapper = ObjectMapper()
            val token: NaverToken = mapper.readValue(responseToken, NaverToken::class.java)
            val responseUser: String? = loginService.getNaverUserByToken(token)
            if (responseUser != null) {
                val naverUser: NaverRes = mapper.readValue(responseUser, NaverRes::class.java)
                println(naverUser.response.id)
                println(naverUser.response.nickname)
                println(naverUser.response.age)
                println(naverUser.response.gender)
                println(naverUser.response.email)
                println(naverUser.response.name)
                println(naverUser.response.birthday)
                println(naverUser.response.birthyear)
                println(naverUser.response.profileImage)
                val user = loginService.findOrCreateUser(naverUser.response.email, naverUser)
                val jwt = JwtTokenProvider.generateJwt(naverUser.response.email)
                val responseBody = mapOf(
                        "code" to ResponseCode.USER_OAUTH2_SUCCESS.code,
                        "message" to ResponseCode.USER_OAUTH2_SUCCESS.message,
                        "result" to mapOf(
                                "jwt" to jwt,
                                "userId" to user.id
                        )
                )
                ResponseEntity.ok(responseBody)
            } else {
                val errorBody = mapOf(
                        "code" to ResponseCode.RESOURCE_NOT_FOUND.code,
                        "message" to "Naver 사용자 정보를 가져오는 데 실패했습니다."
                )
                ResponseEntity.badRequest().body(errorBody)
            }
        } else {
            val errorBody = mapOf(
                    "code" to ResponseCode.RESOURCE_NOT_FOUND.code,
                    "message" to "Naver 토큰을 가져오는 데 실패했습니다."
            )
            ResponseEntity.badRequest().body(errorBody)
        }
    }

}