# Gacha Tracker App Apache

A simple gacha tracker for keeping track of your pulls and pull history in your favourite Gacha games.

Minimum supported Android version is Android 8

## Supported Games
- Genshin Impact
- Honkai: Star Rail
- Zenless Zone Zero

## Features
- Pull Counter: also tracks pulls to pity and 50/50
- History of Pulls
- Statistics: both for the individual user and global averages across all users
- App uses custom backend server solution
- App uses the <a href="https://github.com/square/retrofit">Retrofit</a> Http client

## Technology Stack
- Frontend: Android Studio(Java)
- Backend: Apache2 server, php scripting language, MariaDB database  

## Project Setup
- Create a credentials.xml file in app/src/main/res/values/credentials.xml
- Create server_url resource in credentials.xml that contains your server url
- Setup the <a href="https://github.com/sesvete/gacha-app-apache-server">server</a>

## Download
Head to the <a href="https://github.com/sesvete/gacha-tracker-apache/releases">releases</a> page for the newest release

Permission to install unknown apps is required for the installation of the app
