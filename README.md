# BiteWise – Food Ingredient Scanner
**By Phuc Nguyen**

A smart Android app that helps users make informed food choices by scanning barcodes or labels, analyzing ingredients, and checking against personalized preferences like allergy keywords and calorie limits.
---

## Overview

**BiteWise** empowers users to track their food consumption and make healthier decisions by leveraging barcode scanning and ingredient analysis powered by the [Open Food Facts API](https://world.openfoodfacts.org/). The app extracts ingredient information, displays nutritional content, and highlights risky allergens using a customizable detection system.

Although developed during a busy finals week and job search, this app reflects my passion for building real-world, purposeful projects.

---

## Core Features

- **Barcode scanning** – Fast, reliable product lookup using ML Kit Barcode Scanning API.
- **OCR ingredient extraction** – Extract and parse on-package text using ML Kit Text Recognition.
- **Ingredient analysis & allergy detection** – Automatically checks scanned ingredients against user-defined allergy keywords using rule-based NLP
- **Calorie limit enforcement** –  Compares product calories with user-set thresholds and warns when limits are exceeded
- **Open Food Facts API integration** - Fetches real-time product and nutrition info from a global food database.
- **Scan history with Room DB**:
  - Stores product name, ingredient list, calories, and scan timestamp.
  - Displays entries grouped by date using ListView and a custom adapter.
- **Smart notifications** – Timely alerts when scanned products contain allergens or exceed nutrition targets.
- **Personalized dashboard** *(planned)* – Visual insights with charts and progress tracking.

---

## Why Scan Barcodes Instead of Just Reading Labels?

- **Access rich data**: Barcodes unlock detailed product information from Open Food Facts beyond what's printed on packaging.
- **Faster logging**: Automatically record ingredients and nutrition with a single scan.
- **Standardized information**: Get reliable, centralized data for better analysis.
- **Smarter health tracking**:
    - Analyze trends over time
    - Detect nutrient imbalances
    - Personalize dietary goals
    - Alert users to allergens or additives

---

## Tech Stack

| Feature                  | Technology                   |
|--------------------------|------------------------------|
| Language                 | Java                         |
| IDE                      | Android Studio               |
| Image Capture & Barcode | CameraX, ML Kit              |
| OCR                     | ML Kit Text Recognition      |
| API Communication       | Retrofit                     |
| Data Source             | Open Food Facts API          |
| Local Storage           | Room Database                |
| UI Design               | Material Design Components   |

---

## Target Users

- Health-conscious individuals
- People with dietary restrictions (e.g., gluten, nuts, dairy)
- Nutritionists and researchers
- Anyone seeking a simple, smart food logging tool

---

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

--- 

## Future Improvements

- Interactive dashboard for nutrition analysis
- Visual intake charts (daily/weekly/monthly)
- Health goal recommendations and alerts
- Machine Learning enhancements for NLP allergen detection

---

## Screenshots

*(Coming Soon)*

---

## Getting Started

To run the app locally:

1. Clone this repository:
   git clone https://github.com/yourusername/BiteWise.git
2. Open the project in **Android Studio**.
3. Connect an Android device or start an emulator.
4. Click **Run** to build and launch the app.

---

## Project Structure

BiteWise/
├── app/
│ ├── activities/
│ ├── models/
│ ├── adapters/
│ ├── utils/
│ └── res/
├── AndroidManifest.xml
├── build.gradle
└── ...

## Contribution

BiteWise is a solo project developed independently to apply real-world skills in Android development, OCR integration, API communication, and rule-based NLP.  
While not built for a course or client, it reflects a strong focus on solving practical problems in health and nutrition.  
Feedback and suggestions are welcome through [issues](#) or pull requests.
