package com.example.demo.exception

class UnauthorizedUserException : RuntimeException {
    constructor() : super("User is not authorized for this action")
    constructor(message: String) : super(message)
}