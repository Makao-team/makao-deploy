package com.makao.deploy.response

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalControllerAdvice {
    private val log = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)

    @ExceptionHandler(value = [CommonException::class])
    fun handleCommonException(e: CommonException): ResponseEntity<CommonResponse<String>> {
        log.error(e.message, e)
        return CommonResponse.error(e.status, e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<CommonResponse<String>> {
        val firstError: FieldError? = e.bindingResult.fieldErrors.firstOrNull()
        val errorMessage = firstError?.defaultMessage ?: "Invalid request"
        log.info("Validation error: {}", errorMessage)
        return CommonResponse.error(HttpStatus.BAD_REQUEST, errorMessage)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<CommonResponse<String>> {
        log.info(e.message, e)
        return CommonResponse.error(HttpStatus.NOT_FOUND, "NOT_FOUND")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<CommonResponse<String>> {
        log.error(e.message, e)
        return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR")
    }
}
