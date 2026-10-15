plugins {
    id("com.android.application")
}

android {
    namespace = "com.sumoo.ajt124"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sumoo.ajt124"
        minSdk = 23
        targetSdk = 36
        versionCode = 3
        versionName = "1.2"
    }
}

// Keep compileSdk and targetSdk aligned with Android 16 (API 36); verify via lintDebug in CI.
