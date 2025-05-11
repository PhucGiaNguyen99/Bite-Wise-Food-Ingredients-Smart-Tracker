# Ingredients-Scanner
Phuc Nguyen's Ingredient Scanner

**05/09 - Let's redirect the goals of this beautiful app - BiteWise**


**General idea of the app:** 
To help users track their food consumption and make informed health decisions by scanning barcodes and analyzing product data automatically.

**Why need to scan barcode of products? why not just use the nutrition facts typically printed on product packaging??**:
- Scanning a barcode allows the app to access a wealth of information beyond what's printed on the label (By tracking the Open Food Facts database).
- Automated data logging for allowing users to quickly and accurately log their food intake with minimal effort.
- Standardized information for presenting standardized and consistent information by leveraging a centralized database.
- Enhanced analysis and insights for providing advanced features such as:
  + Tracking consumption patterns over time.
  + Identifying nutrient deficiencies or excesses.
  + Providing personalized dietary recommendations.
  + Highlighting potential allergens or additives to avoid. 

**For whom?** This beautiful app is useful for health-conscious users, food researchers, and individuals with dietary restrictions who need to quickly check ingredient information without manually typing it.

**One Real Cool Feature - Ingredient-based Allergen detection using NLP**
Direction: 
- Uses already extract ingredients from barcodes.
- Highlights and alerts risky ingredients every time from the list of allergy preferences the user set once.
- Now it is a simple keyword-matching or rule-based NLP approach, so no need for ML models. **Use spaCy or nltk. **
- Later: Think about the synonym detection (arachis oil = peanut oil), fuzzy matching (caseinate for dairy), and maybe using machine learning to detect lesser-known allergens or suggest similar-safe alternatives.
  
**Core Features**
1. Barcode Scanner Integration: Instantly scan product barcodes and fetch detailed data using the Open Food Facts API.
2. Ingredient & Nutrition Logging: Automatically extract and store ingredients, nutritional facts (calories, sugar, sodium, protein, etc.) and scan timestamps.
3. Allergy & Health Goal Customization:
   - Let users define allergens and health goals (e.g., limit sugar, avoid gluten, increase protein).
   - Flag products that conflict with these settings.
4. Personalized Dashboard & Insights:
- Visual analytics (pie charts, bar graphs) of daily/weekly/monthly intake.
- Track consumption vs. goals in a clean UI.
5. Smart notifcations: Warn users if scanned products exceed nutritional limits or contain allergens.

**Current Technical Stack & Tools**
1. Android Studio (Java)
2. CameraX (For capturing images and scanning barcodes)
3. ML Kit OCR (For extracting text from food labels)
4. ML Kit Barcode Scanner (For scanning product barcodes)
5. Retrofit (For fetching product data from Open Food Facts API)
6. Open Food Facts API (For retrieving food product details)

05/10:
1. Display the product info in a separate screen.
2. Improved the entire UI of the app.
3. Implement the history of scanned products:
- Implemented a local scan history feature using Room Database to store and organize product scan data.
- Each scanned item—containing product name, ingredients, calorie information, and timestamp—is saved as a ScannedFood entity. The data is persistently stored and displayed in a grouped-by-date format on a dedicated History screen using a ListView with a custom adapter for visual distinction between dates and entries.

**Features Implemented**
1. Text recognition using ML Kit OCR to extract ingredients from images
2. Barcode scanning to identify food products
3. Integration with Open Food Facts API to fetch product details
4. Display product information including ingredients, calories, and additives


