package com.capstone.jachulsa.controller

import com.capstone.jachulsa.service.LoginRequest
import com.capstone.jachulsa.service.LoginService
import com.capstone.jachulsa.service.NaverRes
import com.capstone.jachulsa.service.NaverToken
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
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
    ): String {
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
                "naverUser.toString() : $naverUser\n" +
                        "naverUser.response.gender : ${naverUser.response.gender}\n" +
                        "naverUser.response.birthyear : ${naverUser.response.birthday}\n" +
                        "naverUser.response.age : ${naverUser.response.age}"
            } else {
                "Naver 사용자 정보를 가져오는 데 실패했습니다."
            }
        } else {
            "Naver 토큰을 가져오는 데 실패했습니다."
        }
    }

//    @GetMapping("/oauth/login")
//    @Throws(
//            MalformedURLException::class,
//            UnsupportedEncodingException::class,
//            URISyntaxException::class
//    )
//    fun callBack(
//            request: HttpServletRequest?,
//            response: HttpServletResponse?,
//            @RequestParam("code") code: String,
//            @RequestParam("state") state: String,
//            @RequestParam(value = "error", required = false) error: String?,
//            @RequestParam(value = "error_description", required = false) errorDescription: String?
//    ) {
//        val callback = NaverCallback(code, state, error, errorDescription)
//        val responseToken: String? = loginService.getNaverTokenUrl(callback)
//        if (responseToken != null) {
//            val mapper = ObjectMapper()
//            val token: NaverToken = mapper.readValue(responseToken, NaverToken::class.java)
//            val responseUser: String? = loginService.getNaverUserByToken(token)
//            if (responseUser != null) {
//                val naverUser: NaverRes = mapper.readValue(responseUser, NaverRes::class.java)
//                println("naverUser.toString() : " + naverUser.toString())
//                println("naverUser.response.gender : " + naverUser.response.gender)
//                println("naverUser.response.birthyear : " + naverUser.response.birthday)
//                println("naverUser.response.age : " + naverUser.response.age)
//            } else {
//                println("Naver 사용자 정보를 가져오는 데 실패했습니다.")
//            }
//        } else {
//            println("Naver 토큰을 가져오는 데 실패했습니다.")
//        }
//    }

}