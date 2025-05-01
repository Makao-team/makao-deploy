package com.makao.deploy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MakaoDeployApplication

fun main(args: Array<String>) {
    runApplication<MakaoDeployApplication>(*args)
}
