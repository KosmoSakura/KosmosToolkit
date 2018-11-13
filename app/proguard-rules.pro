#          .======.
#          | INRI |
#          |      |
#   耶稣保佑|      |程序不崩
# .========'      '========.
# |   _      xxxx      _   |
# |  /_;-.__ / _\  _.-;_\  |
# |     `-._`'`_/'`.-'     |
# '========.`\   /`========'
#          | |  / |
#          |/-.(  |
#          |\_._\ |
#          | \ \`;|
#          |  > |/|
#          | / // |
#          | |//  |
#          | \(\  |
#          |  ``  |
#          |      |
#          |      |
# \\    _\\| \//  |//_   _ \// _
# ^ `^`^ ^`` `^ ^` ``^^`  `^^` `^ `^

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# 如果您的项目使用带有JS的WebView，请取消注释以下内容
# 并为JavaScript接口指定完全限定的类名
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 取消注释以保留用于调试堆栈跟踪的行号信息。
#-keepattributes SourceFile,LineNumberTable

# 如果保留行号信息，请取消注释以隐藏原始源文件名。
-renamesourcefileattribute SourceFile
#1.基本指令区
#指定代码的压缩级别 表示proguard对代码进行迭代优化的次数，Android一般为5
-optimizationpasses 5
#包明不混合大小写
#-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
#忽略警告
-ignorewarning
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguardMapping.txt
# 混淆时所采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*,InnerClasses
#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

#2.默认保留区 保持哪些类不被混淆
-keep public class cos.mos.antivirus.init.App
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.app.Dialog

#如果有引用v4包可以添加下面这行
#-keep class android.support.** {*;}
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
#保持 native 方法不被混淆
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#jsoup
#-keeppackagenames org.jsoup.nodes

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* { *; }
-keep public class **.R$*{
   public static final int *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

#3.webview
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#---------定制化区域----------------------------------------------
#--1.实体类---------------------------------
-keep class cos.mos.antivirus.dao.**{*;}
-keep class cos.mos.antivirus.widget.{*;}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
#######################     常用第三方模块的混淆选项         ###################################
#---------------------------------2.第三方包-------------------------------
# Location
-ignorewarnings
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}

-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

# --------------------------RecyclerView适配器混淆------------------------------------
-keep class com.chad.library.adapter.** { *; }
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
# --------------------------------Butternife------------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
   @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
   @butterknife.* <methods>;
}

# --------------------------------Gson------------------------------------
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
#--------------------------------Glide------------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#---------------------------------greendao----------------------------------
-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**
#-------------------------------zxing------------------------------
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}
#------------------------------okhttp-----------------------------
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *; }
-dontwarn okio.**
-dontwarn com.google.common.cache.**
-dontwarn java.nio.file.**
-dontwarn sun.misc.**
#------------------------------其他小东西-----------------------------
-keep class java.nio.file.** { *; }
-keep class sun.misc.** { *; }
#-------------------------------------------------------------------------
#---------------------------------3.与js互相调用的类------------------------
#暂无
#-------------------------------------------------------------------------
#---------------------------------4.反射相关的类和方法-----------------------
#暂无