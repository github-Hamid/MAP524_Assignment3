package com.example.assignment3;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileStoreManager {
static String fileName = "result.txt";
FileOutputStream fos;
FileInputStream fis;

public void writeNewResultToFile(Activity context, int correctAnswers, int totalQuestions)
{
    try
    {
        fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
        fos.write((Integer.toString(correctAnswers) + "-" + Integer.toString(totalQuestions)).getBytes());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public void deleteAllResults(Activity context)
{
    try {
        fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
        fos.write(("0-0").getBytes());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    finally {
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

public ArrayList<Integer> readResultFromFile(Activity context)
{
    ArrayList<Integer> result = new ArrayList<>(2);
    StringBuffer stringBuffer = new StringBuffer();
    try {
        fis = context.openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        int read;
        while ((read = inputStreamReader.read()) != -1)
        {
            stringBuffer.append((char)read);
        }
        result = fromStringToArrayofResult(stringBuffer.toString());
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    finally {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return result;
}


private ArrayList<Integer> fromStringToArrayofResult(String fileContent)
{

    ArrayList<Integer> resultArray = new ArrayList<>(2);
    for(int i = 0; i < fileContent.toCharArray().length; i++)
    {
        if(fileContent.toCharArray()[i] == '-')
        {
            int correctAnswers = Integer.parseInt(fileContent.substring(0,i));
            int totalQuestions =
                    Integer.parseInt(fileContent.substring(i+1, fileContent.toCharArray().length));
            resultArray.add(correctAnswers);
            resultArray.add(totalQuestions);
            break;
        }
    }
    return resultArray;
}


}
