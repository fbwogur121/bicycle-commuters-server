package com.capstone.jachulsa.dto

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val httpStatus: HttpStatus,
    val code: Int,
    var message: String
) {

    /**
    success - 1000~
     */

    USER_LOGIN_SUCCESS(HttpStatus.CREATED,1010,"login success"),

    CREATE_SUCCESS(HttpStatus.CREATED,1020,"create success"),
    READ_SUCCESS(HttpStatus.OK,1021,"read success"),
    UPDATE_SUCCESS(HttpStatus.OK,1022,"update success"),
    DELETE_SUCCESS(HttpStatus.OK,1023,"delete success"),


    /**
    fail - 2000~
     */
    // 400 Bad Request
    HTTP_NOT_READABLE(HttpStatus.BAD_REQUEST,2000,"Http message not readable."),
    NOT_READABLE(HttpStatus.BAD_REQUEST,2001,"{mismatchedFieldNames} 필드가 누락되었거나 데이터 형식이 올바르지 않습니다."), //HttpMessageNotReadableException null,자료형
    NOT_NULL_FIELD(HttpStatus.BAD_REQUEST,2002," {fieldName} 필드의 입력값 [ rejectedValue ]이 유효하지 않습니다."), // MethodArgumentNotValidException (@VALID)
    TYPE_MISMATCHED(HttpStatus.BAD_REQUEST,2002," {fieldName} 필드의 입력값 [ rejectedValue ]의 형식이 올바르지 않습니다."), // MethodArgumentTypeMismatchException


    // 401 Unauthorized
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,2100, "접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,2101, "유효한 토큰이 아닙니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED,2102, "액세스 토큰이 만료되었습니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN,2300,"권한이 없습니다."),

    // 404 Not Found 페이지
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,2400,"요청한 주소를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,2401,"사용자가 존재하지 않습니다."),
    EXPENDITURE_NOT_FOUND(HttpStatus.NOT_FOUND,2402,"지출 기록이 존재하지 않습니다."),
    RIDING_NOT_FOUND(HttpStatus.NOT_FOUND,2403,"라이딩 기록이 존재하지 않습니다."),


    // 409 Conflict
    RESOURCE_ALREADY_EXIST(HttpStatus.CONFLICT,2901,"이미 존재하는 { }입니다."),



    /**
    server error - 4000~
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,4000, "서버 오류가 발생하였습니다.");


}
