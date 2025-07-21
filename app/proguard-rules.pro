# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.android.boilerplate.base.model.data.local.BaseModel
-keep class com.android.boilerplate.base.model.data.remote.request.BaseRequest
-keep class com.android.boilerplate.base.model.data.remote.response.BaseResponse

-keep class com.android.boilerplate.base.model.data.remote.response.Result { *; }

-keep class com.android.boilerplate.model.data.aide.Language {
*** id;
*** lang;
*** name;
*** selected;
}

# https://r8.googlesource.com/r8/+/refs/heads/main/compatibility-faq.md#troubleshooting-gson-gson
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken