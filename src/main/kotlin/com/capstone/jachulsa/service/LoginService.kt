//package com.capstone.jachulsa.service
//
//import lombok.RequiredArgsConstructor
//import org.springframework.stereotype.Service
//import org.springframework.web.util.UriComponentsBuilder
//import java.io.BufferedReader
//import java.io.UnsupportedEncodingException
//import java.net.MalformedURLException
//import java.net.URISyntaxException
//import java.net.URLEncoder
//
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//
//@Service
//@RequiredArgsConstructor
//class LoginService {
//    @Throws(URISyntaxException::class, MalformedURLException::class, UnsupportedEncodingException::class)
//    fun getNaverAuthorizeUrl(type: String): String {
//        val baseUrl = "https://nid.naver.com/oauth2.0/authorize"
//        val clientId = "KsYEPMXctpGK6qPFxnbF"
//        val redirectUrl = "http://localhost:8080/oauth/login"
//        val uriComponents = UriComponentsBuilder
//            .fromUriString("$baseUrl/$type")
//            .queryParam("response_type", "code")
//            .queryParam("client_id", clientId)
//            .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
//            .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
//            .build()
//        return uriComponents.toString()
//    }
//
////    @Throws(URISyntaxException::class, MalformedURLException::class, UnsupportedEncodingException::class)
////    fun getNaverTokenUrl(type: String, callback: NaverCallback): String? {
////        val baseUrl = "https://nid.naver.com/oauth2.0/authorize"
////        val clientId = "KsYEPMXctpGK6qPFxnbF"
////        val clientSecret = "_fmpPupJcu"
////
////        val uriComponents = UriComponentsBuilder
////            .fromUriString("$baseUrl/$type")
////            .queryParam("grant_type", "authorization_code")
////            .queryParam("client_id", clientId)
////            .queryParam("client_secret", clientSecret)
////            .queryParam("code", callback.code)
////            .queryParam("state", URLEncoder.encode(callback.state, "UTF-8"))
////            .build()
////
////        return try {
////            val url = URL(uriComponents.toString())
////            val con = url.openConnection() as HttpURLConnection
////            con.requestMethod = "GET"
////
////            val responseCode = con.responseCode
////            val br: BufferedReader
////
////            br = if (responseCode == 200) { // 정상 호출
////                BufferedReader(InputStreamReader(con.inputStream))
////            } else {  // 에러 발생
////                BufferedReader(InputStreamReader(con.errorStream))
////            }
////
////            var inputLine: String?
////            val response = StringBuffer()
////            while (br.readLine().also { inputLine = it } != null) {
////                response.append(inputLine)
////            }
////
////            br.close()
////            response.toString()
////        } catch (e: Exception) {
////            e.printStackTrace()
////            null
////        }
////    }
////
////    fun getNaverUserByToken(token: NaverToken): String? {
////        try {
////            val accessToken = token.access_token
////            val tokenType = token.token_type
////
////            val url = URL("https://openapi.naver.com/v1/nid/me")
////            val con = url.openConnection() as HttpURLConnection
////            con.requestMethod = "GET"
////            con.setRequestProperty("Authorization", "$tokenType $accessToken")
////
////            val responseCode = con.responseCode
////            val br: BufferedReader
////
////            br = if (responseCode == 200) { // 정상 호출
////                BufferedReader(InputStreamReader(con.inputStream))
////            } else {  // 에러 발생
////                BufferedReader(InputStreamReader(con.errorStream))
////            }
////
////            var inputLine: String?
////            val response = StringBuffer()
////            while (br.readLine().also { inputLine = it } != null) {
////                response.append(inputLine)
////            }
////
////            br.close()
////            return response.toString()
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////        return null
////    }
//}