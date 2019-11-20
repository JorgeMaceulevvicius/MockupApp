package br.com.livroandroid.trainingmockup.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import br.com.livroandroid.trainingmockup.R;

public class SigleChoiceDialog extends DialogFragment {

    int position = 0;

    public interface SingleChoiceListener{
        void onPositiveButtonClicked(String temp,int position);
        void onNegativeBuuttonClicked();
    }

    SingleChoiceListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SingleChoiceListener) context;

        }catch (Exception e){
           throw new ClassCastException(getActivity().toString()+"SingleChoiceListener must implemented");
        }

    }

    @Nullable
    public Dialog onCreateDialog(Bundle savedIntenceState) {

        final String[] method = getActivity().getResources().getStringArray(R.array.methods);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select your method to capture the Temperature");
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(editText);
        editText.setEnabled(false);
        builder.setSingleChoiceItems(R.array.methods, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                position = i;

                if(position == 1){
                   editText.setEnabled(true);

                }else {
                    editText.setEnabled(false);
                }

            }
        });

        builder.setPositiveButton("Uploud", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String temp = null;

                if(position == 1){

                    temp = editText.getText().toString();
                    mListener.onPositiveButtonClicked(temp,position);
                }else {

                    mListener.onPositiveButtonClicked(temp,position);
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onNegativeBuuttonClicked();

            }
        });


        return  builder.create();
    }
}
