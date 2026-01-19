plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.forseva"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.forseva"
        minSdk = 31
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // HTTP Logging (Essential for debugging microservices)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Lifecycle (for ViewModels if you use them later)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("com.google.android.gms:play-services-location:20.0.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
}