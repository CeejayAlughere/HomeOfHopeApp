# Home of Hope Android

[![Android CI](https://github.com/CeejayAlughere/HomeOfHopeApp/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/CeejayAlughere/HomeOfHopeApp/actions/workflows/android-ci.yml)

Home of Hope is a Kotlin Android app built for the XISD6329 team project supporting **Home of Hope NGO (Siyazigabisa)**. Members can sign in, browse fundraising causes, record a donation in South African rand and view their donation history.

The Android app connects to the same PHP and MySQL backend as the Home of Hope website. When a member records a donation on their phone, the NGO can see the selected cause and amount in the website’s admin dashboard. The app records contributions without charging the member’s bank account.

**[Watch the project demonstration on YouTube](https://youtu.be/nTzSagurRYw?si=3KbYH4IJGE84doZ4)**

## How a donation works

1. A member signs in, opens the causes list and chooses a fundraiser.
2. They select their bank, enter an amount in rand and tap **Record donation**.
3. The app submits the record to the website API. Once the API confirms it was saved, the app shows a confirmation and refreshes the member’s donation history.
4. The donation appears in the website’s admin dashboard against the same cause.

If connectivity drops, the form shows an error and the member can retry. The form keeps the same request ID for that retry. Opening a fresh form creates a new ID, so a member can make a second contribution for the same amount.

## Inside the Android app

The screens use **Kotlin, Jetpack Compose and Material 3**. ViewModels manage form validation and screen state. Repositories use **Retrofit and OkHttp** to call the PHP API. The app also uses **Room** for local data and **DataStore** for session information.

Google sign-in uses Android Credential Manager and Firebase Authentication. Members can also use their Home of Hope website account to sign in to the app.

Three files show how the donation flow fits together:

- [DonationScreen.kt](app/src/main/java/com/homeofhope/app/ui/screens/donation/DonationScreen.kt) collects the cause, bank and amount and gives each donation attempt a request ID.
- [DonationViewModel.kt](app/src/main/java/com/homeofhope/app/ui/viewmodel/DonationViewModel.kt) validates the form, blocks repeat taps during submission and updates the screen when the request finishes.
- [DonationRepository.kt](app/src/main/java/com/homeofhope/app/data/repository/DonationRepository.kt) sends the authenticated request and retrieves the member’s donation history from the website API.

## Run locally

Clone the repository and open the project folder in Android Studio:

```bash
git clone https://github.com/CeejayAlughere/HomeOfHopeApp.git
cd HomeOfHopeApp
```

1. Let Gradle sync and install the Android SDK components requested by the project. The GitHub Actions build uses **JDK 17**.
2. For live causes, member accounts and donations, run the companion Home of Hope PHP/MySQL backend. Check the API base URL in [AuthRetrofitClient.kt](app/src/main/java/com/homeofhope/app/data/network/AuthRetrofitClient.kt) and make sure your emulator or device can reach it. On the standard Android emulator, `10.0.2.2` reaches a backend running on your development machine.
3. Select the `app` run configuration, choose an emulator or connected device, and run the app. To test Google sign-in with your own Firebase project, configure Google authentication for your Android application ID and signing certificate.

## Build and test

Run the same checks as GitHub Actions from the repository root:

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

The [Android CI workflow](.github/workflows/android-ci.yml) builds the debug APK, runs unit tests and checks Android lint on pull requests to `main` and on pushes to `main`. The unit-test suite includes nine validation tests.

We also tested the donation flow across the Android app and website. A rapid double tap produced one record. Opening a fresh form and deliberately submitting another **R100** donation to the same cause produced a second record with a different request ID.

## Team project

Home of Hope was developed collaboratively for **XISD6329**. The team used Azure Boards and Azure Repos during coursework. This GitHub repository hosts the Android app, its pull requests and its automated build checks.