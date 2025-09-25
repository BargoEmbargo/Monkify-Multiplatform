package cz.uhk.monkify

import android.app.Application
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import java.io.InputStream
import java.util.Properties

fun initializeFirebase() {
    FirebasePlatform.initializeFirebasePlatform(
        object : FirebasePlatform() {
            val storage = mutableMapOf<String, String>()
            override fun store(key: String, value: String) = storage.set(key, value)
            override fun retrieve(key: String) = storage[key]
            override fun clear(key: String) {
                storage.remove(key)
            }
            override fun log(msg: String) = println(msg)
        },
    )

    val properties = Properties()
    val inputStream: InputStream? = try {
        Thread.currentThread().contextClassLoader.getResourceAsStream("firebase.properties")
    } catch (e: Exception) {
        initializeFirebase()::class.java.classLoader?.getResourceAsStream("firebase.properties")
    }

    if (inputStream == null) {
        throw RuntimeException("Cannot find firebase.properties.Make sure it's in src/desktopMain/resources")
    }

    properties.load(inputStream)

    val options = FirebaseOptions(
        apiKey = properties.getProperty("apiKey"),
        authDomain = properties.getProperty("authDomain"),
        projectId = properties.getProperty("projectId"),
        applicationId = properties.getProperty("applicationId"),
        storageBucket = properties.getProperty("storageBucket"),
        gcmSenderId = properties.getProperty("gcmSenderId"),
    )

    Firebase.initialize(Application(), options)
}
