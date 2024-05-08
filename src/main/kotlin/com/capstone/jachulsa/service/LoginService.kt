package com.capstone.jachulsa.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.*
import java.util.stream.Collectors


@Service
@RequiredArgsConstructor
class LoginService {
    @Throws(URISyntaxException::class, MalformedURLException::class, UnsupportedEncodingException::class)
    fun getNaverAuthorizeUrl(type: String): String {
        val baseUrl = "https://nid.naver.com/oauth2.0/authorize"
        val clientId = "KsYEPMXctpGK6qPFxnbF"
        val redirectUrl = "http://localhost:8080/oauth/login"
        val uriComponents = UriComponentsBuilder
            .fromUriString(baseUrl)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
            .queryParam("state", URLEncoder.encode("123", "UTF-8"))
            .build()
        val finalUrl = uriComponents.toString()
        return finalUrl
    }

    @Throws(URISyntaxException::class, MalformedURLException::class, UnsupportedEncodingException::class)
    fun getNaverTokenUrl(callback: NaverCallback): String? {
        val baseUrl = "https://nid.naver.com/oauth2.0/token"
        val clientId = "KsYEPMXctpGK6qPFxnbF"
        val clientSecret = "_fmpPupJcu"

        val uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", callback.code)
                .queryParam("state", callback.state)
                .build().encode().toUri()

        return try {
            val url = URL(uriComponents.toString())
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST" // 토큰 요청은 POST 메서드를 사용해야 합니다.

            val responseCode = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                BufferedReader(InputStreamReader(con.inputStream)).use { br ->
                    br.lines().collect(Collectors.joining())
                }
            } else { // 에러 발생
                BufferedReader(InputStreamReader(con.errorStream)).use { br ->
                    br.lines().collect(Collectors.joining())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getNaverUserByToken(token: NaverToken): String? {
        try {
            val accessToken = token.access_token
            val tokenType = token.token_type

            val url = URL("https://openapi.naver.com/v1/nid/me")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("Authorization", "$tokenType $accessToken")

            val responseCode = con.responseCode
            val br: BufferedReader

            br = if (responseCode == 200) { // 정상 호출
                BufferedReader(InputStreamReader(con.inputStream))
            } else {  // 에러 발생
                BufferedReader(InputStreamReader(con.errorStream))
            }

            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }

            br.close()
            return response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}