package com.capstone.jachulsa.config

import com.capstone.jachulsa.service.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@kotlin.annotation.Target
@Retention(RetentionPolicy.RUNTIME)
annotation class AuthenticatedApi

@RestControllerAdvice
class AuthenticatedApiInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val method = handler.method
            if (method.isAnnotationPresent(AuthenticatedApi::class.java)) {
                // JWT 토큰을 헤더에서 추출
                val token = extractJwtFromHeader(request)

                // JWT 유효성 검사
                if (!JwtTokenProvider.validateJwt(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                    return false
                }

                // JWT 토큰을 사용하여 사용자 인증 처리
                //JwtTokenProvider.authenticateUser(token)
            }
        }
        return true
    }

    private fun extractJwtFromHeader(request: HttpServletRequest): String {
        val authHeader = request.getHeader("Authorization")
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            throw IllegalArgumentException("JWT token not found in Authorization header")
        }
    }
}
