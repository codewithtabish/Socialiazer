package com.example.socializer.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.IntegerParser
import com.example.socializer.R
import com.example.socializer.models.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class UserAdapter(val context:Context,val usersList:ArrayList<UserDataModel>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.signup_follower_first_row,parent,false))
    }

    override fun getItemCount(): Int {
      return  usersList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val obj = usersList[position]
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        holder.userName.text = obj.displayName
        var counts=obj.followers.size.toString()
        val intCount=0
        if (counts.toInt() ==intCount){
            counts=""

        }
        holder.followers.text = "${counts} "
        Picasso.get()
            .load(obj.photoURL)
            .into(holder.profileImage)

        // Set initial button text based on follow status
     if (obj.followers.contains(currentUserUid)){
         holder.followButton.text="UnFollow"
//         holder.followButton.setBackgroundResource(R.drawable.login_button_design)

     }else{
         holder.followButton.text="Follow"


     }




        holder.followButton.setOnClickListener {
            val userDocRef = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(obj.uid)

            val isFollowing = obj.followers.contains(currentUserUid)

            if (isFollowing) {
                // User is currently following, so unfollow
                obj.followers.remove(currentUserUid)
                if (obj.followersCount!=0){

                    holder.followers.text = "${obj.followersCount}💞"
                    obj.followersCount -= 1
                    if (obj.followersCount==0){
                        holder.followers.text = ""


                    }


                }else{
                    holder.followers.text = ""
                }
                holder.followButton.text = "Follow"
            } else {
                // User is not following, so follow
                obj.followers.add(currentUserUid)
                obj.followersCount += 1
                holder.followButton.text = "Unfollow"
            }

            // Update Firestore with the new followers list and count
            userDocRef.update(
                mapOf(
                    "followers" to obj.followers,
                    "followersCount" to obj.followersCount
                )
            )
                .addOnSuccessListener {
                    // Successfully updated
                    holder.followers.text = "${obj.followersCount}"
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Toast.makeText(context, "Failed to update followers: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }



    class UserViewHolder(viewItem:View):RecyclerView.ViewHolder(viewItem) {
        val profileImage=viewItem.findViewById<ImageView>(R.id.user_follweer_signup_profile)
        val userName=viewItem.findViewById<TextView>(R.id.user_signup_userName)
        val followers=viewItem.findViewById<TextView>(R.id.user_signUp_follwer_count)
        val followButton=viewItem.findViewById<Button>(R.id.user_signup_follow_button)

    }
}