# Zapp Small Merchant Gateway Library Integration Documentation

## Introduction

This documentation provides a description about what is the **recommended way** the Merchant Apps to use the Zapp Small Merchant Library on Android platform.

The steps of starting a new Android project with the Zapp Small Merchant Gateway libraries are as follows:

1. Download the Zapp Small Merchant Gateway Library source code from GitHub
2. Start a new Android Studio project
3. Add Zapp Small Merchant Gateway Core Library to the project
4. Add Zapp Small Merchant Gateway Mcom Library to the project
5. Review dependencies, add dependency of Mcom library to main project

## Download the Zapp Small Merchant Gateway Library source code from GitHub

The source code of the library is publicly available on GitHub (https://github.com/vocalinkzapp/ZappSMGLib-Android). The first step of starting a new Android project with the library is to download its soruce code (https://github.com/vocalinkzapp/ZappSMGLib-Android/archive/master.zip). After downloading the source code as a .zip archive, uncompress it to a folder. In the next steps, we are going to import these sources to a new Android Studio project.

## Start a new Android Studio project

Start Android Studio then select 'Start a new Android Studio project' from the 'Welcome to Android Studio' dialog. Configure your new project freely (the only requirement for Zapp Small Merchant Gateway library is that the minimum SDK level cannot be lower than API 7). Once the new project has been started, you arrive to the main screen of Android Studio. The next step here is to import the Zapp Small Merchant Gateway libraries to the new Android project.

## Add Zapp Small Merchant Gateway Core Library to the project

To add the core library to the new Android project, the steps are the following:

- select "File" -> "New" -> "Import Module..."
- browse "zapp-smg-core-library" subfolder of the previously downloaded and uncompressed library archive into the "Source directory" field then select 'Choose' then 'Finish'
- observe that the "zapp-smg-core-library" appears in the modules list

The next step is to import the Mcom library source.

## Add Zapp Small Merchant Gateway Mcom Library to the project

To add the Mcom library to the new Android project, the steps are the following:

- select "File" -> "New" -> "Import Module..."
- browse "zapp-smg-mcom-library" subfolder of the previously downloaded and uncompressed library archive into the "Source directory" field then select 'Choose' then 'Finish'
- observe that the "zapp-smg-mcom-library" appears in the modules list

The next step is to review the dependency tree of the modules of the Android project and add the dependency of Mcom library to the main app module.

## Review dependencies, add dependency of Mcom library to main project

To review the dependency tree of the modules, the steps are the following:

- select "File" -> "Project structure..."
- observe that the Project Structure dialog contains 3 modules, namely 'app', 'zapp-smg-core-library', 'zapp-smg-mcom-library'
- select 'zapp-smg-mcom-library' module from the Modules list and select 'Dependencies' tab
- observe that ':zapp-smg-core-library' is listed in the dependencies list (if not then add it manually by clicking '+' -> 'Module dependency' -> ':zapp-smg-core-library' -> 'OK')
- select 'app' module from the Modules list and select 'Dependencies' tab
- add the Mcom library dependency to the main app project by clicking '+' -> 'Module dependency' -> ':zapp-smg-mcom-library' -> 'OK'
- select 'OK' to close the Project Structure dialog

## Next steps

After the core and mcom libraries have successfully been imported to the Android project the next step is to read the documentation of the Zapp Small Merchant Gateway mcom journey library [here](zapp-smg-mcom-library).