package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
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
import ca.cmpt276.project.model.Child;
import ca.cmpt276.project.model.ChildManager;

/**
 * This is the dialog for entering child's name
 * when add and edit the child. And there will
 * be a delete button for exist child.
 */
public class ConfigChildDialog extends DialogFragment {
    //private static final String NAME = "NAME";
    private static final String CHILD = "CHILD";
    private static final String POS = "POSITION";
    private View view;
    private int pos;
    private NoticeDialogListener listener;
    private AlertDialog.Builder builder;

    private Child child;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_config_child_dialog, null);

        setChild();
        if (pos < 0) {
            //Setting default image
            @SuppressLint("UseCompatLoadingForDrawables")
            Drawable defaultImage = getResources().getDrawable(R.drawable.default_photo_jerry);
            Bitmap photo = ((BitmapDrawable) defaultImage).getBitmap();
            child.setPhoto(photo);

            builder.setTitle(R.string.add_child)
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {})
                    .setNeutralButton(R.string.addPhoto, null);
        } else {
            builder.setTitle(R.string.edit_child)
                    .setNegativeButton(R.string.delete, (dialog, which) -> listener.onDialogDelete(pos))
                    .setNeutralButton(R.string.changePhoto, null);
        }
        builder.setView(view)
                .setPositiveButton(R.string.ok, null); // will override after build (onStart method)

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

    private void setChild() {
        assert getArguments() != null;
        child = (Child) getArguments().getParcelable(CHILD);
        pos = getArguments().getInt(POS);

        EditText editText = view.findViewById(R.id.name_edit_text);
        editText.setText(child.getName());
    }

    private void positiveClick() {
        EditText editText = view.findViewById(R.id.name_edit_text);
        String name = editText.getText().toString();


        if (name.isEmpty()) {
            editText.setError(getString(R.string.name_empty_warnning));
            return;
        }
        child.setName(name);
        listener.onDialogPositiveClick(pos, child);
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
        Intent intent = ChildrenPhotoActivity.makeIntent(getActivity(), child);
        startActivity(intent);
    }

    public static ConfigChildDialog getInstance(int pos, Child child) {
        ConfigChildDialog dialog = new ConfigChildDialog();

        Bundle args = new Bundle();
        args.putParcelable(CHILD, (Parcelable) child);
        args.putInt(POS, pos);
        dialog.setArguments(args);

        return dialog;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(int pos, Child child);

        public void onDialogDelete(int pos);
    }
}