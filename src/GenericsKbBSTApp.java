import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class GenericsKbBSTApp {
    /* A class that represents a node in the binary search tree */
    public class TreeNode{
        private TreeNode left;
        private TreeNode right;
        private String[] data;
        public TreeNode(String[] data){     /* Stores term,statement and confidence score */
            this.data = data;
            this.left = null;
            this.right = null;
        }
        public String[] getData(){
            return data;
        }
        public TreeNode getRight(){
            return right;
        }
        public TreeNode getLeft(){
            return left;
        }
    }
    private TreeNode root;  /* Root node of the binary search tree */
    private String fileName;    /* Name of the file containing the knowledge base */
    public GenericsKbBSTApp(){
        this.fileName = "";
        root =null;
    }
    /* adds node to the binary tree 
     * if the binary tree is empty, it creates a new node
     * Otherwise, traverses the tree and inserts is added as the left node if the new node is less than the root node,
     * and if the new node is greater than the current node, it is added as the right node.
     * Updates the existing statement and confidence score if the term is found and the new confidence score is higher.
    */
    public void insert(String[] data){
        if (root == null){
            root = new TreeNode(data);
            return;
        }
        else {
            TreeNode fast = root;
            TreeNode slow = fast;
            while (fast != null) {
                slow = fast;
                if (data[0].compareTo(fast.data[0]) < 0){   /*Compare terms*/
                    fast = fast.left;
                }
                else if (data[0].compareTo(fast.data[0]) > 0){
                    fast = fast.right;
                }
                else  {
                    if (data[2].compareTo(fast.data[2]) >= 0){  /* Terms are equal, update if new  confidence score is higher */
                        fast.data[2] = data[2];
                        fast.data[1] = data[1];             
                    }
                    break;
                }    
            }
            if (fast == null){      /*  Reached end of appropriate branch, insert new node */
                if (slow.data[0].compareTo(data[0]) > 0){
                    slow.left = new TreeNode(data);
                }
                else {
                    slow.right = new TreeNode(data);
                }
            }
        }
    }
    public TreeNode getRoot(){
        return root;
    }
    public void setRoot(TreeNode root){
        this.root = root;
    }
    /*
     * Loads the knowledge base from a file.
     * Reads the file line by line.
     * Splits each line by tab ("\t") to separate term, statement, and score.
     * Calls the 'insert' method to add each line (data) to the tree.
     * Returns true if successful, false if file not found.
     */
    public boolean loadFileToBST(){
        File file = new File(fileName);
        try{
            Scanner input = new Scanner(file);
            String line;
            while (input.hasNextLine()){
                line = input.nextLine();
                insert(line.split("\t"));
            }
            input.close();
            return true; 
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }
    void inorder(TreeNode root){        /* Inorder traversal to print the terms alphabetically in the tree */
        if (root != null){
            inorder(root.left);
            System.out.println(root.data[0]);
            inorder(root.right);
        }
    }
    /*
     * Searches for a term in the tree and returns an array containing
     * the term, statement, and confidence score if found, or an array
     * with "None" values if not found.
     */
    public String[] searchByTerm(String term, TreeNode node) {
        if (node == null) {
            String[] res = {"None","None","0.0"};
            return res;
        }
        if (node.data[0].equals(term)) {
            return node.data;
             } 
        else if (node.data[0].compareTo(term) > 0) {
            return searchByTerm(term, node.left);
        } 
        else {
            return searchByTerm(term, node.right);
        }
    }
    /*
     * Searches for a term and sentence in the tree and returns an array containing
     * the term, statement, and confidence score if found, or an array
     * with "None" values if not found.
     */
     public String searchByTermAndSentence( TreeNode node, String term, String sentence) {
        if (node == null) {
            return "None";
        }
        if (node.data[0].equals(term) && node.data[1].equals(sentence)) {
            return node.data[2];
        } 
        else if (node.data[0].compareTo(term) > 0) {
            return searchByTermAndSentence(node.left, term, sentence);
        } 
        else {
            return searchByTermAndSentence( node.right,term,sentence);
        }
    }
    /**
         * Add a new statement to the knowledge base for the given term. 
         * It creates a new data array containing the term, sentence, and score.
         * It earches for the term in the tree using 'searchByTerm'.
         * If the term is found it compares the new confidence score with the existing one.
         * If the new score is higher, updates the statement and score in the tree.
         * If the term is not found, inserts the new data into the tree.
         * Returns true if the statement was added or updated, false otherwise.
         */
    public boolean addNewStatement(String term, String sentence, String score){
          String[] data = {term,sentence,score};
          String[] res = searchByTerm(term, root);
          String[] resCopy = new String[3];
          resCopy[0] = res[0];
          resCopy[1] = res[1];
          resCopy[2] = res[2];
          insert(data);
           res = searchByTerm(term, root);
          if (resCopy[0].equals(res[0])){
            return !(res[1].equals(resCopy[1]));
          }
          return false;
    }
    /**
     * This method implements the main menu and user interaction for the application.
     * It presents a menu with options to:
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
                loadFileToBST();
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
                String score = sc.nextLine();
               boolean res = addNewStatement(term, sentence, score);
                System.out.println("");
                if (res){
                    System.out.println("Statement for term "+term+" has been updated");
                }
                else{
                System.out.println("");
                }
            }
            else if (userInput == 3){
                System.out.print("Enter the term to search: ");
                term = sc.nextLine();
                String[] row = searchByTerm(term,root);
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
                String res = searchByTermAndSentence(root,term,sentence);
                if (!res.equals("None")){
                    System.out.println("The statement was found and has a confidence score of "+res+".");
                }
                else {
                    System.out.println("Statement not found");
                }
                System.out.println("");
            }
            else {
                break;
            }
        } 
        sc.close();         
    }
}