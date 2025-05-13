# BiteWise – Food Ingredient Scanner
**By Phuc Nguyen**

A smart Android app to help users make informed food choices by scanning product barcodes and extracting ingredient data.

---

## Overview

**BiteWise** empowers users to track their food consumption and make healthier decisions by leveraging barcode scanning and ingredient analysis powered by the [Open Food Facts API](https://world.openfoodfacts.org/). The app extracts ingredient information, displays nutritional content, and highlights risky allergens using a customizable detection system.

Although developed during a busy finals week and job search, this app reflects my passion for building real-world, purposeful projects.

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

## Target Users

- Health-conscious individuals
- People with dietary restrictions (e.g., gluten, nuts, dairy)
- Nutritionists and researchers
- Anyone seeking a simple, smart food logging tool

---

## Highlight Feature: Ingredient-Based Allergen Detection (Rule-Based NLP)

This feature matches ingredients against a user-defined allergy list.

- Extracted ingredient list is compared using keyword matching (e.g., “peanut”, “lactose”)
- Alerts when allergens are detected
- Customizable allergy settings
- Powered by `spaCy` or `nltk` (lightweight NLP, no ML model needed—for now)

**Planned Enhancements**:
- Synonym detection (e.g., "arachis oil" = peanut)
- Fuzzy matching (e.g., "caseinate" = dairy)
- Machine Learning integration for advanced detection and suggestions

---

## Core Features

- **Barcode scanning** – Fast, reliable scan with ML Kit Barcode API
- **OCR ingredient extraction** – Read and parse label text using ML Kit Text Recognition
- **Open Food Facts API integration** – Fetch real-time product info
- **Allergy detection system** – Based on customizable preferences
- **Personalized dashboard** *(planned)* – Visual insights with charts and progress toward health goals
- **Smart notifications** *(planned)* – Get alerts when products exceed nutrition limits or contain allergens
- **Scan history with Room DB**:
    - Stores product name, ingredients, calories, and timestamp
    - Displayed in a grouped-by-date layout using `ListView` and a custom adapter

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

## Recent Updates (as of 05/10)

- Product info now displayed on a dedicated screen
- UI overhaul with cleaner layout and better experience
- Implemented local scan history with Room Database and custom adapter
- Grouped product history view by date

## Recent Updates (as of 05/12)
- Color for the app:
  Name	HEX Code	Use Case Suggestion
  Amber 500	#FF9800	Vibrant and bold, great for primary color
  Deep Orange	#FF5722	Rich and dynamic, good for action buttons
  Orange 300	#FFB74D	Softer, warm background or card color
  Orange 700	#F57C00	Darker accent, great for headers or toolbar

- Add Kcal limit setting and warning system:
  - Added KcalLimitActivity to allow users to set and save daily kcal limits using SharedPreferences
  - Integrated CheckKcalLimitActivity to compare scanned product kcal with the set limit
  - Displays warning if product exceeds limit, or confirmation if within limit
  - Hooked into barcode scanning flow and updated UI accordingly
---

## Features Implemented

- OCR ingredient extraction
- Barcode scanning
- Open Food Facts API integration
- Product info display
- Allergen detection (rule-based)
- Scan history with timestamp and data grouping

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

## Contribution

Currently a personal project. Open to feedback and suggestions.
