package com.capstone.jachulsa.service

import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service
object JwtTokenProvider {
    private const val SECRET_KEY = "mySecretKey"
    private const val EXPIRATION_TIME_MS = 864_000_000 // 10일(ms 단위)

    // JWT 생성
    fun generateJwt(email: String): String {
        val expirationDate = Date(System.currentTimeMillis() + EXPIRATION_TIME_MS)
        val signingKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray())

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact()
    }

    // JWT 유효성 검사
    fun validateJwt(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY.toByteArray()).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // JWT에서 email 추출
    fun getEmailFromJwt(token: String): String? {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.toByteArray()).build().parseClaimsJws(token).body
            claims.subject
        } catch (e: Exception) {
            null
        }
    }


//    // JWT 토큰에서 사용자 인증 정보 추출
//    fun getAuthentication(token: String): Authentication? {
//        val email = getEmailFromJwt(token) ?: return null
//
//        val userDetails: UserDetails = YourUserDetailsService.loadUserByUsername(email)
//        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//    }
//
//    // 현재 사용자를 인증 처리
//    fun authenticateUser(token: String) {
//        val authentication: Authentication = getAuthentication(token)
//                ?: throw CustomException(ResponseCode.UNAUTHORIZED)
//
//        SecurityContextHolder.getContext().authentication = authentication
//    }
}