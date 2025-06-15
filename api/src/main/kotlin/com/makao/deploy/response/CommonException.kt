package com.makao.deploy.response

import org.springframework.http.HttpStatus

open class CommonException(
    override val message: String,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

class BadRequestException(
    override val message: String,
    override val cause: Throwable? = null
) : CommonException(message, HttpStatus.BAD_REQUEST, cause)

class NotFoundException(
    override val message: String,
    override val cause: Throwable? = null
) : CommonException(message, HttpStatus.NOT_FOUND, cause)

class UnauthorizedException(
    override val message: String,
    override val cause: Throwable? = null
) : CommonException(message, HttpStatus.UNAUTHORIZED, cause)

class InternalServerException(
    override val message: String,
    override val cause: Throwable? = null
) : CommonException(message, HttpStatus.INTERNAL_SERVER_ERROR, cause)