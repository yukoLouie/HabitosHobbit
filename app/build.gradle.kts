plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "2.1.0"
    id("com.google.gms.google-services") version "4.4.2"
}


android {
    namespace = "mx.edu.itson.potros.habitoshobbitses"
    compileSdk = 35

    defaultConfig {
        applicationId = "mx.edu.itson.potros.habitoshobbitses"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11" // Moved to the correct block
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.11.0")) // Asegurar que el BOM esté actualizado
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-analytics-ktx:22.4.0") // Confirmar que la versión sea compatible
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") // Última versión disponible
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.android.filament:filament-android:1.24.0")
    implementation(libs.firebase.auth.ktx)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
