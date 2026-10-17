# Home of Hope NGO Android Application

### XISD6329

A comprehensive mobile platform designed for **Home of Hope NGO** (Siyazigabisa) to manage member engagement, donations, community support, and volunteer activities. This application serves as a bridge between the NGO and its community, fostering transparency and active participation.

## 🌟 Key Features

### 👤 Member Features
- **Secure Registration & Login**: Multi-step registration for new members with session management.
- **Personal Dashboard**: Track total donations, volunteer hours, and membership progress in real-time.
- **Membership Levels**: Progress through tiers (Seed, Hope, Silver, Gold, Legacy) based on contributions and engagement.
- **Community Wall**: Interact with other members in a shared forum for news and updates.
- **Digital Membership Card**: View and present your digital ID with membership details.
- **Causes & Events**: 
  - Browse active NGO causes and view detailed impact reports.
  - Discover and join upcoming volunteer events and community gatherings.
- **Blog & News**: Access the latest NGO articles, success stories, and newsletters.
- **Secure Donations**: 
  - Seamless donation process integrated with backend services.
  - Track donation history and receive instant confirmation.
- **Support & FAQ**: Comprehensive help center with FAQs and a direct contact system.

###  Admin Management System
- **Secure Admin Portal**: Dedicated access for NGO administrators (`admin@hoh.org`).
- **Dashboard Analytics**: Real-time stats on membership growth, total donations, and volunteer impact.
- **Member Management**: 
  - Search, view, and manage member profiles.
  - Suspend or activate accounts based on compliance.
  - Securely handle member data deletions.
- **Volunteer Hours Management**: 
  - Manually log and verify volunteer service hours.
  - Audit and update service records.

###  Legal & Privacy
- **Privacy Policy**: Clear disclosure of data handling practices.
- **Terms of Service**: Defined guidelines for app and community usage.
- **Cookie Policy**: Information on local data storage and preferences.

##  Technical Stack
- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for REST API communication.
- **Local Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Session Management**: [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- **Asynchronous Flow**: Kotlin Coroutines & Flow
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Animations**: [Lottie for Android](https://github.com/airbnb/lottie-android)
- **Splash Screen**: [AndroidX SplashScreen API](https://developer.android.com/develop/ui/views/launch/splash-screen)

## Project Structure
- `data/`:
  - `local/`: Room database entities, DAOs, and configuration.
  - `model/`: Data classes for API responses and local entities.
  - `network/`: Retrofit API services and network client configuration.
  - `repository/`: Single source of truth for data operations (Local + Remote).
- `ui/`:
  - `screens/`: Composable screens organized by feature (Admin, Member, Home, etc.).
  - `viewmodel/`: Business logic and state management using `ViewModel`.
  - `navigation/`: Centralized navigation graph and route definitions.
  - `theme/`: Material 3 design system implementation (Colors, Typography, Shapes).
- `utils/`: Helper classes and extension functions.

##  Getting Started

### Admin Credentials
To access the Admin Management System:
- **Email**: `admin@hoh.org`
- **Password**: `Admin@123`

### Prerequisites
- [Android Studio Ladybug](https://developer.android.com/studio) (or newer)
- JDK 17
- Android SDK 24+ (Min SDK)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/CeejayAlughere/HomeOfHopeApp.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or physical device.
