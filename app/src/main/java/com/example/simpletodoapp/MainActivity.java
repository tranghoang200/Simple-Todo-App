package com.example.simpletodoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> item;
    ItemAdapter itemAdapter;

    Button addButton;
    TextView etItem;
    RecyclerView recycItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        etItem = findViewById(R.id.addItemText);
        recycItem = findViewById(R.id.itemList);

        loadItems();

        ItemAdapter.onLongClickListener longClickListener = new ItemAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                item.remove(position);
                itemAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemAdapter.onClickListener clickListener = new ItemAdapter.onClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                intent.putExtra(KEY_ITEM_TEXT, item.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);

                startActivityForResult(intent, EDIT_TEXT_CODE);
            }
        };
        itemAdapter = new ItemAdapter(item, longClickListener, clickListener);

        recycItem.setAdapter(itemAdapter);
        recycItem.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                item.add(todoItem);
                itemAdapter.notifyItemInserted(item.size() - 1);
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                etItem.setText("");
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TEXT_CODE && resultCode == RESULT_OK) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            item.set(position, itemText);
            itemAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated success", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("Main Activity", "cannot get the result from edit activity");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    
    private void loadItems() {
        try {
            item = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error reading items", e);
            item = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), item);
        } catch (IOException e) {
            Log.e("Main Activity", "Error saving items", e);
        }
    }
}