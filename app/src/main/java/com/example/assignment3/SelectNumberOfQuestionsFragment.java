package com.example.assignment3;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectNumberOfQuestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectNumberOfQuestionsFragment extends DialogFragment {

    interface DialogClickListener{
        void okButtonClicked(int number);
        void cancelButtonClicked();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private int totalNumberOfQuestions;
    public static final String Tag = "tag";
    public DialogClickListener handler;

    public SelectNumberOfQuestionsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SelectNumberOfQuestionsFragment newInstance(int param1) {
        SelectNumberOfQuestionsFragment fragment = new SelectNumberOfQuestionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalNumberOfQuestions = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        TextView textView;
        EditText editText;
        Button okBtn;
        Button cancelBtn;
        View view = inflater.inflate(R.layout.fragment_select_number_of_questions, container, false);
        textView = view.findViewById(R.id.txt);
        editText = view.findViewById(R.id.numberOfQuestionsEditText);
        okBtn = view.findViewById(R.id.okBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        textView.setText(getResources().getString(R.string.selectNumberOfQuestionsText)
                + " " + totalNumberOfQuestions);

        okBtn.setOnClickListener(new View.OnClickListener() {
            int num;
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty())
                {
                    try{
                      num = Integer.parseInt(editText.getText().toString());
                      if(num > totalNumberOfQuestions || num <= 0)
                          num = -1;
                    }catch (Exception e)
                    {
                        num = -1;
                    }
                }
                else
                    num = -1;
                handler.okButtonClicked(num);
                dismiss();

            }
        });
         cancelBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 handler.cancelButtonClicked();
                 dismiss();
             }
         });
        return view;
    }
}