package com.itis.foody.common.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

private const val DB_URL = "https://foody-627d7-default-rtdb.europe-west1.firebasedatabase.app/"
private const val STORAGE_CUSTOM_BUCKET = "gs://foody-627d7.appspot.com"

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseDatabase(): DatabaseReference = Firebase.database(DB_URL).reference

    @Provides
    fun providesFirebaseStorage(): StorageReference =
        Firebase.storage(STORAGE_CUSTOM_BUCKET).reference
}
