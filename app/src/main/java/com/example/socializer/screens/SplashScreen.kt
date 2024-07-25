package com.example.socializer.screens

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.socializer.MainActivity
import com.example.socializer.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        animationView = findViewById(R.id.animation_view)

        animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Optional: Do something when the animation starts
            }

            override fun onAnimationEnd(animation: Animator) {
                // Load variables and navigate to the appropriate screen after the animation ends
                loadVariables()
            }

            override fun onAnimationCancel(animation: Animator) {
                // Optional: Do something when the animation is cancelled
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Optional: Do something when the animation repeats
            }
        })
    }

    private fun loadVariables() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
     val   mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser == null) {
            // User is not logged in, navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User is logged in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
