# Home of Hope NGO Android Application

## XISD6329

The Home of Hope NGO Android application is designed to support **Home of Hope NGO (Siyazigabisa)** by providing a mobile platform for member engagement, donations, community support, and volunteer activities.

The application aims to connect the NGO with its community and encourage participation, transparency, and access to information.

## 🌟 Key Features

### 👤 Member Features

- **Registration and Login:** Multi-step member registration and authentication.
- **Personal Dashboard:** View donation totals, volunteer hours, and membership progress.
- **Membership Levels:** Membership tiers such as Seed, Hope, Silver, Gold, and Legacy.
- **Community Wall:** Share and view community news and updates.
- **Digital Membership Card:** Access a digital membership card containing membership details.
- **Causes and Events:** Browse NGO causes, impact information, volunteer events, and community gatherings.
- **Blog and News:** Read NGO articles, success stories, and newsletters.
- **Donations:** Access donation functionality and donation history.
- **Support and FAQs:** Find help information and contact support.

### 🛡️ Administration Features

- **Admin Portal:** Dedicated access for authorised NGO administrators.
- **Dashboard Analytics:** View information about membership, donations, and volunteer activity.
- **Member Management:** Search and manage member profiles and account status.
- **Volunteer Hours Management:** Record, verify, and update volunteer service hours.

### 🔒 Legal and Privacy Features

- **Privacy Policy:** Information about personal data handling.
- **Terms of Service:** Guidelines for using the application and community features.
- **Cookie Policy:** Information about local storage and preferences, where applicable.

> Feature descriptions represent the intended application functionality. Actual availability depends on the current implementation and configuration.

## 🧰 Technical Stack

| Component | Technology |
|---|---|
| Programming language | Kotlin |
| User interface | Jetpack Compose and Material 3 |
| Architecture | MVVM and Clean Architecture principles |
| Networking | Retrofit and OkHttp |
| Local database | Room |
| Session and preference storage | DataStore Preferences |
| Navigation | Jetpack Navigation Compose |
| Asynchronous operations | Kotlin Coroutines and Flow |
| Image loading | Coil |
| Animations | Lottie for Android |
| Splash screen | AndroidX SplashScreen API |
| Build system | Gradle |

### Technology References

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [Room](https://developer.android.com/training/data-storage/room)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Coil](https://coil-kt.github.io/coil/)
- [Lottie for Android](https://github.com/airbnb/lottie-android)
- [Android SplashScreen](https://developer.android.com/develop/ui/views/launch/splash-screen)

## 📁 Project Structure

The project is organised into an Android application module and Gradle configuration files.

The following is a conceptual overview of the application architecture. Actual folders may differ from this example.

```text
HomeOfHopeApp/
├── app/
├── gradle/
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── azure-pipelines.yml
└── README.md
```

Application code may be organised into the following logical layers, depending on the current implementation:

- **data/local/** — Local database entities, DAOs, and database configuration.
- **data/model/** — Data classes and models.
- **data/network/** — API services and network configuration.
- **data/repository/** — Data access and repository logic.
- **ui/screens/** — User interface screens.
- **ui/viewmodel/** — UI state and business logic.
- **ui/navigation/** — Navigation routes and graph.
- **ui/theme/** — Colours, typography, shapes, and Material 3 styling.
- **utils/** — Shared helper functions and utilities.

## 🚀 Getting Started

### Prerequisites

Before running the application, install:

- [Android Studio](https://developer.android.com/studio)
- Git
- A JDK version compatible with the project's Gradle and Android Gradle Plugin configuration
- The Android SDK and any required SDK components configured by the project

Check the project's Gradle configuration for the required Java version and Android minimum SDK. Do not assume these values without verifying the configuration.

### Clone the Repository

```bash
git clone https://github.com/CeejayAlughere/HomeOfHopeApp.git
cd HomeOfHopeApp
```

If you are using the Azure DevOps repository instead, clone the repository URL provided by your project under **Repos → Files → Clone**.

### Open the Project

1. Open Android Studio.
2. Select **Open** and choose the cloned project folder.
3. Allow Gradle synchronisation to finish.
4. Install any SDK components requested by Android Studio.
5. Select an emulator or connected Android device.
6. Run the application using the Android Studio Run button.

### Administrator Access

Use only administrator test credentials supplied through an authorised, secure channel. Do not store passwords, API keys, or other secrets in this README or source control.

## 🧪 Development and Testing

Run the following commands from the repository root on macOS or Linux.

### Build the Debug APK

```bash
chmod +x ./gradlew
./gradlew assembleDebug
```

The generated APK is normally located under:

```text
app/build/outputs/apk/debug/
```

### Run Unit Tests

```bash
./gradlew testDebugUnitTest
```
<<<<<<< HEAD

This command runs the debug unit-test task configured by Gradle. The actual test coverage depends on the tests present in the project.

### Run the Available Verification Tasks

```bash
./gradlew test
```

Use the test tasks supported by the project configuration. Review the Gradle output to determine whether tests were discovered and executed successfully.

## 🔄 Team Collaboration and CI/CD

The XISD6329 team uses Azure DevOps to coordinate development and maintain the source code.

### Azure Repos

Azure Repos provides Git-based source control for the Android application. Team members use branches to isolate changes and commits to record their work.

### Azure Boards

Azure Boards is used to create, assign, and track work items. Work items help the team organise tasks, monitor progress, and associate development work with project requirements.

### Azure Pipelines

Azure Pipelines automates the Android build and configured validation tasks. The pipeline is defined in `azure-pipelines.yml` and can be triggered by changes to the configured branch.

### Branching and Pull Requests

The team workflow is:

1. Create a feature branch from `main`.
2. Make changes related to an assigned work item.
3. Commit and push the changes to the feature branch.
4. Create a pull request targeting `main`.
5. Link the relevant Azure Boards work item.
6. Request review from another team member.
7. Wait for required reviews and build validation to pass.
8. Merge the pull request after all required policies are satisfied.

This workflow supports collaboration, traceability, and controlled integration of changes.


Keep this section accurate and update it as work is completed.

## 📌 Project Status

The repository includes an Android application, Gradle build configuration, and an Azure Pipelines YAML definition. Build and test outcomes should be verified from the relevant Azure Pipeline runs and Gradle logs.

## 📄 Licence

Refer to the repository's `LICENSE` file for the applicable licence terms.
=======
>>>>>>> d292b96 (updated ReadMe.md)
