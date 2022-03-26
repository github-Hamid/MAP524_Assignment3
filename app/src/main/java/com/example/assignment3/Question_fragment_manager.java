package com.example.assignment3;

import java.util.ArrayList;

public class Question_fragment_manager {
    ArrayList<Question_fragment> fragmentManager = new ArrayList<Question_fragment>(0);

    public void addFragment(Question_fragment q)
    {
        fragmentManager.add(q);
    }
}
