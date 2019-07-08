# Android Veracity SDK
Veracity Protocol library for Android — a powerful item protection and verification.

## Architecture overview
![Image of SDK](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/SDK.png)

### Input data
Veracity SDK takes care of the whole upload process. It allows you to automatically upload fingerprint image files, overview image, and configurable data using a simple interface for protection, verification and item search.

### Event data
The SDK automatically checks data in our database and allows you to receive protected and verified objects as well as Firebase notifications.

### API data
Allows you to download data from our database. The API includes automatic download, caching and data parsing.

### Detail capture
Veracity SDK includes an Activity enabling to capture item fingerprint in the .png file format. It includes augmented reality, as well as manual guiding mechanism to obtain image fingerprints.

## Instalation
Add the jitpack.io repository to your build.gradle file:
```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```
and add implementation of sdk to your build.gradle file:
```
implementation 'com.github.DominikNozka:veracity-sdk:1.0.5'
```
Note: Veracity SDK is still a private repository, please contact jan@veracitiyprotocol.com to obtain a permission.

## Input data (upload)
### Protection
1. Create ProtectAdd object
```kotlin
val protectAdd = ProtectAdd.Builder()
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
                .setFingerprint("path/to/fingeprint.png")
                .setThumbnailImage("path/to/thumbnail.jpg")
                .setOverviewImage("path/to/overview.jpg")
                .setArtworkId("5c30b27a507d460004bd8059")
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

### Image Search
1. Create VerifyAdd object
```kotlin
 val searchAdd = SearchAdd.Builder()
                .setOverviewImg("path/to/overview.jpg")
                .setThumbnailImg("path/to/thumbnail.jpg")
                .create()
            
```
2. Upload SearchAdd object with Veracity SDK
```kotlin
VeracitySdk(context).upload(searchAdd)
```
## Event data
### Protect events
1.) Implement ProtectEvent interface in your Activity
```kotlin
class ExampleActivity:Activity(),ProtectEvent.EventListener{ ...
```
2.) Initialize ProtectEvent at onCreate in your Activity 
```kotlin
val protectEvent = ProtectEvent(context,this)
protectEvent.registerReceiver()
```
3.) Override methods in you Activity
```kotlin
override fun onProtectUploadingFinished(protectAdd: ProtectAdd){}
override fun onProtectUploadingStarted(protectAdd: ProtectAdd){}
override fun onProtectUploadingProgress(progress: Int, uploadSpeed: String, protectAdd: ProtectAdd) {}
override fun onProtectUploadingFailed(failReason:String,protectAdd: ProtectAdd){}
override fun onProtectAnalyzingFinished(protectGet: ProtectGet) {}
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
2.) Initialize VerifyEvent Object at onCreate in your Activity 
```kotlin
val verifyEvent = VerifyEvent(context,this)
verifyEvent.registerReceiver()
```
3.) Override methods in you Activity
```kotlin
override fun onVerifyUploadingStarted(verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingFailed(failReason: String, verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingProgress(progress: Int, uploadSpeed: String, verifyAdd: VerifyAdd) {}
override fun onVerifyUploadingFinished(verifyAdd: VerifyAdd) {}
override fun onVerifyAnalyzingFinished(verifyGet: VerifyGet) {}
```
4.) Unregister VerifyEvent Object at onDestroy in your Activity 
```kotlin 
verifyEvent.unregisterReceiver()
```

### Search events
1.) Implement SearchEvent interface in your Activity
```kotlin
class ExampleActivity:Activity(),SearchEvent.EventListener{ ...
```
2.) Initialize SearchEvent Object at onCreate in your Activity 
```kotlin
val searchEvent = SearchEvent(context,this)
searchEvent.registerReceiver()
```
3.) Override methods in you Activity
```kotlin
override fun onSearchUploadingStarted(searchAdd: SearchAdd) {}
override fun onSearchUploadingFailed(failReason: String, searchAdd: SearchAdd) {}
override fun onSearchUploadingProgress(progress: Int, uploadSpeed: String, searchAdd: SearchAdd) {}
override fun onSearchUploadingFinished(searchAdd: SearchAdd) {}
override fun onSearchAnalyzingFinished(verifyGet: VerifyGet) {}
```
4.) Unregister SearchEvent Object at onDestroy in your Activity 
```kotlin 
searchEvent.unregisterReceiver()
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

## Notifications
In order to receive the Firebase notifications and onSearchAnalyzingFinished, onVerifyAnalyzingFinished and onProtectAnalyzingFinished events, you have to implement the Firebase notifications.
<br/><br/>
1.) Implement the VeracityFirebaseRegistraion interface in your Activity
```kotlin
class ExampleActivity:Activity(),VeracityFirebase.RegistrationListener{ ...
```
2.)  Override the registration success and failure events
```kotlin
override fun onSuccess() {}
override fun onFailure(error: String) {}
```
3.) Register the Firebase token to our database
```kotlin
VeracityFirebase(context).registerToken("yourFirebaseToken",this)
```
4.) You can always check whether the token is already registered
```kotlin
VeracityFirebase(context).isTokenRegistered()
```
5.) And finally, in your FirebaseMessagingService, let Veracity SDK read the Firebase messages that you receive from FirebaseMessagingService
```kotlin
override fun  onMessageReceived(remoteMessage : RemoteMessage) {

    if (remoteMessage.getData().size() > 0) {
        VeracityFirebase(context).readMsg(remoteMessage.data)
    }
}
```
## Example App
Having issues with the integration? Check out our [ExampleActivity](https://github.com/oneprove/android-veracity-sdk/blob/master/src/main/java/com/veracity/sdk/sample/SampleActivity.kt)
