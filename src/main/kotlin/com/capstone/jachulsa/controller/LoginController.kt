package com.capstone.jachulsa.controller

import com.capstone.jachulsa.service.LoginService
import com.capstone.jachulsa.service.NaverCallback
import com.capstone.jachulsa.service.NaverRes
import com.capstone.jachulsa.service.NaverToken
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URISyntaxException


@Controller
@RequiredArgsConstructor
class LoginController(
        private val loginService: LoginService
) {

    @GetMapping("/naver-login")
    @Throws(
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        URISyntaxException::class
    )
    fun naverLogin(request: HttpServletRequest?, response: HttpServletResponse) {

        val url: String = loginService.getNaverAuthorizeUrl("authorize")
        println(url)
        try {
            response.sendRedirect(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @GetMapping("/oauth/login")
    @ResponseBody
    @Throws(
            MalformedURLException::class,
            UnsupportedEncodingException::class,
            URISyntaxException::class
    )
    fun callBack(
            request: HttpServletRequest?,
            response: HttpServletResponse?,
            @RequestParam("code") code: String,
            @RequestParam("state") state: String,
            @RequestParam(value = "error", required = false) error: String?,
            @RequestParam(value = "error_description", required = false) errorDescription: String?
    ): String {
        val callback = NaverCallback(code, state, error, errorDescription)
        val responseToken: String? = loginService.getNaverTokenUrl(callback)
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