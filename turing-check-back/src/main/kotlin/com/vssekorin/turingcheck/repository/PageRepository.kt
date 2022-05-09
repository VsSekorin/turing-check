package com.vssekorin.turingcheck.repository

import com.vssekorin.turingcheck.entity.Page
import org.springframework.data.jpa.repository.JpaRepository

interface PageRepository : JpaRepository<Page, Long> {

    fun findByName(name: String): Page?
}
