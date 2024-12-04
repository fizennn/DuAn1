plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.duan1.polyfood"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.duan1.polyfood"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions.resources.excludes.add("META-INF/io.netty.versions.properties")
    packagingOptions.resources.excludes.add("META-INF/INDEX.LIST")
    packagingOptions.resources.excludes.add("mozilla/public-suffix-list.txt")
    packagingOptions.resources.excludes.add("META-INF/DEPENDENCIES")


}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BOM và các thư viện Firebase
    implementation(libs.firebase.bom)
    implementation(libs.google.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // Thư viện Google Sign-In
    implementation(libs.play.services.auth)

    // Gson dùng để xử lý JSON
    implementation(libs.gson)

    // Glide dùng để tải hình ảnh
    implementation(libs.glide)
    annotationProcessor(libs.compiler)


    // https://mvnrepository.com/artifact/com.google.firebase/firebase-messaging
    implementation(libs.firebase.messaging)

    implementation (libs.lottie)

    implementation ("com.google.auth:google-auth-library-oauth2-http:1.20.0")
    implementation(kotlin("script-runtime"))


}
