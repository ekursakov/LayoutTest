@file:Suppress("unused")

package com.github.ekursakov.headerslayout

object Dependencies {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    object AndroidX {
        const val core = "androidx.core:core:1.0.1"
        const val appCompat = "androidx.appcompat:appcompat:1.0.2"
        const val fragment = "androidx.fragment:fragment:1.0.0"
        const val transition = "androidx.transition:transition:1.0.0"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
        const val dynamicAnimation = "androidx.dynamicanimation:dynamicanimation:1.0.0"

        const val material = "com.google.android.material:material:1.0.0"
    }

    object Test {
        const val junit = "junit:junit:4.12"
    }

    object Plugins {
        const val android = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    }

    object Versions {
        const val compileSdk = 28
        const val minSdk = 16
        const val targetSdk = 28

        const val androidGradlePlugin = "3.2.1"

        const val kotlin = "1.3.21"
    }
}
