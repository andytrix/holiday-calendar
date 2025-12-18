# Holiday Calendar

Holiday Calendar is a native Android application that allows users to browse public holidays by country and year. The app retrieves public holiday data from an external API and displays it in a clean, mobile friendly UI.

Built with Kotlin and Jetpack Compose, following MVVM architecture.

![Holiday Calendar Gif](https://github.com/user-attachments/assets/a4175f0b-dc07-4617-8b97-6e6cbe2cd290)

## Contents

* [Introduction](#holiday-calendar)
* [Features](#features)
* [Architecture & Structure](#architecture--structure)
* [Technology Stack](#technology-stack)
* [Installation & Setup](#installation--setup)
* [API](#api)
* [License](#license)

## Features

- **Public Holiday Search** — Fetch public holidays by country and year
- **Next Holiday Highlight** — Automatically shows the next upcoming holiday
- **Filtering Options** — View only upcoming or global holidays
- **Error Handling** — User friendly messages for network or API errors
- **Loading States** — Spinner displayed while data is loading
- **Dark Mode Support** — Toggleable theme, persisted across launches
- **Navigation** — Separate Home, Info, and Settings screens
- **Localized Strings** — All UI text stored in string resources
  
## Architecture & Structure

The app follows **MVVM architecture** and is divided into clear layers:

```text
app/src/main/java/com/andytrix/holidaycalendar/
├── data/
│   ├── api/
│   │   ├── HolidayApi.kt           # API interface
│   │   └── RetrofitClient.kt       # Retrofit instance
│   ├── model/
│   │   ├── Country.kt              # Country model
│   │   └── PublicHoliday.kt        # Holiday data model
│   ├── repository/
│   │   └── HolidayRepository.kt    # API wrapper
│   └── PrefsDataStore.kt           # DataStore prefs
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt           # Main screen
│   │   ├── InfoScreen.kt           # About screen
│   │   └── SettingsScreen.kt       # Settings screen
│   ├── components/
│   │   ├── AppActionButton.kt      # Action button
│   │   ├── AppTopBar.kt            # Top bar
│   │   ├── CountryDropdown.kt      # Country selector
│   │   ├── ErrorView.kt            # Error view
│   │   ├── LoadingView.kt          # Loading view
│   │   ├── HolidayListItem.kt      # List item card
│   │   └── SupportedCountries.kt   # Dropdown country data
│   ├── navigation/
│   │   ├── AppNavHost.kt           # Nav host
│   │   └── Routes.kt               # Route names
│   ├── theme/
│   │   ├── Color.kt                # Colors
│   │   ├── Theme.kt                # Theme setup
│   │   └── Type.kt                 # Typography
│   └── state/
│   │   └── HolidayUiState.kt       # UI state types
├── viewmodel/
│   └── HolidayViewModel.kt         # ViewModel
└── MainActivity.kt                 # App entry
```

State management, validation, and API calls are handled in the ViewModel. UI reacts to state changes using Compose.

## Technology Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose
* **Architecture:** MVVM
* **Networking:** Retrofit + Coroutines
* **Persistence:** DataStore Preferences
* **Navigation:** Compose Navigation

## Installation & Setup

1. **Clone the repository**

```bash
git clone https://github.com/andytrix/holiday-calendar.git
```

2. **Open in Android Studio**

* Open the project
* Let Gradle sync
* Run on an emulator or physical device

No additional configuration is required.

## API

* **Public Holidays API:** Nager.Date

    * [https://date.nager.at](https://date.nager.at)

The API is free to use and does not require authentication or API keys.

## License

This project is licensed under the terms described in the [LICENSE](./LICENSE) file.
