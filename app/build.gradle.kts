plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "autonoma.pe.ux"
    compileSdk = 34

    defaultConfig {
        applicationId = "autonoma.pe.ux"
        minSdk = 25
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")

    // Libreria carview
    implementation("androidx.cardview:cardview:1.0.0")
    // Libreria preferencia para el manejo de sesiones
    implementation("androidx.preference:preference-ktx:1.2.1")
    //RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //LOTTIE ANIMACIONES
    implementation("com.airbnb.android:lottie:6.1.0")
    //INTERCEPTOR
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //Libreria para visualizacion de imagenes
    implementation ("com.squareup.picasso:picasso:2.8")
    //SearchableSpinner
    implementation ("com.toptoche.searchablespinner:searchablespinnerlibrary:1.3.1")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}