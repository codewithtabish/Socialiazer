package com.example.socializer.models
import com.google.firebase.Timestamp

data class Story(
    val storyID: String,
    val userID: String,
    val mediaURL: String,
    val type: String,
    val timestamp: Timestamp,
    val likes: ArrayList<String>, // Array of userIDs who liked the story
    val likeCount: Int,
    val comments: ArrayList<Comment>, // Array of comments
    val shares: ArrayList<String>, // Array of userIDs who shared the story
    val shareCount: Int
)

data class Comment(
    val commentID: String,
    val userID: String,
    val text: String
)
