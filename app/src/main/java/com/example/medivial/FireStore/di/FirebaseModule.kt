package com.example.medivial.FireStore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {
    var userDitails: DocumentReference =  Firebase.firestore.collection("user").document(
        FirebaseAuth.getInstance().currentUser!!.uid)
    @Provides
    @Singleton
    fun provideFireStoreInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("user")
    }
}