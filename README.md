# Android Veracity SDK
Veracity Protocol library for Android — a powerful item protection and verification.

## Architecture overview
![Image of SDK](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/SDK.jpg)

### Input data
Veracity SDK takes care of the whole upload process. It allows you to automatically upload fingerprint image files, overview image, and configurable data using a simple interface for protection, verification and item search.

### Event data
The SDK automatically checks data in our database and allows you to receive protected and verified objects as well as Firebase notifications.

### API data
Allows you to download data from our database. The API includes automatic download, caching and data parsing.

### Detail capture
Veracity SDK includes an Activity enabling to capture item fingerprint in the .png file format. It includes augmented reality, as well as manual guiding mechanism to obtain image fingerprints.

## Instalation
add the jitpack.io repository to your build.gradle file:
```groovy
allprojects {
   repositories {
      jcenter()
      maven {
              url "https://jitpack.io"
              credentials { username authToken }
          }
   }
}
```
add implementation of sdk to your build.gradle file:
```groovy
implementation 'com.github.DominikNozka:veracity-sdk:version'
```
Veracity SDK is still a private repository, please contact jan@veracitiyprotocol.org to obtain authToken
add the token to $HOME/.gradle/gradle.properties:
```groovy
authToken=insertYourObtainedTokenHere
```

add permissions, Activitiy and Service declaration to your AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<activity
      android:name="com.veracity.sdk.detail.DetailActivity"
      android:screenOrientation="portrait" />
      
<service
      android:name="com.veracity.sdk.api.uploader.UploadService"
      android:permission="android.permission.BIND_JOB_SERVICE"/>
```

## Requirements
Min SDK version: Android 8.0 (API level 26)
Camera: YUV support with 10MP resolution and higher

To check if your device has adequate camera call function:
```kotlin
AppReq.check(context)
```
## Authentication
1.) Implement ProtectEvent interface in your Activity
```kotlin
class ExampleActivity:Activity(),Authenticate.LoginListener{ ...
```
2.) Override methods in you Activity
```kotlin
override fun onLogInSuccess(){}
override fun onLogInFailure(error: String){}
```
3.) Authenticate with credentials
Note: please contact jan@veracitiyprotocol.org to obtain credentials ("Email","Password")
```kotlin
Authenticate(this@MainActivity).logIn("Email","Password",this)
```

## Input data (upload)
### Protection
1. Create ProtectAdd object
```kotlin
val protectAdd = ProtectAdd.Builder()
                  //all parameters are required
                .setArtistFirstname("Dominik")
                .setArtistLastname("Nožka")
                .setYear(1993)
                .setName("Test")
                .setWidth(30.0f)
                .setHeight(40.0f)
                .setThumbnail( File("path/to/thumbnail.jpg"))
                .setOverview(File("path/to/overview.jpg"))
                .setFingerprint1(File("path/to/fingeprint1.png"))
                .setFingerprint2(File("path/to/fingerprint2.png"))
                .setFingerprintLocation( " {\n" +
                        "        \"x\" : 260,\n" +
                        "        \"y\" : 166,\n" +
                        "        \"width\" : 139,\n" +
                        "        \"height\" : 184\n" +
                        "    }")
                .create()
```
2. Upload ProtectAdd object with Veracity SDK
```kotlin
VeracitySdk(context).upload(protectAdd)
```
### Verification
1. Create VerifyAdd object
```kotlin
val verifyAdd = VerifyAdd.Builder()
                  //required parameters
                .setFingerprint("path/to/fingeprint.png")
                .setArtworkId("5c30b27a507d460004bd8059")
                  //optional parameters
                .setThumbnailImage("path/to/thumbnail.jpg")
                .setOverviewImage("path/to/overview.jpg")
                .setArtworkPublicId("artPublicId")
                .setName("name")
                .setAuthorFirstName("authorFirstname")
                .setAuthorLastName("authorLastname")
                .setAdmin(false)
                .setOriginal(false)
                .setCreatedBy("")
                .create()

