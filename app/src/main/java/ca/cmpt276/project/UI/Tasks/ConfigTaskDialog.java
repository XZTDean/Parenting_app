package ca.cmpt276.project.UI.Tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.CHILD.Child;
import ca.cmpt276.project.model.TASKS.Task;
import ca.cmpt276.project.model.TASKS.TaskManager;

/**
 * This is the dialog for task detail. Users can see the detail
 * info in this dialog, and they can also edit or delete task
 * in this dialog. As data change it will send a message back
 * to activity to notify the change.
 */
public class ConfigTaskDialog extends DialogFragment {
    private static final String POS_KEY = "POS";

    private View view;
    private boolean newTask;
    private boolean edit;
    private int pos;
    private Task task;
    private TaskManager taskManager;
    private NoticeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.config_task_dialog, null);

        setFields();
        setButton();

        if (newTask) {
            displayEditPanel();
            ImageButton button = view.findViewById(R.id.task_edit_complete);
            button.setVisibility(View.GONE);
            builder.setTitle(R.string.add_task);
        } else {
            displayInfoPanel();
            builder.setNeutralButton(R.string.reset, null);
        }

        builder.setView(view)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        assert dialog != null;
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(v -> clickPositiveButton());
        button = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        button.setOnClickListener(v -> {
            resetTask();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (NoticeDialogListener) context;
    }

    private void displayEditPanel() {
        edit = true;

        View displayPanel = view.findViewById(R.id.task_info_disp);
        displayPanel.setVisibility(View.GONE);

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
        editPanel.setVisibility(View.GONE);

        View displayPanel = view.findViewById(R.id.task_info_disp);
        displayPanel.setVisibility(View.VISIBLE);

        TextView name = view.findViewById(R.id.task_name_disp);
        name.setText(task.getName());
        TextView desc = view.findViewById(R.id.task_desc_disp);
        desc.setText(task.getDescription());

        displayChild();
    }

    private void displayChild() {
        Child child = task.getNext();

        if (child == null) {
            View childBlock = view.findViewById(R.id.task_child_block);
            childBlock.setVisibility(View.GONE);
        } else {
            TextView childName = view.findViewById(R.id.task_child_name);
            childName.setText(child.getName());

            ImageView childImage = view.findViewById(R.id.task_child_image);
            childImage.setImageBitmap(child.getPhoto());
        }
    }

    private void clickPositiveButton() {
        if (edit) {
            if (completeEdit()) {
                dismiss();
            }
        } else {
            dismiss();
        }
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
            if (newTask) {
                task.setName(name);
                task.setDescription(desc);
                success = taskManager.add(task);
            } else {
                success = taskManager.edit(pos, name, desc);
            }

            if (success) {
                listener.dataChanged();
            } else {
                nameEdit.setError(getString(R.string.task_exist));
                descEdit.setError(getString(R.string.task_exist));
            }
        }
        return success;
    }

    private void finishTask() {
        task.finishTask(task.getNext());
        displayChild();
        listener.dataChanged();
    }

    private void resetTask() {
        task.reset();
        displayChild();
        listener.dataChanged();
    }

    private void setButton() {
        ImageButton completeEdit = view.findViewById(R.id.task_edit_complete);
        completeEdit.setOnClickListener(v -> {
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
            listener.dataChanged();
            dismiss();
        });

        Button completeTask = view.findViewById(R.id.task_complete_button);
        completeTask.setOnClickListener(v -> finishTask());
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

    public interface NoticeDialogListener {
        void dataChanged();
    }
}