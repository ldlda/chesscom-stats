import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.parcelize)
}
android {
    namespace = "com.ldlda.chesscom_stats"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.ldlda.chesscom_stats"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }

    testOptions {
//        unitTests.isReturnDefaultValues = true
    }

    configurations.all {
        exclude(group = "com.intellij", module = "annotations") // WHO
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.converter.kotlinx.serialization)
    implementation(libs.mockwebserver)
    implementation(libs.logging.interceptor)
    implementation(libs.picasso)
    implementation(libs.volley)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.adapter.java8)
    implementation(libs.converter.gson)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.javax.annotation.api)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.glide)
    implementation(libs.androidx.core.splashscreen)


    implementation(libs.androidx.room.runtime)

    // Note: Choose only one of ksp or annotationProcessor. Don't include both.

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp(libs.androidx.room.compiler)

//     If this project only uses Java source, use the Java annotationProcessor
//     No additional plugins are necessary
//    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

}