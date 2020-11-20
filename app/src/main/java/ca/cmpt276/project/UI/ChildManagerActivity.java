package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;

/**
 * This is the UI class for child managing. User can add,
 * edit or delete child in this page. It will show a list
 * of child, and pop up a dialog for input.
 */
public class ChildManagerActivity extends AppCompatActivity implements ConfigChildDialog.NoticeDialogListener {
    private ChildManager manager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildManagerActivity.class);
    }

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
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        listView.setEmptyView(findViewById(R.id.no_child_layout));
    }

    private void clickList() {
        ListView listView = findViewById(R.id.children_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Child child = manager.get(position);
                ConfigChildDialog dialog = ConfigChildDialog.getInstance(position, child);
                dialog.show(getSupportFragmentManager(), "ConfigChildDialog");
            }
        });
    }

    private void setAddButton() {
        FloatingActionButton button = findViewById(R.id.add_child_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigChildDialog dialog = ConfigChildDialog.getInstance(-1, new Child("",  null));
                dialog.show(getSupportFragmentManager(), "ConfigChildDialog");
            }
        });
    }

    private void saveToDisk() {
        SharedPreferences prefs = this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(manager.CHILD_KEY, manager.toJson());

        editor.apply();
    }

    private void updateData() {
        populateListView();
        saveToDisk();
    }

    @Override
    public void onDialogPositiveClick(int pos, Child child) {
        if (pos != -1) {
            manager.get(pos).setName(child.getName());
            manager.get(pos).setPhoto(child.getPhoto());
        } else {
            manager.add(child);
        }
        updateData();
    }

    @Override
    public void onDialogDelete(int pos) {
        manager.delete(pos);
        updateData();
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

            String name = manager.get(position).getName();
            Bitmap photo = manager.get(position).getPhoto();

            TextView nameTextView = itemView.findViewById(R.id.child_name);
            nameTextView.setText(name);

            ImageView childIcon = itemView.findViewById(R.id.child_icon);
            childIcon.setImageBitmap(photo);

            return itemView;

        }
    }
}