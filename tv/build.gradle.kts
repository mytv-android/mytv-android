import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sentry.android.gradle)
}


android {
    @Suppress("UNCHECKED_CAST")
    apply(extra["appConfig"] as BaseAppModuleExtension.() -> Unit)

    namespace = "top.yogiczy.mytv.tv"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.mytv.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = "${System.getenv("VERSION_CODE")}".toInt()
        versionName = "1.0.1.${System.getenv("VERSION_CODE")}"//.${System.getenv("COMMIT_HASH")}"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "SENTRY_DSN", "\"${getProperty("sentry.dsn") ?: System.getenv("SENTRY_DSN")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")

            ndk {
                abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            }
        }
        debug{
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            ndk {
                abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
            }
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("jniLibs")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += listOf("version")
    productFlavors {
        create("original") {
            dimension = "version"
        }

        create("disguised") {
            dimension = "version"
            applicationId = "com.chinablue.tv"
        }
    }

    // splits {
    //     abi {
    //         isEnable = true
    //         isUniversalApk = false
    //         reset()
    //         // noinspection ChromeOsAbiSupport
    //         include("arm64-v8a")
    //     }
    // }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation.base)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.android.material)
    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.material.icons.extended)

    // 播放器
    val mediaSettingsFile = file("../../media/core_settings.gradle")
    if (mediaSettingsFile.exists()) {
        implementation(project(":media3:lib-exoplayer"))
        implementation(project(":media3:lib-exoplayer-hls"))
        implementation(project(":media3:lib-exoplayer-rtsp"))
        implementation(project(":media3:lib-exoplayer-dash"))
        implementation(project(":media3:lib-ui"))
    } else {
        implementation(libs.androidx.media3.exoplayer)
        implementation(libs.androidx.media3.exoplayer.hls)
        implementation(libs.androidx.media3.exoplayer.rtsp)
        implementation(libs.androidx.media3.exoplayer.dash)
        implementation(libs.androidx.media3.ui)
    }
    implementation(libs.androidx.media3.datasource.rtmp)
    implementation(libs.androidx.media3.exoplayer.smoothstreaming)

    implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-java:v10.0.0")
    // implementation("com.github.CarGuo.GSYVideoPlayer:gsyvideoplayer-ex_so:v10.0.0")

    // 二维码
    implementation(libs.qrose)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    implementation(libs.okhttp)
    implementation(libs.androidasync)

    implementation(libs.tinypinyin)

    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:util"))
    implementation(project(":allinone"))
    implementation(project(":gsyvideoplayer-ex_so"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

sentry {
    org.set("mytv-android")
    projectName.set("mytv")
    authToken.set(getProperty("sentry.auth_token") ?: System.getenv("SENTRY_AUTH_TOKEN"))
    ignoredBuildTypes.set(setOf("debug"))
    autoUploadProguardMapping = false
}

fun getProperty(key: String): String? {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(propertiesFile))

        return properties.getProperty(key)
    }

    return null
}
