package com.wenbin.knowhowbinding.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wenbin.knowhowbinding.MainActivity
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.ActivityLoginBinding
import com.wenbin.knowhowbinding.ext.getVmFactory
import com.wenbin.knowhowbinding.splash.SplashActivity
import com.wenbin.knowhowbinding.util.Logger


class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
        currentUser?.let {
            moveMainActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]
        auth = Firebase.auth

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener {
            viewModel.firebaseAuthWithGoogle("12356789")
            signIn()
        }

        viewModel.firebaseUser.observe(this, Observer {
            Logger.d("check_googleSign, firebaseUser = $it")
            it?.let {
                moveMainActivity(it)
            }
        })

        binding.button2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
        }

    }



    private fun moveMainActivity(user: FirebaseUser) {

        val intent = Intent(this, SplashActivity::class.java)

        val currentUser = User(
            id = user.uid,
            image = user.photoUrl.toString(),
            name = user.displayName.toString(),
            email = user.email.toString()
        )

        UserManager.user = currentUser

        viewModel.postUser(currentUser)

        startActivity(intent)
        overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
        finish()
    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                // firebaseAuthWithGoogle(account.idToken!!)
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
                updateUI(viewModel.firebaseUser.value)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Logger.w("Google sign in failed: $e")
            }
        }
    }
    // [END onactivityresult]

// [START signin]
private fun signIn() {
    val signInIntent = googleSignInClient.signInIntent
    Logger.d("knowGoogleSignIn, signInIntent = $signInIntent")
    Logger.d("knowGoogleSignIn, RC_SIGN_IN = $RC_SIGN_IN")
    startActivityForResult(signInIntent, RC_SIGN_IN)
}
// [END signin]

    private fun updateUI(user: FirebaseUser?) {}

    companion object {
        const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}