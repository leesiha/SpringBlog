package com.example.demo.model

import java.time.Instant
import javax.persistence.*

@Entity
@Table(
    name = "user", schema = "demo_schema", indexes = [
        Index(name = "email_UNIQUE", columnList = "email", unique = true)
    ]
)
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    val id: Long = 0

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: Instant

    @Column(name = "email", nullable = false)
    lateinit var email: String

    @Column(name = "password", nullable = false)
    lateinit var password: String

    @Column(name = "username", nullable = false)
    lateinit var username: String
}