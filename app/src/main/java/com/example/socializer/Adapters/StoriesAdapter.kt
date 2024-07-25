package com.example.socializer.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.screens.SplashScreen
import com.google.android.play.integrity.internal.i
import com.squareup.picasso.Picasso
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory


class StoriesAdapter(val context: Context, val storiesList:ArrayList<MyStory>,val manager: FragmentManager): RecyclerView.Adapter<StoriesAdapter.UserViewHolder>() {

    inner  class UserViewHolder(view: View):RecyclerView.ViewHolder(view){
        val storyImage=view.findViewById<ImageView>(R.id.imageViewStory)
        val storyUserName=view.findViewById<TextView>(R.id.storyUserName)
        val row=view.findViewById<ConstraintLayout>(R.id.storyRow)


    }



    override fun getItemCount(): Int {
        return  storiesList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val obj = storiesList[position]
        val imageUrl = obj?.url
        if (imageUrl != null) {
            Picasso.get()
                .load(imageUrl)
                .into(holder.storyImage)
        }
        var i=3
        while (i<=1){
            Picasso.get()
                .load(imageUrl)
                .into(holder.storyImage)

            i--


        }


        holder.row.setOnClickListener {
            val myList=ArrayList<MyStory>()
            myList.add(MyStory(obj.url))

                StoryView.Builder(manager)
                    .setStoriesList(myList)
                    .setStoryDuration(5000)
                    .setTitleText("Hamza Al-Omari")
                    .setSubtitleText("Damascus")
                    .setTitleLogoUrl("some-link")
                    .setStoryClickListeners(object : StoryClickListeners {
                        override fun onDescriptionClickListener(position: Int) {
                            // Implement your action for description click
                        }

                        override fun onTitleIconClickListener(position: Int) {
                            // Implement your action for title icon click
                        }
                    })
                    .build()
                    .show()
                // Handle case where fragmentManager is null (e.g., log an error)
                Log.e("StoryView", "FragmentManager not found for story view")
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.story_row,parent,false))


    }
}