            VeracitySdk(this).upload(verifyAdd)
```
2. Upload VerifyAdd object with Veracity SDK
```kotlin
VeracitySdk(context).upload(verifyAdd)
```

## Event data
### Protect events
1.) Implement ProtectEvent interface in your Activity
```kotlin
class ExampleActivity:Activity(),ProtectEvent.EventListener{ ...
```
2.) Override methods in you Activity
```kotlin
override fun onProtectUploadingFinished(protectAdd: ProtectAdd){}
override fun onProtectUploadingStarted(protectAdd: ProtectAdd){}
override fun onProtectUploadingProgress(progress: Int, uploadSpeed: String, protectAdd: ProtectAdd) {}
override fun onProtectUploadingFailed(failReason:String,protectAdd: ProtectAdd){}
override fun onProtectAnalyzingFinished(protectGet: ProtectGet) {}
```
3.) Initialize ProtectEvent at onCreate in your Activity 
```kotlin
val protectEvent = ProtectEvent(context,this)
protectEvent.registerReceiver()
```
4.) Unregister ProtectEvent at onDestroy in your Activity 
```kotlin 
protectEvent.unregisterReceiver()
```

### Verification events
1.) Implement VerifyEvent interface in your Activity
```kotlin
class ExampleActivity:Activity(),VerifyEvent.EventListener{ ...
```
2.) Override methods in you Activity
```kotlin
override fun onVerifyUploadingStarted(verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingFailed(failReason: String, verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingProgress(progress: Int, uploadSpeed: String, verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingFinished(verifyAdd: VerifyAdd) {}
override fun onVerifyAnalyzingFinished(verifyGet: VerifyGet) {}
```
3.) Initialize VerifyEvent Object at onCreate in your Activity 
```kotlin
val verifyEvent = VerifyEvent(context,this)
verifyEvent.registerReceiver()
```
4.) Unregister VerifyEvent Object at onDestroy in your Activity 
```kotlin 
verifyEvent.unregisterReceiver()
```

## API data get
### Verifications 
```kotlin
val boolean getListFromCache=false
VerifyGetList(context, getListFromCache).Query {
     //You can iterate through-> it (it:ArrayList<VerifyGet>)       
}
```

### Protections 
```kotlin
val boolean getListFromCache=false
ProtectGetList(context, getListFromCache).Query {
      //You can iterate through-> it (it:ArrayList<ProtectGet>)         
}
```

### Image Cropp
Veracity SDK includes image cropp view with automatic edge detection and perspective correction 
<br/>
![](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/image_cropp.gif)<br/><br/>
Include CropImageView into your layout.xml file
```xml
<com.veracity.sdk.crop.CropImageView
            android:id="@+id/crop_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/black"
            android:layout_above="@+id/crop_btn"
    />
```
Implement CropEvent and PostionEvent interface in your Activity
```kotlin
class ActivityCropp:Activity(),CropImageView.CropEvent,CropImageView.PositionEvent{
```
Initialize CropView 
```kotlin
//input file needs to be in 4:3(H:W) aspect ratio
crop_view.findPositions(this,this, File("path_to_4:3_input_image_file"),File("file_path_to_image_result"),File("file_path_to_image_result_thumbnail"))
```
After onPositionsFound() method call, you can crop the image
```kotlin
crop_view.cropImage(this)
```
When the image is cropped, you'll get your cropped image file with thumbnail in jpeg image format in onImageCropped method
```kotlin
override fun onImageCropped(jpegCropped: File,thumbnail:File){ }
```
Don't forget to call recycle() onDestroy() to recycle images in crop_view
```kotlin
override fun onDestroy() {
   super.onDestroy()
   crop_view.recycle()
}
```
## Detail Caputure
DetailActivity includes augmented reality, as well as manual guiding mechanism to obtain image fingerprints in the .png file format.
<br/>
![](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/ar_gif.gif)
![](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/manual_gif.gif)

### Protection
Configure a folder with two fingerprint (.png files) and set an overview path and size in the detialConfig constructor.
```kotlin
val detailConfigProtect = DetailConfig(captureType = DetailConfig.typeProtect,
                folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,overviewHeight = 30)
DetailActivity.launch(detailConfigProtect,this)
```
You will receive two fingerprint .png files with the fingerprint location (DetailLocation) in the JSON format onActivityResult.
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

    if (requestCode == 1) {
        if (resultCode == Activity.RESULT_OK) {
            val result = data.getStringExtra("result")
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //result cancelled
        }
    }
}
```

### Verification
Verify detail capture requires Detail Location. OnActivityResult you will receive only one fingerprint file that is required for the verification.
```kotlin
val detailLocation = DetailLocation(100,100,1500,2000)
            val detailConfigVerify = DetailConfig(captureType = DetailConfig.typeVerify,
                folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,
                overviewHeight = 30,detailLocation = detailLocation)

DetailActivity.launch(detailConfigVerify,this)
```
### Demo project
If you are interested in cooperation. We will send you the demo project with your authentication token and credentials for fast and flawless integration into your project. Please contact jan@veracitiyprotocol.org for more info.
