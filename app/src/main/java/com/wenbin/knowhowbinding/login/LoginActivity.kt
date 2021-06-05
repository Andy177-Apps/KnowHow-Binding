package com.wenbin.knowhowbinding.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.wenbin.knowhowbinding.MainViewModel
import com.wenbin.knowhowbinding.R
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.databinding.ActivityLoginBinding
import com.wenbin.knowhowbinding.ext.getVmFactory


class LoginActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel> { getVmFactory() }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.d("knowGoogleSignIn", "currentUser = $currentUser")
        updateUI(currentUser)
        Log.d("knowGoogleSignIn", "updatedCurrentUser = $currentUser")
        Log.d("knowGoogleSignIn", "updateUI(currentUser) = ${updateUI(currentUser)})")
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
            Log.d("knowGoogleSignIn", "auth = $auth")
            Log.d("check_googleSign", "signInButton in Activity is clicked.")
            viewModel.firebaseAuthWithGoogle("12356789")
            signIn()
        }

        viewModel.firebaseUser.observe(this, Observer {
            Log.d("check_googleSign", "firebaseUser = $it")

        })
    }



    private fun moveMainActivity(user: FirebaseUser) {
        val currentUser = User(
            id = user.uid,
            image = user.photoUrl.toString(),
            name = user.displayName.toString(),
            email = user.email.toString()
        )

        UserManager.user = currentUser

    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("knowGoogleSignIn", "requestCode = $requestCode")

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
                updateUI(viewModel.firebaseUser.value)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
// [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
// [END auth_with_google]

// [START signin]
private fun signIn() {
    val signInIntent = googleSignInClient.signInIntent
    Log.d("knowGoogleSignIn", "signInIntent = $signInIntent")
    Log.d("knowGoogleSignIn", "RC_SIGN_IN = $RC_SIGN_IN")
    startActivityForResult(signInIntent, RC_SIGN_IN)
}
// [END signin]

    private fun updateUI(user: FirebaseUser?) {}

    companion object {
        const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}