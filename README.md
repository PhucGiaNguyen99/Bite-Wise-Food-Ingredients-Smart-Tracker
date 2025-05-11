# Ingredients-Scanner
Ingredient Scanner
Ingredient Scanner is an Android app that allows users to capture an image of food label using ML Kit OCR, extract the text using Google ML Kit's OCR technology, and save the extracted text to a .txt file for later use.

This app is useful for health-conscious users, food researchers, and individuals with dietary restrictions who need to quickly check ingredient information without manually typing it.

📷 Features
✅ Capture Image – Take a picture of food labels using the phone's camera.

✅ Scan barcodes on food products to retrieve detailed product information from the Open Food Facts API

✅ Save Text to File – Stores the extracted text in a .txt file in the app’s storage.

✅ Display key product details, including:
  - Product Name
  - Brand
  - List of Ingredients
  - Allergens (Milk, Soy, Gluten, etc.)
  - Additives & Preservatives
  - Calories & Basic Nutrition Info

🛠️ Technical Stack & Tools
-  Android Studio (Java)
-  CameraX 📸 (For capturing images and scanning barcodes)
-  ML Kit OCR 📝 (For extracting text from food labels)
-  ML Kit Barcode Scanner 🔍 (For scanning product barcodes)
-  Retrofit 🌐 (For fetching product data from Open Food Facts API)
-  Open Food Facts API 📊 (For retrieving food product details)

🚀 Features Implemented (MVP Stage)
✔️ Text recognition using ML Kit OCR to extract ingredients from images
✔️ Barcode scanning to identify food products
✔️ Integration with Open Food Facts API to fetch product details
✔️ Display product information including ingredients, calories, and additives

📌 Next Steps & Future Features
🔹 Enhance UI with Material Design components
🔹 Categorize ingredients into Main, Additives, and Allergens
🔹 Add Health Score (A to E) to indicate food quality
🔹 Improve scanning accuracy and filtering of valid ingredients

03:17 AM - 03/30: 
Store the food details in Room database:
- Create a data entity ScannedFood with id, productName, ingredients, calories, scanTime.
- Create a DAO Interface ScannedFoodDao -> functions to interact with the table.
- Create the Room Database AppDatabase -> a singleton Room Database with ScannedFood Entity, ScannedFoodDao interface, and getDatabase(context) to access it. 
**allowMainThreadQueries() is okay for small apps or testing but consider switching to background threads later with LiveData or Coroutines for better performance.**