package com.capstone.jachulsa.service

import com.capstone.jachulsa.dto.ResponseCode
import com.capstone.jachulsa.exception.CustomException
import com.capstone.jachulsa.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import java.security.Key


@Service
class JwtTokenProvider(private val userRepository: UserRepository){

    private lateinit var key: Key

    // JWT 만료 시간 설정 (예: 864000000ms = 10일)
    private val EXPIRATION_TIME_MS: Long = 864_000_000

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String


    @PostConstruct
    fun init() {
        // secretKey를 기반으로 Key 객체 생성
        key = Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    // JWT 생성
    fun generateJwt(email: String): String {

        System.out.println(email)
        userRepository.findOneByEmail(email) ?: throw CustomException(ResponseCode.USER_NOT_FOUND)

        val expirationDate = Date(System.currentTimeMillis() + EXPIRATION_TIME_MS)

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact()
    }

    // JWT 유효성 검사
    fun validateJwt(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // JWT에서 email 추출 - sub: "~~@naver.com"
    fun getEmailFromJwt(token: String): String {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
            claims["sub"]?.toString() ?: throw CustomException(ResponseCode.INVALID_EMAIL_CLAIM)
        } catch (e: Exception) {
            throw CustomException(ResponseCode.INVALID_ACCESS_TOKEN)
        }
    }


//    fun getEmailFromJwt(token: String): String { //- email json으로 저장될 시
//        return try {
//            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
//            val subClaim = claims["sub"]?.toString()
//            val jsonObject = ObjectMapper().readTree(subClaim)
//            jsonObject["email"]?.asText() ?: throw CustomException(ResponseCode.INVALID_EMAIL_CLAIM)
//        } catch (e: Exception) {
//            throw CustomException(ResponseCode.INVALID_ACCESS_TOKEN)
//        }
//    }

//    // JWT 토큰을 Authorization 헤더에서 추출하는 함수
//    private fun extractJwtFromHeader(authorizationHeader: String?): String? {
//        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            authorizationHeader.substring(7)
//        } else {
//            null
//        }

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