package com.example.demo.exception


class CommentNotFoundException(commentId: Long) : ResourceNotFoundException("Comment with ID $commentId not found")