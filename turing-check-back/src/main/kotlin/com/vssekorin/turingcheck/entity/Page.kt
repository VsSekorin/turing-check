package com.vssekorin.turingcheck.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "page")
data class Page(

    @Id
    @SequenceGenerator(name = "page_id_gen", sequenceName = "page_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_id_gen")
    @Column(nullable = false)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String = "",

    @Column(nullable = false)
    var program: String = "",

    @Column(nullable = false)
    var tests: String = "",
) {
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    lateinit var created: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updated: LocalDateTime
}
