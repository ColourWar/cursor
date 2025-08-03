# Add project specific ProGuard rules here.
-keep class com.monopoly.game.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}