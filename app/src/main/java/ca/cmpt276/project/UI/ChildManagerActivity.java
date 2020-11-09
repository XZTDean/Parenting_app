package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;

public class ChildManagerActivity extends AppCompatActivity implements ConfigChildDialog.NoticeDialogListener {
    private ChildManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_manager);

        manager = ChildManager.getInstance();

        setToolbar();
        populateListView();
        clickList();
        setAddButton();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_child_manager);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void populateListView() {
        List<String> names = new ArrayList<>();
        for (Child child: manager) {
            names.add(child.getName());
        }

        ArrayAdapter<String> adapter = new ChildArrayAdapter(names);
        ListView listView = findViewById(R.id.children_list);
        listView.setAdapter(adapter);
    }

    private void clickList() {
        ListView listView = findViewById(R.id.children_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = manager.get(position).getName();
                ConfigChildDialog dialog = ConfigChildDialog.getInstance(position, name);
                dialog.show(getSupportFragmentManager(), "ConfigChildDialog");
            }
        });
    }

    private void setAddButton() {
        FloatingActionButton button = findViewById(R.id.add_child_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigChildDialog dialog = ConfigChildDialog.getInstance(-1, "");
                dialog.show(getSupportFragmentManager(), "ConfigChildDialog");
            }
        });
    }

    @Override
    public void onDialogPositiveClick(int pos, String name) {
        if (pos != -1) {
            manager.get(pos).setName(name);
        } else {
            manager.add(new Child(name));
        }
        populateListView();
    }

    private class ChildArrayAdapter extends ArrayAdapter<String> {
        public ChildArrayAdapter(List<String> strings) {
            super(ChildManagerActivity.this, R.layout.item_view, strings);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            String name = getItem(position);
            String firstLetter = name.toUpperCase().substring(0, 1);
            String colorName = "icon_" + position % 5;
            int colorId = getResources().getIdentifier(colorName, "color", getPackageName());
            int color = ContextCompat.getColor(ChildManagerActivity.this, colorId);

            TextView nameTextView = itemView.findViewById(R.id.child_name);
            nameTextView.setText(name);
            TextView iconTextView = itemView.findViewById(R.id.name_icon);
            iconTextView.setText(firstLetter);
            iconTextView.setBackgroundTintList(ColorStateList.valueOf(color));

            return itemView;
        }
    }
}