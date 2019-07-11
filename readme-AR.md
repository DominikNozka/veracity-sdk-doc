# ARCameraController

ARCameraController is a class that represents process of fingerprint (artwork detail) acquisition. Internally, the instance of the class controls frame extraction from the camera stream and acquisition of the artwork detail – both using AVFoundation framework. Each frame is processed using OpenCV 3 which is required by podspec file. Position of the fingerprint in an artwork overview is determinated by [FingerprintFinder](https://github.com/oneprove/ios-FingerprintFinder) which is automatically required as dependency. The fingerprint is captured multiple times then the sharpest image of the artwork detail is selected for further use.

## Example

To run the example project, clone the repo, and run `pod install` from the Example directory first.

## Requirements

- iOS 11.0+
- Swift 4.0+
- OpenCV 3+

## Installation

ARCameraController is available through [CocoaPods](http://cocoapods.org). To install
it, simply add the following lines to your Podfile:

```ruby
source 'https://github.com/CocoaPods/Specs.git'
source 'https://github.com/oneprove/specs-oneprove.git'

pod 'ARCameraController'
```

To suppress OpenCV warnings set _Documentation Comments_ to __NO__ in Build Settings.

## Usage

#### 1. Initial setup

If you don't know the position of the fingerprint in overview image call:

```swift
cameraController = ARCameraController(overviewImage: overviewImage, overviewSize: overviewSize, completion: { (status, fingerprintRect) in
    self.activityIndicator.stopAnimating()
    self.checkStatus(status)

    print("Located fingerprint: \(fingerprintRect)")
})
```

If you know the position of the fingerprint in overview image call:

```swift
    self.cameraController = ARCameraController(overviewImage: overviewImage, fingerprintRect: fingerprintRect, completion: { (status) in
    print(status)
    self.activityIndicator.stopAnimating()
    self.checkStatus(status)
})
```

Don't forget to set  `delegate`  and `previewView` property after initialization:
```swift
self.cameraController?.delegate = self
self.cameraController?.previewView = self.cameraImageView
```

#### 2. Start and Stop

If yout need to stop the ARCameraController instance call:
```swift
cameraController?.stop()
```
If yout need to start the ARCameraController instance call:

```swift
cameraController?.stop()
```
#### 3. ARCameraControllerDelegate
```swift
func stateDidChange(state: ARCameraControllerState) {
    print("stateDidChange: \(state)")
}
```
Notifies the delegate that the current state of the AR navigation has been changed.

```swift
func willCapturePhoto(progress: Progress) {
    print("willCapturePhoto: \(progress.description)")
}
```
Notifies the delegate that the acquisition of the artwork detail is about to begin.

```swift
func didCapturePhoto(photo: UIImage, progress: Progress) {
    print("didCapturePhoto: \(photo.size) \(progress.description)")
}
```
Notifies the delegate that the acquisition of the artwork detail has been completed.

```swift
func bestEvaluatedPhotos(photos: [UIImage]) {
    print("bestEvaluatedPhotos: \(photos[0].size) and \(photos[1].size)")
}
```
Notifies the delegate that the two sharpest images of the artwork detail are available.

```swift
    func shouldResetAutomatically(sender: ARCameraController) {
        print("shouldResetAutomatically")
        
        // If tracking reset is desired call reset method (see the statement below).
        sender.reset()
    }
```
Notifies the delegate that tracking is lost longer than defined amount of time (5s).

#### 4. Flashlight

To check if flashlight is available call:
```swift
cameraController.flashlightAvailable()
```

To toggle flashlight with maximal intensity call:
```swift
let flashlightOn = cameraController.toggleFlashlight()
```

To toggle flashlight with custom intensity call method with parameter `level` in range **0.0** to **1.0** 
```swift
let flashlightOn = cameraController.toggleFlashlight(level: 0.5)
```

## Author

Petr Bobak, PetrBobak@me.com

## License

ARCameraController is available under the MIT license. See the LICENSE file for more info.
