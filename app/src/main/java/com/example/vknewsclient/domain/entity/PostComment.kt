package com.example.vknewsclient.domain.entity

import com.example.vknewsclient.R

data class PostComment(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val commentText: String,
    val publicationDate: String
)
