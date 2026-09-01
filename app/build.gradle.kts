plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "br.com.fiap.EcoWatts"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "br.com.fiap.ecowatts"
        minSdk = 28
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

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
    }
}
// TRECHO DE CÓDIGO OMITIDO
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Dependências do Navigation Compose
    // e Kotlinx Serialization Json
    implementation(libs.androidx.navigation.compose)
    //implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Biblioteca de ícones adicionais
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

    //animação
    implementation("com.airbnb.android:lottie-compose:6.7.1")


    // Room dependencies
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // Retrofit e conversor JSON
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Interceptor para visualizar os logs da API
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coil para carregar imagens assincronamente no Compose
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("androidx.core:core-splashscreen:1.0.1")
}