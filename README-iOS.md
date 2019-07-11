# iOS VeracitySDK

Veracity SDK covers almost all the heavy work for the Veracity Apps. It can provide, persist and upload data, make API requests, and more.

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
Veracity SDK works only with a logged-in user. Therefore, you need to allow the user to register and log in.
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
Upload is handled automatically by the `UploadManager`. Local item upload starts when there are filled all the required properties. All you need to do is setup the manager observing after app launch.
```swift
UploadManager.shared.startObserving()
```

### Events & Database change
Events are now provided only by observing for a database change on one item, or whole array of items, as a `Realm` feature.
To observe a database change for any internal object stored in, simply get data by `RealmHandler.shared.getObjects(ofType:)` and create a notification token by `observe(changes:)` to setup observing.

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

Example of how to setup changes observing on filtered pretectItems data.
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
By default, notification are serviced by Firebase. To setup the Firebase notifications follow up [official tutorial](https://firebase.google.com/docs/ios/setup).

To request user access to notifications.
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
There are two ways to capture a fingerprint.

#### AR
![AR](https://github.com/oneprove/android-veracity-sdk/blob/master/ar_gif.gif)

For more information & how to use [check readme](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/README_AR.md)

#### MAP
![MAP](https://github.com/oneprove/android-veracity-sdk/blob/master/manual_gif.gif)

For more information & how to use [check readme](https://github.com/DominikNozka/veracity-sdk-doc/blob/master/README_MAP.md)
