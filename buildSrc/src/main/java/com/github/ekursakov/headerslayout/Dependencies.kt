@file:Suppress("unused")

package com.github.ekursakov.headerslayout

object Dependencies {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    object Support {
        const val coreUi = "com.android.support:support-core-ui:${Versions.supportLibs}"
        const val appCompat = "com.android.support:appcompat-v7:${Versions.supportLibs}"
        const val transition = "com.android.support:transition:${Versions.supportLibs}"
        const val design = "com.android.support:design:${Versions.supportLibs}"
        const val fragment = "com.android.support:support-fragment:${Versions.supportLibs}"
        const val recyclerView = "com.android.support:recyclerview-v7:${Versions.supportLibs}"
    }

    object Test {
        const val junit = "junit:junit:4.12"
    }

    object Plugins {
        const val android = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Versions {
        const val compileSdk = 27
        const val minSdk = 16
        const val targetSdk = 27

        const val androidGradlePlugin = "3.1.4"

        const val kotlin = "1.2.41"

        const val supportLibs = "27.1.1"
        const val constraintLayout = "1.1.0"

        const val rxJava2 = "2.1.13"
        const val rxAndroid2 = "2.0.2"
    }
}
