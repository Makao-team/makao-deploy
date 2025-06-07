package com.makao.deploy.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CommonResponse<T>(
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T): ResponseEntity<CommonResponse<T>> {
            return ResponseEntity.ok(CommonResponse("OK", data))
        }

        fun <T> error(httpStatus: HttpStatus, message: String): ResponseEntity<CommonResponse<T>> {
            return ResponseEntity.status(httpStatus).body(CommonResponse(message))
        }
    }
}
