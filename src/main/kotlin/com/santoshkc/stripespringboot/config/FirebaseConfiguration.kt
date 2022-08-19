package com.santoshkc.stripespringboot.config

import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfiguration {

    @Bean
    fun firestore(): Firestore {
        return FirestoreClient.getFirestore()
    }
}