package ca.cmpt276.project.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Task;

public class TaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
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
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            Task task = getItem(position);
            assert task != null;
            String taskName = task.getName();
            String nextChild = task.getNext().getName();

            TextView name = findViewById(R.id.task_name);
            name.setText(taskName);
            TextView child = findViewById(R.id.task_child);
            child.setText(nextChild);

            return itemView;
        }
    }
}