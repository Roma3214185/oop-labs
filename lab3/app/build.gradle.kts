plugins {
    id("com.android.application")
}

android {
    namespace = "ua.maze"
    compileSdk = 35

    defaultConfig {
        applicationId = "ua.maze"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    // Timber тягне старий kotlin-stdlib-jdk8, appcompat — новий; BOM вирівнює версії
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.22"))

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
