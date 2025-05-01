package com.makao.deploy.response

import org.springframework.http.HttpStatus

open class CommonException(
    override val message: String,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
) : RuntimeException(message)

class BadRequestException(
    override val message: String,
) : CommonException(message, HttpStatus.BAD_REQUEST)

class NotFoundException(
    override val message: String,
) : CommonException(message, HttpStatus.NOT_FOUND)

