package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SelectNumberOfQuestionsFragment.DialogClickListener {

  Button trueButton;
  Button falseButton;
  ProgressBar progressBar;
ArrayList<Question_fragment> quiz;
int counter;
int numberOfCorrectAnswers;
int totalQuestions;
int numberOfBankQuestions;
FragmentManager fm = getSupportFragmentManager();
FileStoreManager fileStoreManager;
AlertDialog.Builder builder;
AlertDialog.Builder infoBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trueButton = findViewById(R.id.buttonTrue);
        falseButton = findViewById(R.id.buttonFalse);
        progressBar = findViewById(R.id.progressBar);
        fileStoreManager = ((MyAppManager)getApplication()).fileStoreManager;
        numberOfBankQuestions = ((MyAppManager)getApplication()).bankQuestions.size();
        builder = new AlertDialog.Builder(this);
        infoBuilder = new AlertDialog.Builder(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        if(savedInstanceState != null)
        {
            numberOfCorrectAnswers = savedInstanceState.getInt("correctAnswers");
            counter = savedInstanceState.getInt("counter");
            totalQuestions = savedInstanceState.getInt("totalQuestions");
            quiz = ((MyAppManager)getApplication()).manager.fragmentManager;
            if(counter < totalQuestions)
            {
                fm.beginTransaction().replace(R.id.fragmentContainer, quiz.get(counter)).commit();
                progressBar.setMax(totalQuestions);
                progressBar.setProgress(counter);
            }
           else
            {

                showAlertDialogForQuizResult();

            }

        }
        else
        {
            int defaultNumberOfQuestions = ((MyAppManager)getApplication()).bankQuestions.size();
            quiz = createQuiz(defaultNumberOfQuestions);
            counter = 0;
            numberOfCorrectAnswers = 0;
            totalQuestions = defaultNumberOfQuestions;
            fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fm.beginTransaction().add(R.id.fragmentContainer, quiz.get(0)).commit();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter",counter);
        outState.putInt("correctAnswers",numberOfCorrectAnswers);
        outState.putInt("totalQuestions", totalQuestions);


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberOfCorrectAnswers = savedInstanceState.getInt("correctAnswers");
        counter = savedInstanceState.getInt("counter");
        totalQuestions = savedInstanceState.getInt("totalQuestions");

    }

    //option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    //set click listener for menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch(id)
        {
            case R.id.menuAverage:
                showAlertDialogForTotalResult();
                break;
            case R.id.menuNumberOfQuestions:
                SelectNumberOfQuestionsFragment newNumberOfQuestions =
               SelectNumberOfQuestionsFragment.newInstance(numberOfBankQuestions);
                newNumberOfQuestions.handler = this;
                newNumberOfQuestions.show(fm, SelectNumberOfQuestionsFragment.Tag);
                break;
            case R.id.menuReset:
               fileStoreManager.deleteAllResults(MainActivity.this);
               Toast.makeText(this, getResources().getString(R.string.reset), Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }



    //set click listener for true and false buttons
    @Override
    public void onClick(View view) {

     int id = view.getId();
     switch(id)
     {
         case R.id.buttonTrue:
             if(quiz.get(counter).answer)
             {
                 numberOfCorrectAnswers++;
                 counter++;
                 Toast.makeText(this, getResources().getString(R.string.correctAnswer), Toast.LENGTH_SHORT).show();
             }
             else
             {
                 counter++;
                 Toast.makeText(this, getResources().getString(R.string.wrongAnswer), Toast.LENGTH_SHORT).show();
             }

             break;
         case R.id.buttonFalse:
             if(!quiz.get(counter).answer)
             {
                 numberOfCorrectAnswers++;
                 counter++;
                 Toast.makeText(this, getResources().getString(R.string.correctAnswer), Toast.LENGTH_SHORT).show();
             }
             else
             {
                 counter++;
                 Toast.makeText(this, getResources().getString(R.string.wrongAnswer), Toast.LENGTH_SHORT).show();
             }

             break;
     }

     //setting progress bar
        progressBar.setProgress(counter);

     if(counter < quiz.size())
     {
         fm.beginTransaction().replace(R.id.fragmentContainer, quiz.get(counter)).commit();

     }
     else
     {
         showAlertDialogForQuizResult();
     }
    }

  //create Quiz
    public ArrayList<Question_fragment> createQuiz(int numberOfQuestions)
    {

        progressBar.setMax(numberOfQuestions);
        progressBar.setProgress(0);


        Random random = new Random();
        int number;

        ArrayList<Integer> questionNumbers = new ArrayList<>(numberOfQuestions);
        ArrayList<Question> questions = ((MyAppManager)getApplication()).bankQuestions;
        ArrayList<Integer> colors = ((MyAppManager)getApplication()).bankColors;
        int numberOfBankQuestions = ((MyAppManager)getApplication()).bankQuestions.size();
         int numberOfColors = ((MyAppManager)getApplication()).bankColors.size();
        //array list for picking the random number for questions
        ArrayList<Integer> rangeOfQuestions = new ArrayList<>(numberOfBankQuestions);
        for(int i = 0 ; i < numberOfBankQuestions ; i++)
        {
            rangeOfQuestions.add(i);
        }

        for(int i = 0; i < numberOfQuestions; i++)
        {
            number = random.nextInt(rangeOfQuestions.size());
            questionNumbers.add(rangeOfQuestions.get(number));
            rangeOfQuestions.remove(number);
        }

        ((MyAppManager)getApplication()).manager.fragmentManager =
                new ArrayList<Question_fragment>(numberOfQuestions);

        for(int i = 0; i < numberOfQuestions; i++)
        {
            Question_fragment question_fragment =
           Question_fragment.newInstance(
            questions.get(questionNumbers.get(i)).question,
           colors.get(random.nextInt(numberOfColors)),
           questions.get(questionNumbers.get(i)).answer);

            ((MyAppManager)getApplication()).manager.fragmentManager.add(question_fragment);
        }

        return ((MyAppManager)getApplication()).manager.fragmentManager;
    }


    //show builder for total result
    public void showAlertDialogForTotalResult()
    {
        ArrayList<Integer> result = fileStoreManager.readResultFromFile(MainActivity.this);
        infoBuilder.setMessage(getResources().getString(R.string.showTotalResult) +
                result.get(0) + getResources().getString(R.string.direction) + result.get(1)).setCancelable(true).show();
    }


    //show builder for quiz result
    public void showAlertDialogForQuizResult()
    {
        builder.setMessage(getResources().getString(R.string.scoreTextBegin) + " "
                + numberOfCorrectAnswers + " " + getResources().getString(R.string.scoreTextEnd) +
                " " + totalQuestions)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.saveButton)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<Integer> result = fileStoreManager.readResultFromFile(MainActivity.this);
                                int newCorrectAnswers = result.get(0) + numberOfCorrectAnswers;
                                int newTotalQuestions = result.get(1) + totalQuestions;
                                fileStoreManager.writeNewResultToFile(MainActivity.this,newCorrectAnswers,newTotalQuestions);
                                counter = 0;
                                numberOfCorrectAnswers = 0;
                                quiz = createQuiz(totalQuestions);
                                fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fm.beginTransaction().replace(R.id.fragmentContainer, quiz.get(0)).commit();

                            }
                        })
                .setNegativeButton(getResources().getString(R.string.ignoreButton),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                counter = 0;
                                numberOfCorrectAnswers = 0;
                                quiz = createQuiz(totalQuestions);
                                fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                fm.beginTransaction().replace(R.id.fragmentContainer, quiz.get(0)).commit();
                            }
                        }).show();
    }


    //listener for Select number of questions fragment buttons
    @Override
    public void okButtonClicked(int number) {
      if(number != -1)
      {
          quiz = createQuiz(number);
          numberOfCorrectAnswers = 0;
          totalQuestions = number;
          counter = 0;
          fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
          fm.beginTransaction().replace(R.id.fragmentContainer, quiz.get(0)).commit();
      }
      else
      {
          infoBuilder.setMessage("Invalid input! please enter a number less than or equal " + numberOfBankQuestions)
                  .setCancelable(true).show();
      }

    }

    @Override
    public void cancelButtonClicked() {
      return;
    }
}