import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.tasks.PackageAndroidArtifact
import java.io.FileInputStream
import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

val keystorePropertiesFile = rootProject.file("signing.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

allprojects {
    val appConfig: BaseAppModuleExtension.() -> Unit = {
        signingConfigs {
            val localKeystore = rootProject.file("keystore.jks")
            val userKeystore = file(
                System.getenv("KEYSTORE") ?: keystoreProperties.getProperty("storeFile")
                ?: "keystore.jks"
            )

            create("release") {
                storeFile = if (userKeystore.exists()) userKeystore else localKeystore
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                    ?: keystoreProperties.getProperty("storePassword")
                keyAlias = System.getenv("KEY_ALIAS") ?: keystoreProperties.getProperty("keyAlias")
                keyPassword =
                    System.getenv("KEY_PASSWORD") ?: keystoreProperties.getProperty("keyPassword")
            }
        }

        applicationVariants.all {
            outputs.all {
                val ver = defaultConfig.versionName
                val minSdk = defaultConfig.minSdk
                val abi = filters.find { it.filterType == "ABI" }?.identifier ?: "all"
                val flavor = productFlavors.joinToString("-") { it.name }

                (this as BaseVariantOutputImpl).outputFileName =
                    "mytv-android-${project.name}-$ver-${abi}-sdk$minSdk-${flavor}.apk"
            }
        }
    }

    extra["appConfig"] = appConfig

    tasks.withType<PackageAndroidArtifact>().configureEach {
        doLast {
            val outputDir = outputDirectory.get().asFile
            val targetDir = file("$outputDir/../../release")

            if (!targetDir.exists()) targetDir.mkdirs()

            outputDir.listFiles { file -> file.extension == "apk" }?.forEach { apkFile ->
                apkFile.copyTo(file("${targetDir}/${apkFile.name}"), overwrite = true)
            }
        }
    }
}