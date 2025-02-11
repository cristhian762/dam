plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mattiuzzi.fontana.cristhian.socialnetworkv2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mattiuzzi.fontana.cristhian.socialnetworkv2"
        minSdk = 24
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
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    val pagingVersion = "3.1.1"

    implementation("androidx.paging:paging-guava:$pagingVersion")
    implementation("androidx.paging:paging-runtime:$pagingVersion")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}