# Sakuta(FullFuncationalCalc)
 
 This is the final project of Spring SE-017 Mobile applications class.
 
# Note
 
This is demo app for the final project in the class.


# Gradle(Module:app)
```
apply plugin: 'com.android.application'
apply plugin: 'com.google.gms.google-services'

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.3"

    defaultConfig {
        applicationId "com.example.sakuta"
        minSdkVersion 17
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.firebase:firebase-auth:19.3.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.facebook.android:facebook-android-sdk:[5,6)'
    implementation 'com.squareup.picasso:picasso:2.71828'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'com.google.android.material:material:1.0.0'

}
```


 
# Author
 
Name:Kazuhisa Noguchi,  
Belongs:EUAS Software Development,  
Email:kazuhisa.noguchi@eek.ee


# Photos
![スクリーンショット 2020-05-24 15 17 32](https://user-images.githubusercontent.com/31508821/82767157-0521b200-9e2e-11ea-8eab-af150dfb15d4.png)
![スクリーンショット 2020-05-25 2 31 27](https://user-images.githubusercontent.com/31508821/82767378-d9073080-9e2f-11ea-804f-abe559c6f219.png)
![スクリーンショット 2020-05-25 2 30 28](https://user-images.githubusercontent.com/31508821/82767357-b5dc8100-9e2f-11ea-936b-e770bf2a3a79.png)
