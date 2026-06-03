# Walkthrough - "Siyazigabisa Home Of Hope" App

I have successfully created the "Siyazigabisa Home Of Hope" app with Registration and Login functionality.

## Features Implemented

### 1. User Registration
- Users can register with their **Full Name**, **Username**, **Password**, and **Confirm Password**.
- Password confirmation is validated.
- Usernames are checked for uniqueness using a local Room database.

### 2. User Login
- Users can log in using their registered username and password.
- Errors are displayed for invalid credentials.

### 3. Home Screen
- Upon successful login, users are greeted with a welcome message and their username.
- A logout button is available to return to the login screen.

### 4. Data Persistence
- All user data is stored locally using **Room Database**, ensuring that registered users can log in even after the app is restarted.

## Technical Details

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room
- **Navigation**: Navigation Compose
- **Architecture**: MVVM with ViewModel and Repository pattern

## Verification Results

- **Build**: Successfully compiled and assembled.
- **Manual Verification**:
    - Navigated to Registration screen.
    - Registered a new user ("testuser").
    - Verified that navigating back to Login screen resets the state correctly.
    - Logged in with "testuser" and confirmed redirection to the Home screen.
    - Verified that the user remains in the database and can log in again after app restart.
    - Fixed a bug where the `AuthState` was not being reset on navigation, which could block login attempts immediately after registration.
