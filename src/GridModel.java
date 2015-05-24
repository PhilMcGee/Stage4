/**
 * Created by Phil on 24/05/2015.
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GridModel {


    static int minrows = 3;
    static int mincols = minrows;
    static int maxrows = 5;
    static int maxcols = maxrows;
    static int minwordsize = minrows;
    static int maxwordsize = maxrows;
    static int wordsizerange = maxwordsize - minwordsize + 1;



    //GLOBAL main CLASSES
    static class WordList extends ArrayList<String> { //Declare array list type to hold fixed word length word lists
        //also consider a new class word instead of string
    }
    //xx replaced with char array: static class WordGrid extends ArrayList<StringBuilder> {//Declare array list type to hold word grids}

    public static void main(String[] args)
            throws FileNotFoundException {


        WordList[] AvailWords = new WordList[wordsizerange];//Array of wordlists each containing words of particular length
        for (int i = 0; i < wordsizerange; i++) {
            AvailWords[i] = new WordList();//Initialise arraylists for fixed length words
        }

        WordList[] FitWords = new WordList[(maxwordsize*2)-3];//Array of wordlists each containing words with particular char content
        for (int i = 0; i < (maxwordsize*2)-3; i++) {
            FitWords[i] = new WordList();//Initialise arraylists to hold words that fit multiple character criteria
        }
        //GLOBAL main FIXED VARIABLES INITIALISATION
        //grid size range

        System.out.print("grid size range " + minrows + "x" + mincols + " to " + maxrows + "x" + maxcols + ".");

        //**Temp Fixed - input to determine current active grid size
        int activewordsize = 4;//INPUT TEMP - GRID SIZE BEING MADE AND USED
        char[][] newWordGrid = new char[activewordsize][activewordsize];// Word Grid made up of chars


/*
        //MENU CONTROL
        boolean listLoaded = false;//True = viable word list has been loaded

        char menuMainSelect;
        boolean endProgram = false;
        menuMainSelect='f';//TEMP DATA ENTRY
        do{
            switch(menuMainSelect){
                case 'f' : //File import of wordlist

                    break;

                case 'p' : //Play game

                    if (listLoaded) {
                        //CALL GamePlay
                    }else{
                        System.out.println("No word list loaded. Load valid word list from file");
                        menuMainSelect='f';
                    }
                    break;

                case 'e' : //Exit program
                    endProgram = true;

                default :
                    System.out.println("Invalid input. Enter appropriate selection");

            }

        }while(!endProgram);
 */

        ArrayList<String> wordList = new ArrayList<String>(); //xx Holds all words from file list



        {//METHOD ImportWordList
            //NB cross check method - some words are duplicated in AvailWords - could be from word list used

            //Create Scanner object inFile and initialise to full list text file
            Scanner inFile = new Scanner(new FileReader("C:\\Users\\Phil\\Documents\\JCU\\CP5632 Computer Programming 1\\Programming Assignment\\commonEnglish4k.txt"));
            String currentWord;
            while ((inFile.hasNext())) {
                currentWord = inFile.next().toLowerCase().trim(); //Obtain next word in list
                //xx System.out.print(currentWord);
                //xx System.out.print(currentWord.length());
                //xx System.out.println(currentWord.length() - minwordsize);
                wordList.add(currentWord);//xx remove full wordList in final
                if ((currentWord.length() >= minwordsize) && (currentWord.length() <= maxwordsize)) {
                    AvailWords[currentWord.length() - minwordsize].add(currentWord); //add fixed length word to relevant list
                }
            }
            {//*xx test print word lists length, first word and last word:
                System.out.println("Full word list");
                System.out.println("Words = " + wordList.size() + " ; First word : "
                        + wordList.get(0) + " ; Last Word : "
                        + wordList.get(wordList.size() - 1));
                for (int i = 0; i < wordsizerange; i++) {
                    int currentwordsize = i + minwordsize;
                    System.out.println(currentwordsize + " character word list");
                    System.out.println("Words = " + AvailWords[i].size() + " ; First word : "
                            + AvailWords[i].get(0) + " ; Last Word : "
                            + AvailWords[i].get(AvailWords[i].size() - 1));
                }
            }
        }//END METHOD ImportWordList

        {//METHOD GenerateWordGrid

            //global variables
            int wordPosition;//index position for word meeting criteria
            char[] newWord = new char[activewordsize];
//            char[][] newWordGrid = new char[activewordsize][activewordsize];// Word Grid made up of chars
//            char[][] transposedGrid = new char[activewordsize][activewordsize];// Grid for transposing word grids
            char[] nullChar = new char[1];//define a null char! (for emptying contents of char arrays)(IJ Warning accepted)
            int numFitChars;
            int nextPosition;
            boolean charMatch;
            int level;
            int nextLevel=0;
            Random rand = new Random();//
            StringBuilder searchString = new StringBuilder();
            int fitWordIndex;
            StringBuilder[] wordAtLevel = new StringBuilder[activewordsize*2];
            for (int i = 0; i < activewordsize*2; i++) {
                wordAtLevel[i]=new StringBuilder();
            }

            do {//Complete full grid:
                do {//Locate next word for grid:
                    level=nextLevel;
                    //System.out.println("STARTING LEVEL"+level);
                    wordPosition = -1;//index position of suitable word; value -1 indicates suitable word not found.(IJ Warning accepted)
                    if(level==0){
                        //clear newWordGrid contents:
                        //System.out.println(("restart at level 0 - clearing grid"));
                        for (int r = 0; r < activewordsize; r++) {
                            for (int c = 0; c < activewordsize; c++) {
                                newWordGrid[r][c] = nullChar[0];
                            }
                        }
                        //GridPrint(newWordGrid);
                        //clear relevant FitWords contents:
                        //System.out.println("Clearing Fitwords for level: ");
                        for (int i=0;i < (activewordsize*2)-3; i++) {
                            //System.out.print(i+" ");
                            FitWords[i].clear();//CHECK clear contents only?
                        }
                        newWordGrid[0][0] = (char) (rand.nextInt(26) + 'a');//random integer 0<=r<=26 to pick letter
                    }
                    searchString.setLength(0);//clear SearchString
                    //SEARCH STRING CONSTRUCTED FROM COLUMN (COLUMN # LEVEL/2
                    for(int i=0;i<(Math.max(1, ((level + 1) / 2)));i++){//Build searchString from chars in newWordGrid
                        searchString.append(newWordGrid[level/2][i]);
                    }
                    //System.out.println("searchString="+searchString);
                    wordPosition = SubstringLocator(searchString.toString(), AvailWords[activewordsize - minwordsize],
                            0, AvailWords[activewordsize - minwordsize].size() - 1);//Locate searchString in AvailWords

                    if(wordPosition>=0){//Use identified word found:
                        newWord = AvailWords[activewordsize - minwordsize].get(wordPosition).toCharArray();
                    }
                    else{//Step backwards in level and check for alternative prior level words:
                        //#######CONSIDER NESTING CHANGE TO STEP BACK TO THIS POINT IF GRID NOT COMPLETED#########
                        while((level>2)&&(wordPosition<0)){//Try other words from FitWords from prior level(s)
                            //xxSystem.out.println("level="+level);//xx
                            //xxSystem.out.println("FitWords size for level-3 is "+FitWords[level-3].size());//xx
                            if(FitWords[level-3].size()>0){//Replace word in grid if alternative word is available
                                fitWordIndex=rand.nextInt(FitWords[level-3].size());
                                newWord=(FitWords[level-3].get(fitWordIndex).toCharArray());
                                FitWords[level-3].remove(fitWordIndex);
                                wordPosition=0;
                            }
                            else{
                                //Remove word from grid:
                                 /*System.out.println("Removing letters from grid: ");
                                 for(int i=activewordsize-1;i>=((level+1)/2-1);i--){//NB loop iteration check by trial - end loop reduced by 1
                                     System.out.println(" "+newWordGrid[level/2][i]);//xx
                                     newWordGrid[level/2][i]=nullChar[0];
                                 }*/ //test REMOVAL OF NEED TO EMPTY UNUSED CHARS FROM GRID (assumed unnecessary)
                                //System.out.print("level =" + level);
                                nextLevel=level-1;
                                if(nextLevel==2)nextLevel=0;//JUMP BACK RESET - OPTIONAL - SPEED MODIFICATION
                                //System.out.println("  new level ="+nextLevel);
                                {//Transpose grid:
                                    char[][] transposedGrid = new char[activewordsize][activewordsize];// Grid for transposing word grids
                                    for (int r = 0; r < activewordsize; r++) {
                                        for (int c = 0; c < activewordsize; c++) {
                                            transposedGrid[c][r] = newWordGrid[r][c];
                                        }
                                    }
                                    //GridPrint(newWordGrid);//xx
                                    //System.out.println("transposed grid:");//xx
                                    //GridPrint(transposedGrid);//xx

                                    newWordGrid = transposedGrid;
                                }
                            }
                            level=nextLevel;
                        }

                    }
                    if(wordPosition<0){//If no word is found to fit
                        //System.out.print("level =" + level);
                        nextLevel=Math.max(0, level - 1);//Decrease grid level if the target searchString is not found
                        if(nextLevel<=2){
                            nextLevel=0;//Restart if no word solutions for single starting char
                        }
                        //System.out.println("  new level ="+nextLevel);
                        {//Transpose grid:
                            char[][] transposedGrid = new char[activewordsize][activewordsize];// Grid for transposing word grids
                            for (int r = 0; r < activewordsize; r++) {
                                for (int c = 0; c < activewordsize; c++) {
                                    transposedGrid[c][r] = newWordGrid[r][c];
                                }
                            }
                            //GridPrint(newWordGrid);//xx
                            //System.out.println("transposed grid:");//xx
                            //GridPrint(transposedGrid);//xx

                            newWordGrid = transposedGrid;
                        }
                    }
                    //xxSystem.out.print("wordPosition=" + wordPosition + " ");
                }while(wordPosition < 0);
                if (level>2&&level<activewordsize-1) {//Create array list of viable words in FitWords for recursive use pending restart
                    //do for levels that have greater than 2 characters except for last level
                    numFitChars = (level+1)/2;
                    //System.out.println("Store valid words grid status");
                    //GridPrint(newWordGrid);
                    //System.out.print("Storing level " + level);
                    FitWords[level-3].add(String.valueOf(newWord));//add original word (NB: this needs to now be removed)
                    //System.out.print(String.valueOf(newWord) + " ");
                    for (int direction = -1; direction <= 1; direction = direction + 2) {//Backward then forward search
                        //xx System.out.println("wordPosition="+wordPosition);
                        nextPosition = wordPosition;
                        //System.out.print("dir:"+direction+"  ");
                        charMatch = true;
                        do {
                            //xx System.out.println("AvailWords["+(activewordsize-minwordsize)+"]nextPosition="+nextPosition);//xx
                            nextPosition = nextPosition + direction;
                            if(WithinRange(nextPosition,AvailWords[activewordsize - minwordsize])){
                                newWord = AvailWords[activewordsize - minwordsize].get(nextPosition).toCharArray();
                                for (int i = 0; i < numFitChars; i++){
                                    //System.out.println(newWord[i]+" ?=? "+newWordGrid[level/2][i]);
                                    charMatch = ((newWord[i] == newWordGrid[level/2][i]) && charMatch);//change charMatch to false if a character does not match

                                }
                                if (charMatch){
                                    FitWords[level-3].add(String.valueOf(newWord));
                                    //System.out.print("pos"+nextPosition+":"+String.valueOf(newWord)+" ");
                                }


                            }else charMatch=false;
                            //System.out.println(); //xx

                        } while (charMatch);
                    }
                }
                //System.out.println("new word found. Grid before adding:");
                //GridPrint(newWordGrid);
                newWordGrid[level/2] = newWord; //Add new word to grid BEFORE TRANSPOSE
                //System.out.println("Grid after adding:");
                //GridPrint(newWordGrid);//xx
                //System.out.print("level=" + level + " gridCompleted=" + gridCompleted);
                nextLevel=level+1;
                //System.out.println(" new level=" + nextLevel);
                {//Transpose grid:   ///
                    //System.out.println("transposing cells");
                    char[][] transposedGrid = new char[activewordsize][activewordsize];// Grid for transposing word grids
                    for (int r = 0; r < activewordsize; r++) {
                        for (int c = 0; c < activewordsize; c++) {
                            //System.out.print("r=" + r + " c=" + c);
                            transposedGrid[c][r] = newWordGrid[r][c];
                            //System.out.println( "newWordGrid r c ="+newWordGrid[r][c]+" transposedGrid c r ="+transposedGrid[c][r]);
                        }
                    }
                    //System.out.println("pre-transpose:");
                    //GridPrint(newWordGrid);//xx
                    //System.out.println("transposed grid:");//xx
                    //GridPrint(transposedGrid);//xx

                    wordAtLevel[level].setLength(0);
                    for (int i = 0; i < activewordsize; i++) {//amend list of valid words
                        wordAtLevel[level].append(newWord[i]);
                    }
                    //System.out.println("word at level 0 "+wordAtLevel[0]+" wordAtLevel "+level+" "+wordAtLevel[level]);
                    for (int i = 0; i < level; i++) {
                        //avoid grid symmetry outcomes CAN IMPLEMENT SYMMETRY, MIXED OR UNIQUE OPTIONS HERE USING CASE!
                        if (wordAtLevel[i].toString().equals(wordAtLevel[i+1].toString())) {
                            nextLevel=0;
                            //System.out.println("reset");
                        }

                    }


                    newWordGrid = transposedGrid;
//                newWordGrid[level/2] = newWord;//Add new word to grid AFTER TRANSPOSE?
                    //System.out.println("Updated grid");
                    //GridPrint(newWordGrid);//xx
                    //xx level = nextLevel;
                }
                //xx level=nextLevel;

            }while(level<activewordsize*2-1);

            GridPrint(newWordGrid);



        }//END METHOD GenerateWordGrid

        {//METHOD PlayGame
            System.out.println("PlayGame method commenced");

            char[][] displayWordGrid = new char[activewordsize][activewordsize];//screen display grid for current game status
            boolean[][] charDisplayStatus = new boolean[activewordsize][activewordsize];//char display status on display grid. True = valid char in place
            //initialise displayWordGrid and charDisplayStatuswith spaces:
            for (int i = 0; i < activewordsize; i++) {
                for (int j = 0; j < activewordsize; j++) {
                    displayWordGrid[i][j]=' ';
                    charDisplayStatus[i][j]=false;
                }
            }


            //Populate hint letter
            int grade = activewordsize+0; //-2 very hard; -1 hard; 0 normal;
            int rowPosition;
            int colPosition;
            int correctLetterCount=0;//counts correct character guesses made by user (excluding hint letters)
            int hintLetterCount=0;//counts number of hint letter generated
            char userInput=' ';
            char userGameAction = 'g';//g=guess;h=hint letter;e=exit
            //Populate initial hint letters [**TO CHANGE TO METHOD**]
/*            for (int i = 0; i < grade ; i++) {
                do {
                    rowPosition = (int)(Math.random()*activewordsize);
                    colPosition = (int)(Math.random()*activewordsize);
                    System.out.println("letter#="+i+"row="+rowPosition+" col="+colPosition);
                }while((charDisplayStatus[rowPosition][colPosition]));
                charDisplayStatus[rowPosition][colPosition]=true;
                displayWordGrid[rowPosition][colPosition]=newWordGrid[rowPosition][colPosition];
            }*/
            GridPrint(displayWordGrid);
            //Enter guessed letter, request hint or give up
            //##ENTER USER LETTER INPUT METHOD
            do{
                if(hintLetterCount>=grade){
                    System.out.println("enter action : g = guess; h = hint; e=exit");
                    try {
                        userGameAction = (char) System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userGameAction=Character.toLowerCase(userGameAction);

                }else userGameAction='h';//generate initial hint letters

                switch (userGameAction){

                    case 'g':
                        do{
                            do{
                                System.out.println("enter row");
                                try {
                                    userInput = (char) System.in.read();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }while(((int)userInput<48)||((int)userInput>48+activewordsize-1));
                            rowPosition=((int)userInput)-48;

                            do{
                                System.out.println("enter col");
                                try {
                                    userInput = (char) System.in.read();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }while(((int)userInput<48)||((int)userInput>(48+activewordsize-1)));
                            colPosition=((int)userInput)-48;

                        }while(charDisplayStatus[rowPosition][colPosition]);

                        do{
                            System.out.println("enter letter");
                            try {
                                userInput = (char) System.in.read();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }while(!Character.isLetter(userInput));
                        userInput=Character.toLowerCase(userInput);
                        if(userInput==newWordGrid[rowPosition][colPosition]){
                            System.out.println("correct!");
                            displayWordGrid[rowPosition][colPosition]=userInput;
                            charDisplayStatus[rowPosition][colPosition]=true;
                            correctLetterCount++;
                        }else {
                            System.out.println("incorrect");
                        }
                        GridPrint(displayWordGrid);
                        break;
                    case 'h': {
                        System.out.println("new hint letter");
                        do {
                            rowPosition = (int)(Math.random()*activewordsize);
                            colPosition = (int)(Math.random()*activewordsize);
                        }while((charDisplayStatus[rowPosition][colPosition]));
                        charDisplayStatus[rowPosition][colPosition]=true;
                        displayWordGrid[rowPosition][colPosition]=newWordGrid[rowPosition][colPosition];
                        hintLetterCount++;
                        break;
                    }
                }
                GridPrint(displayWordGrid);

            }while((Character.toLowerCase(userGameAction)!='e')&&((correctLetterCount+hintLetterCount)!=(activewordsize*activewordsize)));

        }//END METHOD PlayGame

    }//END MAIN METHOD

    public static void ImportWordList() {


    }

    public static void GenerateWordGrid() {


    }

    public static void PlayGame() {


    }

    public static void GridPrint(char[][] wordGrid){
        // GRID PRINT ROUTINE
        System.out.println("WORD GRID");
        System.out.println(wordGrid.length+" x "+wordGrid[0].length);
        System.out.println((" 01234"));
        StringBuilder SBprint = new StringBuilder("");
        for (int r = 0; r < wordGrid.length; r++) {
            System.out.print(r);
            for (int c = 0; c < wordGrid[0].length; c++) {
                SBprint.append(wordGrid[r][c]);
            }
            System.out.println(SBprint);
            SBprint.setLength(0);
        }

    }

    public static boolean WithinRange (int position, ArrayList<String> AvailWords){
        // METHOD Check if index position is within the arraylist range
        boolean inRange = false;
        if((position>=0)&&(position<=(AvailWords.size()-1)))inRange=true;
        return(inRange);
    }

    public static int SubstringLocator(String target, ArrayList<String> AvailWords , int positionLower, int positionUpper) {

        //METHOD SEARCH FOR SUBSTRING   29/4/2015
        //assumed: initial search range checked, with word not lying outside this range
        //Returns position of a valid word within the word list (returns -1 if matching substring not found)
        //Method variables:
        int position;//
        int relativePosition;
        //int relativePositionLower;// //xx= 10;
        //int relativePositionUpper;// //xx= -10;
        int targetLength = target.length();
        //int countTargetIdIterations = 0;//xx
        //Method:

        do {
            //##OPTIMAL SEARCH METHODOLOGY TO BE DETERMINED BY ITERATION TESTS
            //position = (positionLower+positionUpper)/2; //v.0. base case non random search by equal division
            position = (int) (Math.random()*(positionUpper-positionLower)+positionLower); //v.1. pure random search
            //position = (int) ((0.5 + (Math.random() / 2 * (relativePositionLower + relativePositionUpper) / (relativePositionLower - relativePositionUpper))) * (positionUpper - positionLower) + positionLower);//v.2. !!Goes out of range at boundaries!!
            //relativePositionLower = target.compareTo(AvailWords.get(positionLower).substring(0, targetLength));
            relativePosition = target.compareTo(AvailWords.get(position).substring(0, targetLength));
            //relativePositionUpper = target.compareTo(AvailWords.get(positionUpper).substring(0, targetLength));


            if (relativePosition < 0) {
                positionUpper = position;//Refine search range
                //relativePositionUpper = relativePosition;//Adjust random position generator weighting
            } else if (relativePosition > 0) {
                positionLower = position;//Refine search range
                //relativePositionLower = relativePosition;//Adjust random position generator weighting
            }
            //countTargetIdIterations = countTargetIdIterations + 1;
            /*//System.out.println(" lower pos: " + positionLower + "position: " + position + " upper pos: " + positionUpper +
                    " compare: " + relativePosition + " lower compare: " + relativePositionLower + " upper compare: "
                    + relativePositionUpper);
                    */

        } while (!((relativePosition == 0) || (positionUpper - positionLower < 2)));//??Need to reconsider this for treatment of no find
        /*//OUTCOME: One of the 3 positions holds the string OR string does not exist
        System.out.println("F lower pos: " + positionLower + "position: " + position + " upper pos: " + positionUpper +
                " compare: " + relativePosition + " lower compare: " + relativePositionLower + " upper compare: "
                + relativePositionUpper);
        System.out.println("target: " + target + " pos Lower word: " + AvailWords.get(positionLower) +
                " position word: " + AvailWords.get(position) + " pos Upper word: " + AvailWords.get(positionUpper));
        System.out.println("Target Located Iterations :" + countTargetIdIterations);
*/
        if(!(relativePosition==0))position=-1;//return sub string not found value

        return position;
    }
}

