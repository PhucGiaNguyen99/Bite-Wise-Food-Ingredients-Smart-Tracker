# BiteWise – Food Ingredient Scanner
**By Phuc Nguyen**

## Overview
A feature-rich Android application designed to empower health-conscious users in making informed dietary choices. The app leverages barcode scanning (ML Kit), real-time nutrition analysis via the Nutritionix API, and rule-based NLP to detect allergens and enforce personalized dietary preferences such as calorie thresholds.
This project showcases full-stack Android development expertise, including RESTful API integration, local persistence with Room Database, cloud synchronization using Firebase Firestore, and modern UI development following Material Design principles. Additionally, it demonstrates proficiency in implementing machine learning-based text recognition and custom natural language processing (NLP) for ingredient parsing and allergen detection — all orchestrated within a robust, scalable Android architecture.

## Key Features

- **Barcode Scanning with ML Kit + CameraX** – Seamlessly scans product barcodes using Google's ML Kit and CameraX for real-time, high-performance recognition, supporting a wide range of UPC/EAN codes.

- **Nutritional and Ingredient Retrieval via Nutritionix API** – Integrates with the Nutritionix API to fetch real-time product data, including calories, macronutrients, and complete ingredient lists from a large branded food database.

- **Allergen Detection with Rule-Based NLP** - Users can define custom allergen keywords via interactive Chip UI. The app applies keyword-based natural language processing to detect and highlight matching allergens in scanned ingredient lists.

- **Personalized Calorie Limit Enforcement** - Allows users to set a daily calorie cap. When a scanned product exceeds the remaining allowance, the app provides a visual warning to support dietary compliance.

- **Local + Cloud Scan History Tracking** - Persistently stores scanned product data with timestamps in a local Room Database. History is displayed in a user-friendly, date-grouped format. Optionally synced to Firebase Firestore for multi-device access and cloud backup.

- **Firebase Firestore Integration** - Enables real-time cloud storage of user preferences, scanned product metadata, and historical consumption logs. Facilitates seamless access across devices and future analytics expansion.


## Recent Updates:

- Switched from Open Food Facts to Nutritionix API.
- Added calorie check activity with daily summary.
- Reworked allergen input UI using ChipGroup.
- Improved Material UI consistency and responsiveness.


## Why Barcode Scanning Over Manual Label Reading?
- **Rich, Structured Data Access**
  Scanning barcodes retrieves standardized nutrition and ingredient data from authoritative databases like Open Food Facts and Nutritionix, going beyond what’s printed on packaging.

- **Faster and More Accurate Logging**
  Enables one-tap entry of nutrition facts and ingredients, eliminating manual errors and speeding up food tracking for users.

- **Consistent, Centralized Information**
  Barcodes yield unified product data formats, allowing for reliable comparisons and scalable analysis across different brands and categories.

- **Enhanced Health Intelligence**
  Scanned data supports:
  Longitudinal diet tracking
  Allergen and additive detection
  Personalized dietary feedback
  Identification of nutrient excesses or deficiencies


## Why Nutritionix API?

- Access to 1.9M+ food items, including branded and restaurant products.
- Supports barcode scanning and natural language nutrition queries.
- Continuously updated, dietitian-verified database ensures accuracy.
- Fast, reliable REST API with strong developer support.
- Ideal for real-time ingredient and calorie tracking in mobile apps.


## Tech Stack

| Feature                  | Technology                   |
|--------------------------|------------------------------|
| Language                 | Java                         |
| IDE                      | Android Studio               |
| Image Capture & Barcode | CameraX, ML Kit              |
| OCR                     | ML Kit Text Recognition      |
| API Communication       | Retrofit                     |
| Data Source             | Nutritionix API          |
| Local Storage           | Room Database                |
| Cloud Storage           | Firestore                |
| UI Design               | Material Design Components   |


## Target Users

- Health-conscious individuals.
- People with dietary restrictions (e.g., gluten, nuts, dairy).
- Nutritionists and researchers.
- Anyone seeking a simple, smart food logging tool.


## How Allergy Detection Works

BiteWise uses rule-based NLP to match user-defined allergy keywords against scanned ingredients using:
- **Lowercased string comparison**
- **Whitespace and comma-separated parsing**
- **Basic fuzzy matching (planned)**

Example:
If the user enters `peanut, lactose`, and the OCR returns:
> "May contain traces of arachis oil and milk sugar"

It will flag:
- `arachis oil` → **peanut**
- `milk sugar` → **lactose**


## Future Improvements

- Interactive dashboard for nutrition analysis
- Visual intake charts (daily/weekly/monthly)
- Health goal recommendations and alerts
- Machine Learning enhancements for NLP allergen detection


## Getting Started

To run the app locally:

- Clone this repository:
  git clone https://github.com/yourusername/BiteWise.git
- Open the project in **Android Studio**.
- Connect an Android device or start an emulator.
- Click **Run** to build and launch the app.


## Contribution

**Bite Wise** is an independently developed solo project that applies real-world skills in Android development, OCR integration, RESTful API communication, and rule-based natural language processing. Though not created for a course or client, it reflects a strong focus on solving practical challenges in health and nutrition through mobile technology. Feedback, feature suggestions, and contributions are welcome via issues or pull requests.


