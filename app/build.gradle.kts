import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.example.petrescuechristian"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.petrescuechristian"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // La API key se guarda en local.properties (no se sube a control de versiones)
        // y se inyecta aquí en vez de dejarla escrita en el manifest.
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY", "")

        // Credenciales de PostgreSQL: mismo patrón, vienen de local.properties (no versionado).
        buildConfigField("String", "DB_HOST", "\"${localProperties.getProperty("DB_HOST", "")}\"")
        buildConfigField("int", "DB_PORT", localProperties.getProperty("DB_PORT", "5432"))
        buildConfigField("String", "DB_NAME", "\"${localProperties.getProperty("DB_NAME", "")}\"")
        buildConfigField("String", "DB_USER", "\"${localProperties.getProperty("DB_USER", "")}\"")
        buildConfigField("String", "DB_PASSWORD", "\"${localProperties.getProperty("DB_PASSWORD", "")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/DEPENDENCIES"
            )
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.postgresql)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}