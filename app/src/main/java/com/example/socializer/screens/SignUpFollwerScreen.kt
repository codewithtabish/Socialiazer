package com.example.socializer.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socializer.Adapters.UserAdapter
import com.example.socializer.MainActivity
import com.example.socializer.R
import com.example.socializer.databinding.ActivitySignUpFollwerScreenBinding
import com.example.socializer.models.Preferences
import com.example.socializer.models.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpFollwerScreen : AppCompatActivity() {
    lateinit var binding:ActivitySignUpFollwerScreenBinding
    lateinit var userList:ArrayList<UserDataModel>
    lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpFollwerScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadVarables()
        getAllCollectionData()
        binding.skipButton.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


    }

    private fun loadVarables() {
        userList=ArrayList()


    }


    private fun getAllCollectionData() {
        val db = FirebaseFirestore.getInstance() // Get Firestore instance
        userList.clear()

        val docRef = db.collection("Users")

        docRef.get() // Fetch all documents at once (consider pagination for large collections)
            .addOnSuccessListener { documents ->
                if (documents != null) {
                    userList.clear()
                    for (document in documents) {
                        val data = document.data
                        // Attempt to convert to UserDataModel, handle errors gracefully
                      val   uid = data["uid"] as? String ?: ""

                        val model = try {
                            UserDataModel(
                                uid = data["uid"] as? String ?: "",
                                displayName = data["displayName"] as? String,
                                email = data["email"] as? String,
                                photoURL = data["photoURL"] as? String,
                                role = data["role"] as? String ?: "registered_user",
                                bio = data["bio"] as? String ?: "",
                                gender = data["gender"] as? String ?: "",
                                followers = data["followers"] as? ArrayList<String> ?: ArrayList(),
                                following = data["following"] as? ArrayList<String> ?: ArrayList(),
                                online = data["online"] as? Boolean ?: true,
                                lastSeen = data["lastSeen"] as? Long ?: System.currentTimeMillis(),
                                createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis(),
                                updatedAt = data["updatedAt"] as? Long ?: System.currentTimeMillis(),
                                isActive = data["isActive"] as? Boolean ?: true,
                                notificationsEnabled = data["notificationsEnabled"] as? Boolean ?: true,
                                preferences = Preferences(
                                    theme = (data["preferences"] as? Map<*, *>)?.get("theme") as? String ?: "light",
                                    language = (data["preferences"] as? Map<*, *>)?.get("language") as? String ?: "en"
                                ),
                                followersCount=data["followersCount"] as? Int?:0



                            )
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error converting document: ${document.id}", e)
                            continue // Skip this document if conversion fails
                        }
                        if (uid!=FirebaseAuth.getInstance().currentUser?.uid.toString()){
                            userList.add(model)
                        }
                    }
                    getAllCollections() // Call your next function
                } else {
                    Log.d("Firestore", "No documents retrieved") // Handle no documents case (optional)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: $exception") // Handle errors
            }
    }



    private fun getAllCollections() {
        userAdapter = UserAdapter(this, userList)
        binding.signupFollwerRecyclerView.adapter = userAdapter
        binding.signupFollwerRecyclerView.layoutManager = LinearLayoutManager(this)
//        binding.shimmerView.visibility = View.GONE
//        swipeRefreshLayout.isRefreshing = false

    }


}