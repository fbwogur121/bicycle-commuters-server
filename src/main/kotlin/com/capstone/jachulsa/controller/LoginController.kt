package com.capstone.jachulsa.controller

import com.capstone.jachulsa.service.LoginService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URISyntaxException


@Controller
@RequiredArgsConstructor
class LoginController (private val loginService: LoginService) {
    //private val loginService: LoginService? = null

    @GetMapping("/naver-login")
    @Throws(
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        URISyntaxException::class
    )
    fun naverLogin(request: HttpServletRequest?, response: HttpServletResponse) {
        val url: String = loginService.getNaverAuthorizeUrl("authorize")
        try {
            response.sendRedirect(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @GetMapping("/oauth/login")
    @Throws(
        MalformedURLException::class,
        UnsupportedEncodingException::class,
        URISyntaxException::class,
        JsonProcessingException::class
    )
    fun callBack(request: HttpServletRequest?, response: HttpServletResponse?, callback: NaverCallback) {
        if (callback.getError() != null) {
            System.out.println(callback.getError_description())
        }
        val responseToken: String = loginService.getNaverTokenUrl("token", callback)
        val mapper = ObjectMapper()
        val token: NaverToken = mapper.readValue(responseToken, NaverToken::class.java)
        val responseUser: String = loginService.getNaverUserByToken(token)
        val naverUser: NaverRes = mapper.readValue(responseUser, NaverRes::class.java)
        System.out.println("naverUser.toString() : " + naverUser.toString())
        System.out.println("naverUser.getResonse().getGender() : " + naverUser.getResponse().getGender())
        System.out.println("naverUser.getResonse().getBirthyear() : " + naverUser.getResponse().getBirthyear())
        System.out.println("naverUser.getResonse().getAge() : " + naverUser.getResponse().getAge())
    }
}