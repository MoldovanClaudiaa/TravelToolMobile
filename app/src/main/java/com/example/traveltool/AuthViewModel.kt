package com.example.traveltool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.traveltool.data.Users
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // this will run any time the AuthViewModel is called
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

    fun signup(email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Somehing went wrong")
                }
            }
    }



//    fun signup(email : String, password : String) {
//
//        if(email.isEmpty() || password.isEmpty()) {
//            _authState.value = AuthState.Error("Email or password can't be empty")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.createUserWithEmailAndPassword(email,password)
//            .addOnCompleteListener{ task ->
//                if(task.isSuccessful) {
//                    _authState.value = AuthState.Authenticated
//                    val userId = auth.currentUser?.uid
//                    val user = Users(name,surname,username,email,age)
//
//                    userId?.let {
//                        firestore.collection("users").document(it).set(user)
//                            .addOnSuccessListener {
//                                _authState.value = AuthState.Authenticated
//                            }
//                            .addOnFailureListener { e ->
//                                _authState.value = AuthState.Error(e.Message ?: "Failed to save user data!")
//                            }
//                    }
//                } else {
//                    _authState.value = AuthState.Error(task.exception?.message?:"Somehing went wrong")
//                }
//            }
//    }

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