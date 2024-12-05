plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.axel.astropic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.axel.astropic"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Android Core Libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Networking (Volley for HTTP Requests)
    implementation("com.android.volley:volley:1.2.1")

    // Image Loading (Picasso for displaying images)
    implementation("com.squareup.picasso:picasso:2.8")

    // Room Database (ORM for SQLite)
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2") // For Java Projects

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.google.code.gson:gson:2.8.8")

    // (Optional) For Room Database Migration/Testing
    implementation("androidx.room:room-ktx:2.5.2") // For Coroutine support in Room
}
