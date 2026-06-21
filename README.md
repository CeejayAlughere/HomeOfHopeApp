# Home of Hope NGO Android Application

A comprehensive mobile platform designed for **Home of Hope NGO** (Siyazigabisa) to manage member engagement, donations, community support, and volunteer activities.

## 🌟 Key Features

### 👤 Member Features
- **Secure Registration & Login**: Multi-step registration for new members.
- **Personal Dashboard**: Track total donations, volunteer hours, and membership progress.
- **Membership Levels**: Progress through tiers (Seed, Hope, Silver, Gold, Legacy) based on contributions.
- **Community Wall**: Interact with other members in a shared forum.
- **Digital Membership Card**: View your digital ID with membership details.
- **Member Settings**: Update personal information (Name, Email, Phone) directly from the dashboard.
- **Event Participation**: Browse and join upcoming NGO events.
- **Secure Donations**: Make donations and track history within the app.

### 🛡️ Admin Management System
- **Secure Admin Portal**: Dedicated access for NGO administrators (`admin@hoh.org`).
- **Dashboard Analytics**: Real-time stats on membership growth, total donations, and volunteer impact.
- **Member Management**: 
  - Search, view, and edit member profiles.
  - Suspend or activate accounts.
  - Securely delete member records.
- **Volunteer Hours Management**: 
  - Manually add and verify volunteer hours for members.
  - Edit or remove service records.

## 🛠️ Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library
- **Navigation**: Jetpack Navigation Compose
- **Asynchronous Flow**: Kotlin Coroutines & Flow
- **Image Loading**: Coil (where applicable)

## 📁 Project Structure
- `ui/screens/`: Composable screens for Home, About, Member, and Admin features.
- `ui/viewmodel/`: State management and business logic.
- `data/local/`: Room database entities, DAOs, and database configuration.
- `data/repository/`: Data layer abstraction for local and network resources.
- `ui/navigation/`: Navigation graph and screen route definitions.

## 🚀 Getting Started

### Admin Credentials
To access the Admin Management System:
- **Email**: `admin@hoh.org`
- **Password**: `Admin@123`

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17
- Android SDK 24+ (Min SDK)

### Installation
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or physical device.

## 📝 License
This project is for use by Home of Hope NGO. All rights reserved.
