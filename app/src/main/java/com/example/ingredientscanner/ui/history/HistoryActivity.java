package com.example.ingredientscanner.ui.history;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ingredientscanner.R;
import com.example.ingredientscanner.data.local.AppDatabase;
import com.example.ingredientscanner.data.local.ScannedFood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private List<ScannedFood> scannedHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.historyListView);

        loadHistoryFromRoom();
    }

    private void loadHistoryFromRoom() {
        new Thread(() -> {
            scannedHistory = AppDatabase.getInstance(this).scannedFoodDao().getAllFoods();

            runOnUiThread(() -> {
                if (scannedHistory == null || scannedHistory.isEmpty()) {
                    List<String> emptyMessage = new ArrayList<>();
                    emptyMessage.add("No scanned products yet.");
                    listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emptyMessage));
                    return;
                }

                Map<String, List<ScannedFood>> groupedByDate = groupScansByDate(scannedHistory);
                List<String> displayList = formatDisplayList(groupedByDate);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_history_entry, R.id.itemText, displayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView itemText = view.findViewById(R.id.itemText);
                        String currentText = getItem(position);

                        if (currentText != null && currentText.startsWith("📅")) {
                            itemText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
                            itemText.setTypeface(null, Typeface.BOLD);
                            itemText.setTextSize(18);
                        } else {
                            itemText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                            itemText.setTypeface(null, Typeface.NORMAL);
                            itemText.setTextSize(16);
                        }

                        return view;
                    }
                };

                listView.setAdapter(adapter);
            });
        }).start();
    }

    private Map<String, List<ScannedFood>> groupScansByDate(List<ScannedFood> history) {
        Map<String, List<ScannedFood>> grouped = new LinkedHashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        for (ScannedFood food : history) {
            String dateKey = dateFormat.format(food.timestamp);
            grouped.computeIfAbsent(dateKey, k -> new ArrayList<>()).add(food);
        }

        return grouped;
    }

    private List<String> formatDisplayList(Map<String, List<ScannedFood>> grouped) {
        List<String> displayList = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        for (String date : grouped.keySet()) {
            displayList.add("📅 " + date);
            for (ScannedFood food : grouped.get(date)) {
                displayList.add("• " + food.name + " (" + timeFormat.format(food.timestamp) + ")");
            }
        }

        return displayList;
    }
}
