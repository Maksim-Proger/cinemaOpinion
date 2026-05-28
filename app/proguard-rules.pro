# ----- Stack traces -----
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ----- Kotlin -----
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# ----- Retrofit -----
-keepattributes Signature
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# ----- OkHttp -----
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }
-keep interface okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# ----- Gson -----
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-dontwarn sun.misc.**

# ----- Data-классы API (Gson десериализует через рефлексию) -----
-keep class com.pozmaxpav.cinemaopinion.data.models.** { *; }
# Genre и Country — domain-модели, используемые напрямую в ApiMovieSearch/ApiMovieSearch2
-keep class com.pozmaxpav.cinemaopinion.domain.models.api.** { *; }

# ----- Firebase Realtime Database (no-arg конструкторы + поля) -----
# Data-модели (app модуль)
-keep class com.pozmaxpav.cinemaopinion.data.models.firebase.** { *; }
-keepclassmembers class com.pozmaxpav.cinemaopinion.data.models.firebase.** {
    <init>();
    <fields>;
}
# Domain-модели (app модуль) — используются напрямую в getValue()
-keep class com.pozmaxpav.cinemaopinion.domain.models.firebase.** { *; }
-keepclassmembers class com.pozmaxpav.cinemaopinion.domain.models.firebase.** {
    <init>();
    <fields>;
}
# DomainUserModel (core модуль) — используется в auth и app
-keep class com.example.core.domain.** { *; }
-keepclassmembers class com.example.core.domain.** {
    <init>();
    <fields>;
}
