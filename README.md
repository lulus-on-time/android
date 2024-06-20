## Installation Guide

### Prerequisites

1. Map Server should already be running and reachable by a URL. Installation Guide can be found in [Installation Guide](https://github.com/lulus-on-time/backend)
2. Fingerprint Processing server should already be running and reachable by a URL. Installation Guide can be found in [Installation Guide](https://github.com/lulus-on-time/backend)
3. This guide is specific to Android Studio IDE. However, the steps to run the application using another IDE should not be too different

### Steps

1. Use a terminal, and clone this repository with the command

```
git clone https://github.com/lulus-on-time/backend.git \<folder name>
```

2. Open the folder in Android Studio
3. Let Android Studio build the project. This process should run automatically, but can also be manually triggered by going to the menu Build -> Make Project.
4. A local.properties file should now be created automatically. If not, create a local.properties file and leave it blank.
5. Add the lines

```
WSURL=<url of fingerprint processing server> example: ws://35.219.65.61
APIURL=<url of map server> example: http://35.219.115.59
```

6. To run the application in Android Studio, go to the menu Run -> Run, and choose or create a new emulator. Do note that running the application on an emulator will not work, as Android emulators do not have access to real wifi scanners such as a real smartphones, and thus will not be able to accurately find your position.
7. To create an apk file to be installed in a real Android smartphone, go to the menu Build -> Build Bundle(s) / APK(s) -> Build APK(s). After the file is created, that file can be shared and be installed on an Android Phone.

## User Manual

### Prerequisites

1. Turn on location permissions in the settings app
2. (Optional) Turn on Developer Mode in your Android smartphone. In Developer options, look for the setting Wi-Fi scan throttling and turn it off. This is to allow user location updates every second, instead of the default rate of 4 times every two minutes

### Location Prediction

1. Open the application
2. Click Allow when a prompt to "Allow FindMyself to access this device's precise location?" appears
3. Click Allow all the time / Always allow, when a second prompt appears regarding precise location access
4. As you traverse the location that the Indoor Positioning System is used, a blue indicator indicating your location will also update

### Authentication (To Build the Radio map)

1. A button on the bottom right can be clicked that will bring you to the authentication screen
2. Input your npm, that will be used as an authenticator, that identifies you from other users (This is to allow your fingerprint data to be collected based on your university schedule)
3. Click Save to bring you back to the location prediction screen
4. While you have a class, the application will automatically take your fingerprint to improve the accuracy of the Indoor Positioning System
