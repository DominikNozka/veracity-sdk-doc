# Android Veracity SDK
A powerfull item protection and item verification Veracity Protocol library for Android.

## Architecture overview
![Image of SDK](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/SDK.png)

### Input data
Veracity SDK takes care of the whole upload process. It allows you to automatically upload fingerprint image files, overview image and configurable data with easy interface for protection, verification and item search.

### Event data
SDK automatically observe data in our database and allows you to receive protected and verified objects as well as Firebase notifications.

### API data
Allows you to download data from our database. API includes automatic download, caching and data parsing.

### Detail capture
Veracity SDK include Activity that is able to capture item fingeprint in .png file. It includes augmented reality as well as manual guiding to obtain image fingerprints.

## Instalation
add the jitpack.io repository to your build.gradle file:
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
Note: Veracity SDK is still private repository, please contact jan@veracitiyprotocol.com for our permission

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
DetailActivity includes augmented reality as well as manual guiding to obtain .png image fingerprints.
<br/>
![](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/ar_gif.gif)
![](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/manual_gif.gif)

### Protection
Configure folder where sould be two fingerprint .png files saved and set overview path and size in detialConfig constructor
```kotlin
val detailConfigProtect = DetailConfig(captureType = DetailConfig.typeProtect,
                folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,overviewHeight = 30)
DetailActivity.launch(detailConfigProtect,this)
```
You will receive two fingerprint .png files with fingerprint location (DetailLocation) in JSON fromat onActivityResult
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
Verify detail capture requires DetailLocation. OnActivityResult you will receive only one fingerprint file that is needed for verification.
```kotlin
val detailLocation = DetailLocation(100,100,1500,2000)
            val detailConfigVerify = DetailConfig(captureType = DetailConfig.typeVerify,
                folder = File(m_path),jpegPath=overview.path,overviewWidth = 40,
                overviewHeight = 30,detailLocation = detailLocation)

DetailActivity.launch(detailConfigVerify,this)
```

