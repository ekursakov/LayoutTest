package ek.layouttest;

@SuppressWarnings("unused")
public class Deps {
    public static final String kotlinRuntime = "org.jetbrains.kotlin:kotlin-stdlib-jre7:" + Versions.kotlin;

    public static final String rxJava2 = "io.reactivex.rxjava2:rxjava:" + Versions.rxJava2;
    public static final String rxAndroid2 = "io.reactivex.rxjava2:rxandroid:" + Versions.rxAndroid2;

    //region Workaround for groovy
    public static final Support Support = new Support();
    public static final Test Test = new Test();
    public static final Plugins Plugins = new Plugins();
    //endregion

    public static class Support {
        public final static String appCompat = "com.android.support:appcompat-v7:" + Versions.supportLibs;
        public final static String transition = "com.android.support:transition:" + Versions.supportLibs;
        public final static String design = "com.android.support:design:" + Versions.supportLibs;
        public final static String fragment = "com.android.support:support-fragment:" + Versions.supportLibs;

        public final static String constraintLayout = "com.android.support.constraint:constraint-layout:" + Versions.constraintLayout;
    }

    public static class Test {
        public final static String junit = "junit:junit:4.12";
    }

    public static class Plugins {
        public static final String androidGradlePlugin = "com.android.tools.build:gradle:" + Versions.androidGradlePlugin;
        public static final String kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:" + Versions.kotlin;
    }

    public static class Versions {
        public static final int compileSdk = 27;
        public static final int minSdk = 16;
        public static final int targetSdk = 27;

        public static final String androidGradlePlugin = "3.1.2";

        public static final String kotlin = "1.2.41";

        public static final String supportLibs = "27.1.1";
        public static final String constraintLayout = "1.1.0";

        public static final String rxJava2 = "2.1.13";
        public static final String rxAndroid2 = "2.0.2";

        public static final String retrofit = "2.4.0";

        public static final String dagger = "2.16";
    }
}
