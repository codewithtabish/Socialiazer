package com.example.socializer.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.socializer.MainActivity
import com.example.socializer.R
import com.example.socializer.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
        private lateinit var mAuth: FirebaseAuth
        private lateinit var mGoogleSignInClient: GoogleSignInClient
        lateinit var usersList:ArrayList<String>
        lateinit var radioGroup: RadioGroup
        lateinit var animationLoader:LottieAnimationView

        lateinit var binding: ActivityLoginBinding
        var gender:Any?=null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            loadVariables()
            mAuth = FirebaseAuth.getInstance()
            binding.googleButton.setOnClickListener {
                handleGender()
                if (gender!=null){

                    signIn()

                }else{
                    Toast.makeText(this,"please select the gender",Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun handleGender() {
        val selectedRadioButtonId =radioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = findViewById(selectedRadioButtonId)
            val selectedGender = selectedRadioButton.text.toString()

            // Determine if the selected option is "Male" or "Female"
            when (selectedGender) {
                "MALE" -> {
                    gender="Male"
                }
                "FEMALE" -> {
                    gender="Female"

                }
            }
        }

    }

    private fun loadVariables() {
            usersList=ArrayList()
            radioGroup=binding.radioGroup
           animationLoader=binding.animationLoader
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        // Start sign in process
        private fun signIn() {
            animationLoader.visibility=View.VISIBLE
            binding.googleButton.isEnabled=false
            binding.textView.visibility=View.GONE
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // Handle sign in result
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }

        private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
            try {
                val account = completedTask.getResult(Exception::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account,gender.toString())
                } else {
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Authenticate with Firebase using the Google credential

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount, gender: String) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser

                    if (user != null) {
                        // Determine the photoURL based on gender
                        val photoURL = if (gender == "Male") {
                            "https://avatar.iran.liara.run/public/boy"
                        } else {
                            "https://avatar.iran.liara.run/public/girl"
                        }

                        // Check if the user exists in Firestore
                        FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(user.uid)
                            .get()
                            .addOnCompleteListener { documentTask ->
                                if (documentTask.isSuccessful) {
                                    val document = documentTask.result
                                    if (document != null && document.exists()) {
                                        changeAvatarOfLoginUser(user.uid)
                                        // User exists, navigate to MainActivity
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        animationLoader.visibility=View.GONE
                                        finish() // Finish LoginActivity so it can't be returned to
                                    }

                                    else {
                                        // User does not exist, create a new user
                                        val userMap = hashMapOf(
                                            "uid" to user.uid,
                                            "displayName" to user.displayName,
                                            "email" to user.email,
                                            "photoURL" to photoURL,
                                            "role" to "registered_user",  // Default role, can be changed later
                                            "bio" to "",  // Empty bio initially
                                            "gender" to gender,
                                            "followers" to ArrayList<String>(),  // Empty followers list
                                            "following" to ArrayList<String>(),  // Empty following list
                                            "online" to true,
                                            "lastSeen" to System.currentTimeMillis(),
                                            "createdAt" to System.currentTimeMillis(),
                                            "updatedAt" to System.currentTimeMillis(),
                                            "isActive" to true,
                                            "notificationsEnabled" to true,
                                            "preferences" to hashMapOf(
                                                "theme" to "light",
                                                "language" to "en"
                                            ),
                                            "followersCount" to 0
                                        )

                                        FirebaseFirestore.getInstance()
                                            .collection("Users")
                                            .document(user.uid)
                                            .set(userMap)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                                                // Navigate to FollowerActivity
                                                val intent = Intent(this, SignUpFollwerScreen::class.java)
                                                startActivity(intent)
                                                finish() // Finish LoginActivity so it can't be returned to
                                                animationLoader.visibility=View.GONE
                                            }
                                            .addOnFailureListener { error ->
                                                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                                                animationLoader.visibility=View.GONE
                                                binding.googleButton.isEnabled=true
                                                binding.textView.visibility=View.VISIBLE



                                            }
                                    }
                                } else {
                                    // Handle any errors
                                    documentTask.exception?.let {
                                        Log.e("Firestore Error", it.message ?: "Unknown error")
                                    }
                                    binding.googleButton.isEnabled=true
                                    binding.textView.visibility=View.VISIBLE


                                    Toast.makeText(this, "Error checking user existence.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    animationLoader.visibility=View.GONE
                    binding.googleButton.isEnabled=true
                    binding.textView.visibility=View.VISIBLE

                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeAvatarOfLoginUser(uid: String) {
        // Determine the photoURL based on gender
        val photoURL = if (gender == "Male") {
            "https://avatar.iran.liara.run/public/boy"
        } else {
            "https://avatar.iran.liara.run/public/girl"
        }

        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(uid)
            .update("photoURL", photoURL)
            .addOnSuccessListener {
                Toast.makeText(this, "Avatar updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error updating avatar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    companion object {
            private const val RC_SIGN_IN = 9001
        }
    }



