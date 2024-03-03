import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class GenericsKbArrayApp {
    private String fileName;    /*Stores the file name of the knowledge base */
    private String[][] genericsKbArray;     /*A 2D array to store the knowledge base data (term, statement, confidence score) */
   
    /**
     * Constructor that initializes the 'fileName' to an empty string and creates a 2D String array
     * of the specified size to store the knowledge base data.
     *
     * @param arrayLength The size of the 2D array (number of rows)
     */
    public GenericsKbArrayApp(int arrayLength){
        this.fileName = "";
        genericsKbArray = new String[arrayLength][3];   /*3 columns for term, statement, and score */
    }

     /**
     * Loads the knowledge base from a file into the `genericsKbArray`.
     *
     * @return True if the file is loaded successfully, False otherwise.
     */
    public boolean loadFileToArray(){
        File file = new File(fileName);
        try{
            Scanner input = new Scanner(file);
            int i = 0;
            String data;
            while (input.hasNextLine()){
                data = input.nextLine();
                genericsKbArray[i] = data.split("\t");
                i++;
            }
            input.close();
            return true; 
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }
    /**
     * Searches for a statement in the knowledge base by both term and exact sentence.
     * @param target The term to search for.
     * @param sentence The exact sentence to search for.
     * @return The confidence score associated with the matching statement, or "None" if not found.
     */
    public String searchByTermAndSentence(String target, String sentence ){
        for (String[] row : genericsKbArray){
            if (row[0] == null){
                break;  // Reached the end of the filled array entries
            }
            else if (row[0].equals(target) && row[1].equals(sentence)){
                return row[2];      /*Return the confidence score if term and sentence match */
            }
        }
        return "None";  /*Return the confidence score if term and sentence match */
    }

    /**
     * Searches for a statement in the knowledge base by term.
     * @param target The term to search for.
     * @return An array containing the term, statement, and confidence score if found,
     * or an array containing only "None" if not found.
     */
    public String[] searchByTerm(String target ){
        for (String[] row : genericsKbArray){
            if (row[0] == null){
                break;      /*  Reached the end of the filled array entries */
            }
            else if (row[0].equals(target)){
                return row;     /* Return the entire row if the term is found */
            }
        }
        String[] res = {"None"};
        return res;
    }

    /**
     * Attempts to add a new statement to the knowledge base for the given term.
     * @param term The term for the new statement.
     * @param sentence The statement to add.
     * @param score The confidence score for the statement.
     * @return True if the statement is added or updated, False otherwise.
     */
    public boolean addNewStatement(String term, String sentence, double score){
        String[] row = searchByTerm(term);  /*Search for existing term */
        if (row.length == 3){
            //Term exists in array
            double rowScore = Double.parseDouble(row[2]);
            if (score >= rowScore){
                // update confidence score and sentence if the user input score is higher
                row[1] = sentence;
                row[2] = Double.toString(score);
                return true;
            }
            // statement not updated if the user input score is lower
            return false;
        }
        return false;
    }
        /**
     * Implements the main menu and user interaction for the application.
     * This method presents a menu with options to:
     * 1. Load a knowledge base from a file.
     * 2. Add a new statement to the knowledge base.
     * 3. Search for a statement by term.
     * 4. Search for a statement by term and exact sentence.
     * 5. Quit the application.
     * The method reads user input and calls appropriate methods based on the chosen option.
     */
    public void mainThread(){
        Scanner sc = new Scanner(System.in);
        String term;
        String sentence;
        while (true) {
            System.out.println("Choose an action from the menu:");
            System.out.println("1. Load a knowledge base from a file");
            System.out.println("2. Add a new statement to the knowledge base");
            System.out.println("3. Search for an item in the knowledge base by term");
            System.out.println("4. Search for a item in the knowledge base by term and sentence");
            System.out.println("5. Quit");
            System.out.println("");
            System.out.print("Enter your choice: ");
            int userInput = sc.nextInt();
            sc.nextLine();
            System.out.println("");
            if (userInput == 1){
                System.out.print("Enter file name: ");
                fileName = sc.nextLine();
                loadFileToArray();
                System.out.println("");
                System.out.println("Knowledge base loaded successfully.");
                System.out.println("");
            }
            else if (userInput == 2){
                System.out.print("Enter the term: ");
                term = sc.nextLine();
                System.out.print("Enter the statement: ");
                sentence = sc.nextLine();
                System.out.print("Enter the confidence score: ");
                double score = sc.nextDouble();
                sc.nextLine();
               boolean modified = addNewStatement(term, sentence, score);
                System.out.println("");
                if (modified){
                    System.out.println("Statement for term "+term+" has been updated.");
                }
                else{
                    System.out.println("Confidence score is low.");
                }
                
                
            }
            else if (userInput == 3){
                System.out.print("Enter the term to search: ");
                term = sc.nextLine();
                String[] row = searchByTerm(term);
                if (row.length == 3){
                    System.out.println("Statement found: "+row[1]+" (Confidence score: "+row[2]+")");
                }
                else {
                    System.out.println("Statement not found");
                } 
                System.out.println("");  
            }
            else if (userInput == 4){
                System.out.print("Enter the term: ");
                term = sc.nextLine();
                System.out.print("Enter the statement to search for: ");
                sentence = sc.nextLine();
                System.out.println("");
                String res = searchByTermAndSentence(term, sentence);
                if (!res.equals("None")){
                    System.out.println("The statement was found and has a confidence score of "+res+".");
                }
                else {
                    System.out.println("Statement not found");
                }
                System.out.println("");
            }
            else {
                break;      /*  Exit the loop and close the scanner */
            }
        } 
        sc.close();         
    }
}

