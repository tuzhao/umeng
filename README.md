# umeng
a umeng library for android.

# use steps 1
add following code in your project root build.gradle file. both buildscript and allprojects place.

```gradle 
buildscript {

    repositories {
        ......
        maven { url 'https://dl.bintray.com/umsdk/release' }
        ......
    }
}

allprojects {
    repositories {
        ......
        maven { url 'https://dl.bintray.com/umsdk/release' }
        ......
    }
}
```

# use step 2
如果您的应用使用了代码混淆，请添加如下配置，以避免【友盟+】SDK被错误混淆导致SDK不可用。
```gradle
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
```

SDK需要引用导入工程的资源文件，通过了反射机制得到资源引用文件R.java，但是在开发者通过proguard等混淆/优化工具处理apk时，proguard可能会将R.java删除，如果遇到这个问题，请添加如下配置：
```gradle
-keep public class [您的应用包名].R$*{
public static final int *;
}
```
