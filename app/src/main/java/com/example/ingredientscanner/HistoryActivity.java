package com.example.ingredientscanner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = findViewById(R.id.historyListView);
        List<ScannedFood> history = AppDatabase.getInstance(this).scannedFoodDao().getAllScans();

        Map<String, List<ScannedFood>> groupedByDate = new LinkedHashMap<>();
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (ScannedFood item : history) {
            String dateKey = dateOnlyFormat.format(item.scanTime);
            if (!groupedByDate.containsKey(dateKey)) {
                groupedByDate.put(dateKey, new ArrayList<>());
            }
            groupedByDate.get(dateKey).add(item);
        }

        // Prepare the display list
        List<String> displayList = new ArrayList<>();
        for (String date : groupedByDate.keySet()) {
            displayList.add("📅 " + date);  // Date header
            for (ScannedFood item : groupedByDate.get(date)) {
                displayList.add("• " + item.productName + " (" + timeFormat.format(item.scanTime) + ")");
            }
        }

        // Improve the bold header of the items
        // Keep the ListView visually distinguish the date group headers
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_history_entry, R.id.itemText, displayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView itemText = view.findViewById(R.id.itemText);

                String currentText = getItem(position);
                if (currentText != null && currentText.startsWith("📅")) {
                    itemText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
                    itemText.setTextSize(18);
                    itemText.setTypeface(null, Typeface.BOLD);
                } else {
                    itemText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    itemText.setTextSize(16);
                    itemText.setTypeface(null, Typeface.NORMAL);
                }

                return view;
            }
        };


        listView.setAdapter(adapter);
    }
}
