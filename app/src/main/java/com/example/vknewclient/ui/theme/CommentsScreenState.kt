package com.example.vknewclient.ui.theme

import com.example.vknewclient.domain.FeedPost
import com.example.vknewclient.domain.PostComment

sealed class CommentsScreenState {

    object Initial : CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ) : CommentsScreenState()
}