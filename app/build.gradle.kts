import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.lisss79.weatherforecast"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.lisss79.weatherforecast"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val huaweiApiKey = properties.getProperty("HUAWEI_API_KEY")
        val yandexApiKey = properties.getProperty("YANDEX_API_KEY")
        val mapscoApiKey = properties.getProperty("MAPSCO_API_KEY")
        resValue("string", "HUAWEI_API_KEY", huaweiApiKey)
        resValue("string", "YANDEX_API_KEY", yandexApiKey)
        resValue("string", "MAPSCO_API_KEY", mapscoApiKey)

        setProperty("archivesBaseName", "WeaterForecast")
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
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material)

    // Navigation Component
    implementation(libs.androidx.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // HMS
    implementation(libs.play.services.location)
    implementation(libs.agconnect.core)
    implementation(libs.site)
    implementation(libs.location)
    implementation(libs.androidx.appcompat)

    // Compose Settings
    implementation(libs.ui.tiles)
    implementation(libs.ui.tiles.extended)

    // Preferences Datastore
    implementation(libs.androidx.datastore.preferences)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // DI Kodein
    implementation(libs.kodein.di)
    implementation(libs.kodein.di.framework.android.core)
    implementation(libs.kodein.di.framework.compose)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}