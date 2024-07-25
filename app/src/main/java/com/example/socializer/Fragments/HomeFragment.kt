package com.example.socializer.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socializer.Adapters.StoriesAdapter
import com.example.socializer.R
import com.example.socializer.databinding.ActivityMainBinding
import com.example.socializer.databinding.FragmentHomeBinding
import com.example.socializer.models.StoryDataModel
import omari.hamza.storyview.model.MyStory

class HomeFragment : Fragment() {
    lateinit var storiesList:ArrayList<StoryDataModel>
    lateinit var myStoryList:ArrayList<MyStory>
    lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)
        loadData()

        return binding.root
    }

    private fun loadVariables() {
        myStoryList=ArrayList()
        storiesList=ArrayList()
    }
    private  fun loadData(){
        loadVariables()
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/boy",null,"1"))
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/girl"))
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/boy"))
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/girl"))
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/girl"))
        myStoryList.add(MyStory("https://avatar.iran.liara.run/public/girl"))
        val adapter= StoriesAdapter(requireContext(),myStoryList,requireActivity().supportFragmentManager)
        binding.storiesRecyclerView.adapter=adapter
        binding.storiesRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)



    }



}