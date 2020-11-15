package ca.cmpt276.project.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import ca.cmpt276.project.R;
import ca.cmpt276.project.model.ChildManager;

/**
 * This is the dialog for entering child's name
 * when add and edit the child. And there will
 * be a delete button for exist child.
 */
public class ConfigChildDialog extends DialogFragment {
    private static final String NAME = "NAME";
    private static final String POS = "POSITION";
    private View view;
    private int pos;
    private NoticeDialogListener listener;
    private AlertDialog.Builder builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_config_child_dialog, null);

        setName();
        if (pos < 0) {
            builder.setTitle(R.string.add_child);
        } else {
            builder.setTitle(R.string.edit_child)
                    .setNeutralButton(R.string.delete, (dialog, which) -> listener.onDialogDelete(pos));
        }
        builder.setView(view)
                .setPositiveButton(R.string.ok, null) // will override after build (onStart method)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {})
                .setNeutralButton(R.string.addPhoto, null);


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        assert dialog != null;
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(v -> positiveClick());
        Button addPhotoButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        addPhotoButton.setOnClickListener(v -> neutralClick());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (NoticeDialogListener) context;
    }

    private void setName() {
        assert getArguments() != null;
        String name = getArguments().getString(NAME);
        pos = getArguments().getInt(POS);

        EditText editText = view.findViewById(R.id.name_edit_text);
        editText.setText(name);
    }

    private void positiveClick() {
        EditText editText = view.findViewById(R.id.name_edit_text);
        String name = editText.getText().toString();
        if (name.isEmpty()) {
            editText.setError(getString(R.string.name_empty_warnning));
            return;
        }
        listener.onDialogPositiveClick(pos, name);
        dismiss();
    }

    private void neutralClick() {
        EditText editText = view.findViewById(R.id.name_edit_text);
        String name = editText.getText().toString();
        if (name.isEmpty()) {
            editText.setError(getString(R.string.name_empty_warnning));
            return;
        }
        addPhoto();
    }

    private void addPhoto(){
        Intent intent = new Intent(getActivity(), ChildrenPhotoActivity.class);
        startActivity(intent);
    }

    public static ConfigChildDialog getInstance(int pos, String name) {
        ConfigChildDialog dialog = new ConfigChildDialog();

        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putInt(POS, pos);
        dialog.setArguments(args);

        return dialog;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int pos, String name);
        public void onDialogDelete(int pos);
    }
}