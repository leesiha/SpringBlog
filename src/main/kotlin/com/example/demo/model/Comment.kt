package com.example.demo.model

import java.time.Instant
import javax.persistence.*

@Entity
@Table(
    name = "comment", schema = "demo_schema", indexes = [
        Index(name = "f1_idx", columnList = "user_id"),
        Index(name = "f2_idx", columnList = "article_id")
    ]
)
class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    var id: Long = 0

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: Instant

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: Instant

    @Column(name = "content", nullable = false)
    lateinit var content: String

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", nullable = false)
    lateinit var article: Article
}