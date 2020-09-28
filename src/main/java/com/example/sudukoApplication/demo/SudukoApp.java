package com.example.sudukoApplication.demo;

import java.io.*;
import java.util.*;

public class SudukoApp {

    public void sudokuMethods(){

        LinkedHashSet<String> errorList = new LinkedHashSet<>();

        //Read the input provided and convert it into 2d array
        Integer[][] suduko2dArray = readInputFile();

        //check the validity by frequency
        HashMap<Integer, Integer> hm = checkValidityByFrequency(suduko2dArray, errorList);


        System.out.println("******* VALIDATE SUDUKO APPLICATION ********");

        //check the validity by appearance of each element. Each element should appear only once in each row, column and boxes.
        for(int row=0; row <9 ;row++){
            for(int column=0; column <9 ; column++){
                checkValidityByEachElementAppearance(suduko2dArray, row, column,errorList);
            }
        }


        //calculate the sum of each row, column and boxes.
        HashMap<String, Long> hmMap= calculateSumOfSuduko(suduko2dArray);


        //check the validty of suduko by sum. Sum of each row, column and boxes should be same.
        validateSudukobySum(hmMap,errorList);

        System.out.println("*** Input SUDUKO is **** :-->");
        for(int i=0  ; i <9 ; i++) {
            for(int j=0; j <9 ; j++) {
                System.out.print(suduko2dArray[i][j] +" ,");
            }
            System.out.println();
        }


        //displaying the output.If suduko is valid or not. If errorList is having no entry then suduko is valid otherwise not.
        if(errorList.size()==0){
            System.out.println("SUDUKO IS VALID. IT IS SOLVED CORRECTLY.");
        }
        else {
            System.out.println("SUDUKO  IS NOT VALID.");
            System.out.println("PLEASE HAVE A LOOK OVER THE ERRORS:-- ");
            for(String str : errorList){
                System.out.println(str);
            }
        }

    }


    //reading the suduko and converting it into 2d array
    public Integer[][] readInputFile() {

        File file = new File("/Users/sudhanshu/Documents/HTML project/sudukoTestFiles/sudukoetest2");

        BufferedReader br = null;


        Integer[][] suduko2dArray = new Integer[9][9];
        try {
            br = new BufferedReader(new FileReader(file));

            String st;

            int rowNumber=0;

            while (true) {
                if (!((st = br.readLine()) != null))
                    break;
                String[] sudukoArray = st.split(",");

                int columnNumber=0;
                for(String str:sudukoArray){

                    if(str!= null || !str.isEmpty()) {

                        suduko2dArray[rowNumber][columnNumber] = Integer.parseInt(str);

                        columnNumber++;
                    }
                }
                rowNumber++;

            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        }  catch(Exception e){
            System.out.println("Exception. Please check ur program");
            e.printStackTrace();
        }

        return suduko2dArray;

    }



//doing initial checks to see if suduko is valid or not
//check the frequency of each entered elements. There should be only 9 elements and each element should be repeated 9 times.
    public HashMap<Integer, Integer> checkValidityByFrequency(Integer[][] suduko2dArray, HashSet<String> errorList) {


        HashMap<Integer, Integer> hm = new HashMap();


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                int element = suduko2dArray[i][j];
                if (hm.containsKey(element)) {

                    int frequency = hm.get(element);
                    hm.put(element, frequency + 1);
                } else {
                    hm.put(element, 1);
                }
            }
        }

        //check if there is more than 9 elements entered or not.
        if (hm.size() > 9) {
            errorList.add("Suduko is having more than 9 different elements. It is invalid");
            errorList.add("The different elements and their frequency are :-- ");
            for (HashMap.Entry<Integer, Integer> mapElement : hm.entrySet()) {
                errorList.add("Element -> "+ mapElement.getKey() +" , Frequency  ->" +mapElement.getValue());
            }

        }


        //find the elemnts which are appearing more or less than 9 times.
        for (HashMap.Entry<Integer, Integer> mapElement : hm.entrySet()) {
            int key = mapElement.getKey();
            int value = mapElement.getValue();
            boolean b = true;
            if (value > 9) {
                errorList.add("There is more than expected number of elements.");
                errorList.add("Element ->"+ key +" appearing :->" +value + " instead of 9 times.");
                b = false;
            }

            if (value < 9) {
                errorList.add("There is less than expected number of elements.");
                errorList.add("Element ->"+ key +" appearing :->" +value + " instead of 9 times.");
                b = false;
            }
        }

