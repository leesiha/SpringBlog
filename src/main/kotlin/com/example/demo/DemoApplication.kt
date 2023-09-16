package com.example.demo

import TerminalUI
import com.example.demo.controller.ArticleController
import com.example.demo.controller.AuthController
import com.example.demo.controller.CommentController
import com.example.demo.controller.UserController
import com.example.demo.service.ArticleService
import com.example.demo.service.CommentService
import com.example.demo.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication(
    private val authController: AuthController,
    private val userController: UserController,
    private val articleController: ArticleController,
    private val commentController: CommentController,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        TerminalUI(authController, userController, articleController, commentController).start()
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
