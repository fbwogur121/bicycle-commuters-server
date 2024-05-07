package com.capstone.jachulsa.exception

import com.capstone.jachulsa.dto.ResponseCode


class CustomException(val responseCode: ResponseCode) : RuntimeException()