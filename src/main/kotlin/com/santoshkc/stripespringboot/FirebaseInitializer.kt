package com.santoshkc.stripespringboot

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.IOException
import javax.annotation.PostConstruct


@Configuration
class FirebaseInitializer {
    private val logger = LoggerFactory.getLogger(FirebaseInitializer::class.java)

    @PostConstruct
    fun onStart() {
        logger.info("Initializing Firebase App...")
        try {
            initializeFirebaseApp()
        } catch (e: IOException) {
            logger.error("Initializing Firebase App {}", e)
        }
    }

    private fun initializeFirebaseApp() {
        if (FirebaseApp.getApps() == null || FirebaseApp.getApps().isEmpty()) {
            val serviceAccount =
                FirebaseInitializer::class.java.getResourceAsStream("/firebase-service-credentials-test.json")
            val credentials = GoogleCredentials.fromStream(serviceAccount)
            val options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build()
            FirebaseApp.initializeApp(options)
        }
    }
}