## Notifications
In order to receive Firebase notifications and onSearchAnalyzingFinished, onVerifyAnalyzingFinished and onProtectAnalyzingFinished events you have to implement Firebase notifications.
<br/><br/>
1.) Implement VeracityFirebaseRegistraion interface in your Activity
```kotlin
class ExampleActivity:Activity(),VeracityFirebase.RegistrationListener{ ...
```
2.) Override registration success and failure events
```kotlin
override fun onSuccess() {}
override fun onFailure(error: String) {}
```
3.) Register Firebase token to our database
```kotlin
VeracityFirebase(context).registerToken("yourFirebaseToken",this)
```
4.) You can always check if the token is already registered
```kotlin
VeracityFirebase(context).isTokenRegistered()
```
5.) And finally in your FirebaseMessagingService let Veracity SDK read Firebase messages that you receive from FirebaseMessagingService
```kotlin
override fun  onMessageReceived(remoteMessage : RemoteMessage) {

    if (remoteMessage.getData().size() > 0) {
        VeracityFirebase(context).readMsg(remoteMessage.data)
    }
}
```
## Example App
Having issues with integration? Check out our [ExampleActivity](https://github.com/oneprove/android-veracity-sdk/blob/master/src/main/java/com/veracity/sdk/sample/SampleActivity.kt)

______________________________________________

# iOS VeracitySDK

VeracitySDK covers almost all the heavy work for Veracity apps. It can provide, persist and upload data, make API requests and more.

### Example
Run `pod instal` on example app folder and run....

### Instalation
```ruby
source 'https://github.com/CocoaPods/Specs.git'
source 'https://github.com/oneprove/specs-oneprove.git'

pod 'VeracitySDK'
```

#### Usage
`import VeracitySDK`

### API Token
Veracity SDK works only with logged user. To do so you need to login or register that user.
```swift
// Login.
NetworkClient.login(email: "loginEmail", password: "password") { (success, error) in
    if success {
        print("user was logged")
    }else {
        print(error?.localizedDescription)
    }
}
```

```swift
// Register new user.
NetworkClient.register(email: "newEmail", password: "password") { (success, error) in
    if success {
        print("user was registred")
    }else {
        print(error?.localizedDescription)
    }
}
```

### API data
Allows you to download data from our database. API includes automatic download, caching and data parsing.
#### Verifications
To get or refresh verification data.
```swift
NetworkClient.jobs { (jobs, error) in
    print(jobs.count)
}
```

or use `JobsReloadOperation`.

```swift
let operationQueue = OperationQueue()
let jobOP = JobsReloadOperation()
operationQueue.addOperation(jobOP)
```

#### Protections:
To get or refresh protect data.
```swift
NetworkClient.myProtectedItems { (items, error) in
    print(items.count)
}
```

or use `ProtectItemsReloadOperation`.
```swift
let operationQueue = OperationQueue()
let protectItemsOP = ProtectItemsReloadOperation()
operationQueue.addOperation(protectItemsOP)
```

### Input data
#### Taken images
Any taken image can be persisted and obtained by `ImagePersistenceManager`.
```swift
// Create data from taken image you wan't to persist.
let imageData = takenImage.pngData()
// Persist image data and get filename.
let imageFilename = try? ImagePersistence.saveImageToDisk(imageData: imageData)
```

```swift
// Obtain persisted image or data by filename.
let image = ImagePersistence.imageAtDisk(byFilename: "filename")
let imageData = ImagePersistence.imageDataAtDiskPath(byFilename: "filename")
```

#### Image Search
To create new image search, create new `LocalJob` with `.imageSearch` type and set overview and thumbnail filename and persist local job to database.
```swift
// Create new image search local job.
let newImageSearch = LocalJob(type: .imageSearch, item: nil)
// Set required properties.
newImageSearch.overviewImageFilename = "overview_filename"
newImageSearch.thumbnailImageFilename = "thumbnail_filename"
// Save to dabatase.
RealmHandler.shared.add(newImageSearch, modifiedUpdate: true)
// Upload start.
```

#### Verification
To create new verification of item, create new `LocalJob` with `.verification` and type item you wan't to verify, then append fingerprint filename a persist to database.
```swift
// Create new verification job with item agains you wan't to verify taken fingerprint images.
let newVerification = LocalJob(type: .imageSearch, item: verifingItem)
// Append fingerprint.
newVerification.appendFingerprintFilename(fingerprintImageFilename)
// Save to database.
RealmHandler.shared.add(newVerification, modifiedUpdate: true)
// Upload start.
```

#### Protection
To create new protection, create and fill `LocalProtectItem` and persist to database.
```swift
// Create new protect item.
let newProtect = LocalProtectItem()
newProtect.overviewImageFilename = "overview_filename"
newProtect.thumbnailImageFilename = "thumbnail_filename"
newProtect.width = 20
newProtect.height = 30
newProtect.name = "Item's name"
newProtect.year = 2019
newProtect.creator = Creator(firstName: "First", lastName: "Last")
// Save to database as incomplete.
RealmHandler.shared.add(newProtect, modifiedUpdate: true)
newProtect.fingerprintLocation = FingerprintLocation(rect: CGRect(x: 20, y: 20, width: 300, height: 500))
newProtect.appendFingerprintFilename(fingerprintImageFilename1)
newProtect.appendFingerprintFilename(fingerprintImageFilename2)
// Upload start.
```

### Upload
Upload is handled automaticaly by `UploadManager`. Local item upload starts when there are filled all the required properties. All you need to do is setup manager observing after app launch.
```swift
UploadManager.shared.startObserving()
```

### Events & Database change
Events are now provided only by observing for database change on one item or whole array of items as `Realm` feature.
To observe database change for any internal object stored in. Simply get data by `RealmHandler.shared.getObjects(ofType:)` and create notification token by `observe(changes:)` to setup observing.

Don't forget to `import RealmSwift`.

Example how to setup changes observing on filtered jobs data.
```swift
var observingToken : NotificationToken?
var verifications : Results<Job>?

// Get filtered & sorted jobs data.
verifications = RealmHandler.shared.getObjects(of: Job.self).filter("jobName == verify OR jobName == overviewSearch").sorted(byKeyPath: "createdAt", ascending: false)
observingToken = verifications?.observe({ [weak self] (changes) in
    guard let tableView = self?.tableView else { return }
    switch changes {
    case .initial:
        // First stage, data are loaded.
    case .update(_, let deletions, let insertions, let modifications):
        // Something has changed. There are passed arrays of indexes of items that was changed somehow. 
    case .error(let error):
        // Something went wrong.
        print("error: \(error)")
    }
    // Reload table view data every change.
    tableView.reloadData()
}

// When obseving is not needed anymore.
observingToken?.invalidate()
```

Example how to setup changes observing on filtered pretectItems data.
```swift
var observingToken : NotificationToken?
var protectItems : Results<ProtectItem>?

// Get sorted protectItems data.
protectItems = RealmHandler.shared.getObjects(of: ProtectItem.self).sorted(byKeyPath: "createdAt", ascending: false)
observingToken = protectItems?.observe({ [weak self] (changes) in
    guard let tableView = self?.tableView else { return }
    switch changes {
    case .initial:
        // First stage, data are loaded.
    case .update(_, let deletions, let insertions, let modifications):
        // Something has changed. There are passed arrays of indexes of items that was changed somehow. 
    case .error(let error):
        // Something went wrong.
        print("error: \(error)")
    }
    // Reload table view data every change.
    tableView.reloadData()
}

// When obseving is not needed anymore.
observingToken?.invalidate()
```

### Notifications
Notification are by default serviced by firebase. To setup Firebase notification follow up [official tutorial](https://firebase.google.com/docs/ios/setup).

To request user acces to notifications.
```swift
NotificationManager.shared.requestAccess()
```

To register notification token, pass token every app launch & token change to `NotificationManager`. 
```swift
func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    if let fcmToken = Messaging.messaging().fcmToken {
        NotificationManager.shared.didRegisterForFirebaseNotifications(withToken: fcmToken)
    }
}

// Messaging delegate method to get updated token.
func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
    NotificationManager.shared.didRegisterForFirebaseNotifications(withToken: fcmToken)
}
```
And to process received notification, pass it's payload to `NotificationManager`.
```swift
NotificationManager.shared.processReceivedNotification(userInfo: ["key" : "value"])
```
### Fingerprint capture
To capture fingerprint there are two ways.

#### AR
![AR](https://github.com/oneprove/android-veracity-sdk/blob/master/ar_gif.gif)

For more information & how to use [check repo](https://github.com/oneprove/ios-ARCameraController)

#### MAP
![MAP](https://github.com/oneprove/android-veracity-sdk/blob/master/manual_gif.gif)

For more information & how to use [check repo](https://github.com/oneprove/ios-MAPView)
