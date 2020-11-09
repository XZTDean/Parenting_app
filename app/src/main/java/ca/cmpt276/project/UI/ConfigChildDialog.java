package ca.cmpt276.project.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ca.cmpt276.project.R;

public class ConfigChildDialog extends DialogFragment {
    private static final String NAME = "NAME";
    private static final String POS = "POSITION";
    private View view;
    private int pos;
    private NoticeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                .setPositiveButton(R.string.ok, (dialog, which) -> positiveClick())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});
        return builder.create();
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
        listener.onDialogPositiveClick(pos, name);
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