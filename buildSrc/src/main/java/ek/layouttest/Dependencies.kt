@file:Suppress("unused", "MayBeConstant")

package ek.layouttest

object Dependencies {
    val kotlinRuntime = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val rxJava2 = listOf(
            "io.reactivex.rxjava2:rxjava:${Versions.rxJava2}",
            "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid2}"
    )

    object Ui {
        val observableScrollView = "com.github.ksoichiro:android-observablescrollview:1.6.0"
    }

    object Support {
        val appCompat = "com.android.support:appcompat-v7:${Versions.supportLibs}"
        val transition = "com.android.support:transition:${Versions.supportLibs}"
        val design = "com.android.support:design:${Versions.supportLibs}"
        val fragment = "com.android.support:support-fragment:${Versions.supportLibs}"
        val recyclerView = "com.android.support:recyclerview-v7:${Versions.supportLibs}"

        val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
    }

    object Test {
        val junit = "junit:junit:4.12"
    }

    object Plugins {
        val android = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
        val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Versions {
        val compileSdk = 27
        val minSdk = 16
        val targetSdk = 27

        val androidGradlePlugin = "3.1.4"

        val kotlin = "1.2.41"

        val supportLibs = "27.1.1"
        val constraintLayout = "1.1.0"

        val rxJava2 = "2.1.13"
        val rxAndroid2 = "2.0.2"
    }
}
