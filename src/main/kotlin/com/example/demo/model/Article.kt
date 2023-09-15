package com.example.demo.model

import java.time.Instant
import javax.persistence.*

@Entity
@Table(
    name = "article", schema = "demo_schema", indexes = [
        Index(name = "f3_idx", columnList = "user_id")
    ]
)
class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    var id: Long = 0

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: Instant

    @Column(name = "content", nullable = false)
    lateinit var content: String

    @Column(name = "title", nullable = false)
    lateinit var title: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User
}