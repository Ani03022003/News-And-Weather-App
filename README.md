# 📰 News & Weather App  

An Android application built with **Kotlin** and **MVVM architecture** that provides the latest news along with live weather updates based on your location.  

## ✨ Features  
- 📡 **News Feed**: Browse top headlines and category-wise news  
- ☁️ **Weather Updates**: Real-time weather info and temperature shown in the ActionBar  
- 📍 **Location Access**: Detects your city for local weather updates  
- 💾 **Offline Caching**: Powered by **Room Database**  
- ⚡ **API Integration**: Uses **Retrofit** for networking  
- 🖼️ **Image Loading**: Handled with **Glide**  

## 🖼️ Screenshots  

| Home Screen | News Details | Weather Info |  
|-------------|--------------|--------------|  
| ![Home](screenshots/home.png) | ![Details](screenshots/details.png) | ![Weather](screenshots/weather.png) |  

> ⚠️ Place your screenshots inside a folder named `screenshots/` in the project root, and update the file names above.  

## 🏗️ Tech Stack  
- **Language**: Kotlin  
- **Architecture**: MVVM (Model–View–ViewModel)  
- **Libraries**:  
  - Retrofit (API calls)  
  - Room (local database)  
  - Coroutines (asynchronous tasks)  
  - Glide (image loading)  

## 📦 Installation  
1. Clone the repository  
   ```bash
   git clone https://github.com/your-username/News-And-Weather-App.git
   ```  

2. Open in **Android Studio**  

3. Add your API keys in `local.properties` or `Constants.kt`:  
   ```properties
   NEWS_API_KEY=your_api_key
   WEATHER_API_KEY=your_api_key
   ```  

4. Build and run the app 🚀  

## 📸 Demo Video  
👉 *(Optional) Add a short GIF or YouTube link here to showcase the app in action*  

## 🙌 Acknowledgements  
- [News API](https://newsapi.org/)  
- [OpenWeather API](https://openweathermap.org/)  
