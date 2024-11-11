package com.example.traveltool

//import androidx.datastore.core.message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveltool.data.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if(auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {

                        if(email.isEmpty() || password.isEmpty()) {
                            _authState.value = AuthState.Error("Email or password can't be empty")
                            return
                        }

                        _authState.value = AuthState.Loading
                        auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful) {
                                    _authState.value = AuthState.Authenticated
                                } else {
                                    _authState.value = AuthState.Error(task.exception?.message?:"Somehing went wrong")
                                }
            }
    }

    fun signup(firstName: String, lastName: String, username: String, email : String, password : String) {

        if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("All fields are required!")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    val userId = auth.currentUser?.uid

                    // Create a user object and save it in Firestore
                    val user = Users(firstName,lastName,username,email,password)
                    userId?.let {
                        firestore.collection("users").document(it).set(user)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                            }
                            .addOnFailureListener { e ->
                                _authState.value = AuthState.Error(e.message ?: "Failed to save user data!")
                            }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Somehing went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}

sealed class AuthState {

    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val Message : String) : AuthState()
}