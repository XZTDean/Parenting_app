package ca.cmpt276.project.UI.Tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.CHILD.Child;
import ca.cmpt276.project.model.TASKS.Task;
import ca.cmpt276.project.model.TASKS.TaskManager;

/**
 * This is the activity of list of task, user can
 * see the detail of tasks and add new tasks from
 * this activity.
 */
public class TaskListActivity extends AppCompatActivity implements ConfigTaskDialog.NoticeDialogListener {
    private TaskManager manager;
    private TaskArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        manager = TaskManager.getInstance();

        populateList();
        setButton();
        clickList();
        setToolbar();
    }

    private void populateList() {
        adapter = new TaskArrayAdapter(manager.getList());
        ListView listView = findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.getAdapter();
    }

    private void clickList() {
        ListView listView = findViewById(R.id.task_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConfigTaskDialog dialog = ConfigTaskDialog.getInstance(position);
                dialog.show(getSupportFragmentManager(), "ConfigTaskDialog");
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setButton() {
        FloatingActionButton button = findViewById(R.id.add_task);
        button.setOnClickListener(v -> {
            ConfigTaskDialog dialog = ConfigTaskDialog.getInstance(-1);
            dialog.show(getSupportFragmentManager(), "ConfigTaskDialog");
        });
    }

    private void saveToDisk() {
        SharedPreferences prefs = this.getSharedPreferences("AppPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(manager.TASK_KEY, manager.toJson());
        editor.apply();
    }

    @Override
    public void dataChanged() {
        adapter.notifyDataSetChanged();
        saveToDisk();
    }

    private class TaskArrayAdapter extends ArrayAdapter<Task> {
        public TaskArrayAdapter(@NonNull List<Task> objects) {
            super(TaskListActivity.this, R.layout.task_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_item, parent, false);
            }

            Task task = getItem(position);
            assert task != null;
            String taskName = task.getName();
            TextView name = itemView.findViewById(R.id.task_name);
            name.setText(taskName);

            Child next = task.getNext();
            View view = itemView.findViewById(R.id.next_child_name_display);
            if (next != null) {
                view.setVisibility(View.VISIBLE);
                String nextChild = next.getName();
                TextView child = itemView.findViewById(R.id.task_child);
                child.setText(nextChild);

                Bitmap photo = next.getPhoto();

                ImageView childIcon = itemView.findViewById(R.id.child_icon_task);
                if(childIcon != null) {
                    childIcon.setImageBitmap(photo);
                }
            } else {
                view.setVisibility(View.GONE);
            }

            return itemView;
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, TaskListActivity.class);
    }
}