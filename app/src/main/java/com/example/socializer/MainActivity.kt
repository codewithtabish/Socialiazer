package com.example.socializer

import android.media.RouteListingPreference.Item
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.Adapters.StoriesAdapter
import com.example.socializer.Fragments.HomeFragment
import com.example.socializer.Fragments.NotificationFragment
import com.example.socializer.Fragments.PostFragment
import com.example.socializer.Fragments.ProfileFragment
import com.example.socializer.Fragments.SearchFragment
import com.example.socializer.databinding.ActivityMainBinding
import com.example.socializer.models.StoryDataModel
import com.google.android.play.integrity.internal.i
import me.ibrahimsn.lib.OnItemSelectedListener
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadVariables()
        binding.bottomNavigationView.onItemSelected={it->
            when(it){
                0->{
                    replaceFragment(HomeFragment())

                }
                1->{
                    replaceFragment(SearchFragment())

                }
                2->{
                    replaceFragment(PostFragment())
                }
                3->{
                    replaceFragment(NotificationFragment())
                }
                4->{
                    replaceFragment(ProfileFragment())
                }
            }


        }


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout,fragment)
            .addToBackStack(null)
            .commit()

    }


    private fun loadVariables() {
        replaceFragment(HomeFragment())
    }
}