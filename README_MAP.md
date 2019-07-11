MAPView
MAPView is a subclass of UIView that represents manual acquisition method of the fingerprint (artwork detail). Position of the fingerprint in an artwork overview is determinated by FingerprintFinder which is automatically required as dependency. Internally, the instance of the class controls an acquisition of the fingerprint using AVFoundation framework.

Example
To run the example project, clone the repo, and run pod install from the Example directory first.

Requirements
iOS 11.0+
Swift 4.0+
OpenCV 3+
Installation
MAPView is available through CocoaPods. To install it, simply add the following line to your Podfile:

source 'https://github.com/CocoaPods/Specs.git'
source 'https://github.com/oneprove/specs-oneprove.git'

pod 'MAPView'
To suppress OpenCV warnings set Documentation Comments to NO in Build Settings.

Usage
If you don't know the position of the fingerprint in overview image call:

mapView.delegate = self
mapView.locateFingerprint(overviewImage: image, overviewSize: size) { (status, fingerprintRect) in
    // Completion
    checkFingerprintStatus(status)
    print("Original fingerprint rect in overview image: \(fingerprintRect)")
}
If you know the position of the fingerprint in overview image call:

mapView.delegate = self
mapView.locateFingerprint(overviewImage: image, fingerprintRect: size) {
    // Completion
}
If you want to change zoom of underlying overview image call following method with parameter value in range 0.0 to 1.0:

mapView.changeZoom(value: value)
To capture photo call:

mapView.captureFingerprintPhoto()
Captured photo of fingerprint is available thru CameraPreviewDelegate delegate method:

func didCapturePhoto(_ photo: UIImage?, error: Error?) {
    guard let photo = photo else {
        print(error?.localizedDescription ?? "Unknown error.")
        return
    }

    print("Photo captured: \(photo.size)")
}
Author
Petr Bobak, PetrBobak@me.com

License
MAPView is available under the MIT license. See the LICENSE file for more info.
