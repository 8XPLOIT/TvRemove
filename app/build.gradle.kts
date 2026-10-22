plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "com.example.tvremote"
    compileSdk = 34
    defaultConfig { applicationId = "com.example.tvremote"; minSdk = 24; targetSdk = 34; versionCode = 1; versionName = "1.0" }
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.5.0")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
