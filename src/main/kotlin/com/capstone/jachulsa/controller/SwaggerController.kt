package com.capstone.jachulsa.controller

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequestMapping("/")
class ExampleController {
    @GetMapping("/example")
    fun example() = "예시 API"
}