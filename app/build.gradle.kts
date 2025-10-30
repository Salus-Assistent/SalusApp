// build.gradle.kts (Module :app Level)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Essencial para Hilt e Room
    alias(libs.plugins.hilt) // Plugin Hilt para o módulo
}

android {
    namespace = "com.example.salus"
    compileSdk = 35 // Define o SDK de compilação diretamente

    defaultConfig {
        applicationId = "com.example.salus"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // O vectorDrawables pode ser útil para ícones adaptativos
        // vectorDrawables {
        //     useSupportLibrary = true
        // }
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
        sourceCompatibility = JavaVersion.VERSION_11 // Mantenha consistente com seu JDK
        targetCompatibility = JavaVersion.VERSION_11 // Mantenha consistente com seu JDK
    }
    kotlinOptions {
        jvmTarget = "11" // Mantenha consistente com seu JDK
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Certifique-se que esta versão é compatível com sua versão do Kotlin
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ----- CORE & LIFECYCLE -----
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel Kotlin extensions
    implementation(libs.androidx.lifecycle.runtime.compose) // Para collectAsStateWithLifecycle

    // ----- COMPOSE -----
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // BOM gerencia versões do Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.text)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ----- NAVIGATION COMPOSE -----
    implementation(libs.androidx.navigation.compose)

    // ----- HILT (Dependency Injection) -----
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler) // Compilador Hilt
    implementation(libs.androidx.hilt.navigation.compose) // Integração Hilt + Navigation

    // ----- COROUTINES -----
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // ----- NETWORKING (Retrofit - Mesmo simulando, a estrutura é útil) -----
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Ou Moshi, Kotlinx Serialization
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor) // Para logs de rede

    // ----- IMAGE LOADING (Coil) -----
    implementation(libs.coil.compose)

    // ----- (Opcional - Para depois) MAPS -----
    // implementation(libs.maps.compose) // Exemplo: Google Maps Compose library

    // ----- TESTES -----
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}

// Permitir erros de duplicação que o Kapt/Hilt podem causar (geralmente necessário)
kapt {
    correctErrorTypes = true
}