package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.model.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedResponseDto
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        for (post in posts) {
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue
            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date * 1000),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts.count),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count)
                ),
                isLiked = post.likes.userLikes > 0
            )
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.content.comments
        val profiles = responseDto.content.profiles

        for (comment in comments) {
            //TODO show images and videos in comments
            if (comment.text.isBlank()) continue
            val profile = profiles.find { it.id == comment.authorId.absoluteValue } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = String.format("%s %s", profile.firstName, profile.lastName),
                authorAvatarUrl = profile.photo100,
                commentText = comment.text,
                publicationDate = mapTimestampToDate(comment.date)
            )
            result.add(postComment)
        }
        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMM в hh:mm", Locale.getDefault()).format(date)
    }
}