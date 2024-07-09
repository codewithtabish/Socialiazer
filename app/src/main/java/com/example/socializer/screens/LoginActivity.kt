package com.example.socializer.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        lateinit var binding: ActivityLoginBinding
        var gender="male"

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
                signIn()
            }
        }

        private fun loadVariables() {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        // Start sign in process
        private fun signIn() {
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
                    firebaseAuthWithGoogle(account)
                } else {
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Authenticate with Firebase using the Google credential
        private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
            val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser

                        // Determine the photoURL based on gender
                        val photoURL = if (gender == "male") {
                            "https://avatar.iran.liara.run/public/boy"
                        } else {
                            "https://avatar.iran.liara.run/public/girl"
                        }


                        val userMap = hashMapOf(

                            "uid" to user?.uid,
                            "displayName" to user?.displayName,
                            "email" to user?.email,
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
                                "language" to "en",
                            )
                        )


                        FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(user?.uid.toString())
                            .set(userMap)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                                // Navigate to MainActivity
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Finish LoginActivity so it can't be returned to


                            }.addOnFailureListener {error->
                                Toast.makeText(this,error.toString(),Toast.LENGTH_LONG).show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        companion object {
            private const val RC_SIGN_IN = 9001
        }
    }