        return hm;

    }

    //Check the appearance of each element position wise.
     public void checkValidityByEachElementAppearance(Integer[][] suduko2dArray, int row, int column, HashSet<String> errorList) {

        /*  for suduko to ve valid, each element should appear only once in each row , column and the 3*3 box in which they belong to.
         suduko is having 9 boxes.
         box structure
         box-1 box-2 box-3
         box-4 box-5 box-6
         box-7 box-8 box-9

         Each box is 3*3 square i.e 3 elememnts in the row and 3 elements in the column.
         On the above criteria we are checking positions where elements are repetitive.
         */

        LinkedHashMap<String, Integer> hmMap= new LinkedHashMap<>();

        String position = null;

        //checking row wise. Each element  should appear once
        for(int j=0; j <9; j++) {
             position = "(" +row + "," + j + ")";
            hmMap.put(position, suduko2dArray[row][j]);
        }

        //checking column wise. Each element should appear once.
        for(int i=0; i <9; i++) {

             position = "(" +i + "," + column + ")";
            hmMap.put(position, suduko2dArray[i][column]);

        }

        //checking  3*3 row-column wise i.e if there is repeated element
         int x_multiplier = multiplier(row);
        int y_multiplier = multiplier(column);
        int  i = 3 * x_multiplier;
        int imax = 3 * (x_multiplier+1);

        int j = 3 * y_multiplier;
        int jmax = 3* (y_multiplier+1);

        for( i = 3 * x_multiplier ; i< imax; i++){
            for(j = 3 * y_multiplier ; j < jmax ; j++){

                position = "(" +i + "," + j + ")";
                hmMap.put(position, suduko2dArray[i][j]);

            }
        }
        int element = suduko2dArray[row][column];

         String inputPosition = "(" + row + "," + column + ")";
         StringBuffer sb = new StringBuffer("");
         for (HashMap.Entry<String, Integer> mapElement : hmMap.entrySet()) {

             String key = mapElement.getKey();
             Integer value = mapElement.getValue();

             if (key != null && inputPosition != null && !key.equalsIgnoreCase(inputPosition)) {
                 if (value == element) {

                     sb.append(key + " , ");

                 }
             }

         }

         if (!(sb.length() == 0)){

             errorList.add("Checking elements on the basis of same row, column and the 3*3 square where it belongs to. ");
             errorList.add("There should be no repetition of the elements");
             errorList.add("Element of position  ->" + inputPosition + " and value-> " + element  + " is repeated at position:" + sb);

         }
     }

/*
     This multiplier helps to identify element belongs to which box. There are 9 boxes in total as described above.*/

     public Integer  multiplier( int value){
        if( value >=0 && value < 3 ){
          return 0;
        }
        else if( value >= 3 && value < 6){
            return 1;
        }
        else if(value >= 6 && value < 9 ){
           return 2;
        }
       else return null;
    }

    //if above conditions for appearance and frequncy in above functions is valid, we will check whether the suduko is correct or not.
    //Easiest way to check is to calculate sum of suduko for each row, column and 9 boxes.
    //So if all the  sums are correct, suduko is valid. There will be in total of 27 sums ( 9 rows, 9 columns and 9 boxes) calculated.
    //if at any place sum is incorrect with the rest, we can let the user know about this.

    public HashMap<String, Long> calculateSumOfSuduko(Integer[][] suduko2dArray){

        LinkedHashMap<String, Long> hashMap = new LinkedHashMap<>();

        String hmkey = null;

        int rowNumber =0;

        //calculating row wise sum
        for(int i=0; i<9; i++){
            Long sum=0L;
            for(int j=0;j<9;j++){

                sum= sum + suduko2dArray[i][j];
            }

            hmkey = "Row - " +rowNumber;

            hashMap.put(hmkey, sum);

            rowNumber ++;
        }

        //calculating column wise sum

        int columnNumber = 0;
        for(int j=0; j<9; j++){
            Long sum=0L;
            for(int i=0;i<9;i++){
                sum= sum + suduko2dArray[i][j];
            }

            hmkey = "Column - " +columnNumber;

            hashMap.put(hmkey, sum);

            columnNumber ++;
        }

        //calculating sum for each 3*3 row-column blocks

        int boxNumber = 0;
        int count =0;
        while (count <3) {

            int maxlimit = 3 * (count+1);

            for(int count1 =0; count1< 3; count1 ++) {
                Long sum =0L;


                int maxlimit1 = 3* (count1+1);
                for (int i= 3*count; i< maxlimit; i++) {

                    for (int j = 3 * count1 ; j < maxlimit1; j++) {
                        sum = sum + suduko2dArray[i][j];
                    }

                }

                hmkey = "Box - " +boxNumber;

                hashMap.put(hmkey, sum);

                boxNumber++;
            }

            count ++;
        }

        return hashMap;

    }

// Here from the input of above method, we  can check whether sum of all rows, columns and boxes
// is same or not and if it is not we will identify the user to check that particular row , column and box.
    public void validateSudukobySum(HashMap<String, Long> hmMap, HashSet<String> errorList){

         //hmSumFreq will calculate the frequency of each SUM.( SUM is the sum of rows, columns and boxes.
        HashMap <Long, Integer> hmSumFreq = new  HashMap();

        for (HashMap.Entry<String, Long> mapElement : hmMap.entrySet()) {

             String key= mapElement.getKey();
             Long value = mapElement.getValue();

             if(hmSumFreq.containsKey(value)){
                 int count = hmSumFreq.get(value);
                 hmSumFreq.put(value, count +1 );
             }else{
                 hmSumFreq.put(value, 1);
             }
        }


        //if frequency of hmSum is 1, then the suduko is fine else there are errors

        if(hmSumFreq.size() >1) {

            ArrayList<Integer> list = new ArrayList<>();

            //chcking the column which is having maximum frequency
            for (HashMap.Entry<Long, Integer> hm : hmSumFreq.entrySet()) {

                list.add(hm.getValue());
            }

            Collections.sort(list);

            int maxFrequency = list.get(list.size() - 1);

            //to find the rows and columns, boxes which should be checked

            Long findMaxFreqSum = 0L;

            for (HashMap.Entry<Long, Integer> hm : hmSumFreq.entrySet()) {

                if (hm.getValue() == maxFrequency) {
                    findMaxFreqSum = hm.getKey();
                }

            }


            StringBuffer sb = new StringBuffer("");
            for (HashMap.Entry<String, Long> mapElement : hmMap.entrySet()) {

                if (!(mapElement.getValue() == findMaxFreqSum)) {
                    sb.append(mapElement.getKey() + " | ");
                }

            }
            errorList.add("Please look over these rows and columns :-- >");
            errorList.add(sb.toString());

        }



    }

}


