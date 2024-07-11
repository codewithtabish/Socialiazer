package com.example.socializer.models

data class UserDataModel(
    val uid: String = "",
    val displayName: String? = null,
    val email: String? = null,
    val photoURL: String? = null,
    val role: String = "registered_user",  // Default role, can be changed later
    val bio: String = "",  // Empty bio initially
    val gender: String = "",
    val followers: ArrayList<String> = ArrayList(),  // Empty followers list
    val following: ArrayList<String> = ArrayList(),  // Empty following list
    val online: Boolean = true,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val preferences: Preferences = Preferences(),
    var followersCount: Int=0 // Add this field

)

data class Preferences(
    val theme: String = "light",
    val language: String = "en"
)