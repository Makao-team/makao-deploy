package com.makao.deploy.util

import at.favre.lib.crypto.bcrypt.BCrypt

object StringEncoder {
    fun encode(str: String): String {
        return BCrypt.withDefaults().hashToString(12, str.toCharArray())
    }

    fun match(plain: String, hashed: String): Boolean {
        return BCrypt.verifyer().verify(plain.toCharArray(), hashed).verified
    }
}