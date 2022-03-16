package com.example.rediscacheexample

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch
import java.time.Instant
import java.io.Serializable

@SpringBootApplication
@EnableCaching
class RedisCacheExampleApplication {

    @Bean
    fun applicationRunner(expensiveService: ExpensiveService) = ApplicationRunner {
        val stopWatch = StopWatch()
        val input = 42.0
        time(expensiveService, stopWatch, input)
        time(expensiveService, stopWatch, input)
    }
}

fun main(args: Array<String>) {
    runApplication<RedisCacheExampleApplication>(*args)
}

@Service
class ExpensiveService {

    @Cacheable("expensive")
    fun performExpensiveCalculation(input: Double): Response {
        Thread.sleep(10000)
        return Response("response from input $input @ ${Instant.now()}")
    }
}

data class Response(val message: String) : Serializable

private fun time(expensiveService: ExpensiveService, stopWatch: StopWatch, input: Double): Response {
    stopWatch.start()
    val response = expensiveService.performExpensiveCalculation(input)
    stopWatch.stop()
    println("got response $response after ${stopWatch.lastTaskTimeMillis}")
    return response
}