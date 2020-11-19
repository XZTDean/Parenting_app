package ca.cmpt276.project.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.Task;
import ca.cmpt276.project.model.TaskManager;

public class ConfigTaskDialog extends DialogFragment {
    private static final String POS_KEY = "POS";

    private View view;
    private boolean newTask;
    private boolean edit;
    private int pos;
    private Task task;
    private TaskManager taskManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.config_task_dialog, null);

        setFields();
        setButton();

        if (edit) {
            displayEditPanel();
            ImageButton button = view.findViewById(R.id.task_edit_complete);
            button.setVisibility(View.GONE);
            builder.setTitle("Add Task");
        }

        builder.setView(view);
        return builder.create();
    }

    private void displayEditPanel() {
        edit = true;

        View displayPanel = view.findViewById(R.id.task_info_disp);
        displayPanel.setVisibility(View.INVISIBLE);

        View editPanel = view.findViewById(R.id.task_info_edit);
        editPanel.setVisibility(View.VISIBLE);

        if (!newTask) {
            EditText name = view.findViewById(R.id.task_name_edit);
            name.setText(task.getName());
            EditText desc = view.findViewById(R.id.task_desc_edit);
            desc.setText(task.getDescription());
        }
    }

    private void displayInfoPanel() {
        edit = false;

        View editPanel = view.findViewById(R.id.task_info_edit);
        editPanel.setVisibility(View.INVISIBLE);

        View displayPanel = view.findViewById(R.id.task_info_disp);
        displayPanel.setVisibility(View.VISIBLE);

    }

    private boolean completeEdit() {
        boolean success = true;

        EditText nameEdit = view.findViewById(R.id.task_name_edit);
        String name = nameEdit.getText().toString();
        if (name.isEmpty()) {
            nameEdit.setError(getString(R.string.name) + " " + getString(R.string.empty_error));
            success = false;
        }

        EditText descEdit = view.findViewById(R.id.task_desc_edit);
        String desc = descEdit.getText().toString();
        if (desc.isEmpty()) {
            descEdit.setError(getString(R.string.description) + " " + getString(R.string.empty_error));
            success = false;
        }

        if (success) {
            task.setName(name);
            task.setDescription(desc);
        }
        return success;
    }

    private void setButton() {
        ImageButton complete = view.findViewById(R.id.task_edit_complete);
        complete.setOnClickListener(v -> {
            boolean success = completeEdit();
            if (success) {
                displayInfoPanel();
            }
        });

        ImageButton edit = view.findViewById(R.id.task_edit_button);
        edit.setOnClickListener(v -> displayEditPanel());

        ImageButton delete = view.findViewById(R.id.task_delete_button);
        delete.setOnClickListener(v -> {
            taskManager.remove(pos);
            dismiss();
            // notify list
        });
    }

    private void setFields() {
        Bundle args = getArguments();
        assert args != null;
        pos = args.getInt(POS_KEY);
        taskManager = TaskManager.getInstance();
        if (pos >= 0) {
            task = taskManager.get(pos);
            newTask = edit = false;
        } else {
            task = new Task("", "");
            newTask = edit = true;
        }
    }

    public static ConfigTaskDialog getInstance(int pos) {
        ConfigTaskDialog dialog = new ConfigTaskDialog();

        Bundle args = new Bundle();
        args.putInt(POS_KEY, pos);
        dialog.setArguments(args);

        return dialog;
    }
}