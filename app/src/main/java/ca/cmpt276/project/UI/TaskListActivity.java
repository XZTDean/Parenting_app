package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.Task;
import ca.cmpt276.project.model.TaskManager;

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