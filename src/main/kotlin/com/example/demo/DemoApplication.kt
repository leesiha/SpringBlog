package com.example.demo

import TerminalUI
import com.example.demo.controller.AuthController
import com.example.demo.service.ArticleService
import com.example.demo.service.CommentService
import com.example.demo.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication(
    private val authController: AuthController,
    private val articleService: ArticleService,
    private val commentService: CommentService,
    private val userService: UserService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        TerminalUI(authController, articleService, commentService, userService).start()
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
