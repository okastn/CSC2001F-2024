import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * This class implements a GenericsKbAVL application, which utilizes an AVL tree
 * to store knowledge base information.
 */
public class GenericsKbAVLApp{
  
    /* A class that represents a node in the binary search tree */
    public class TreeNode{
        private TreeNode left;
        private TreeNode right;
        private String[] data;
        int height;
        
        public TreeNode(String[] data){     /* Stores term,statement and confidence score */
            this.data = data;
            this.left = null;
            this.right = null;
            this.height = 0;
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
    private String knowledgeBasefileName;    /* Name of the file containing the knowledge base */
    private String queryFileName;
    int opCountInsert;    // Stores the number of comparison operations performed for the AVL insert
    int opCountSearch;    // Stores the number of comparison operations performed for the AVL search
    int countLine;
    String OutputDatafileName;
    int numberOfFiles;
    int prevCountLine;
    int prevOpCountInsert;
    int prevOpCountSearch;      
    private ArrayList<int[]> outputArray = new ArrayList<>();

    public GenericsKbAVLApp(){              /** Default constructor for the GenericsKbAVLApp class that Initializes member variables.*/
        this.knowledgeBasefileName = "";
        this.queryFileName = "";
        this.OutputDatafileName ="";
        root =null;
        this.opCountInsert = 0;
        this.opCountSearch = 0;
        this.countLine = 0;
        this.numberOfFiles=0;
    }

    public TreeNode getRoot(){
        return root;
    }
    public void setRoot(TreeNode root){
        this.root = root;
    }
    public int getOpCountInsert(){   //Getter method for the number of insert comparison operations performed during AVL search.
        return opCountInsert;
    }
    
    public int getOpCountSearch(){      //Getter method for the number of search comparison operations performed during AVL search.
        return opCountSearch;
    }
    public int getCountLine(){          // Getter method for the total number of lines in the loaded query file
        return countLine;
    }
    public void setCounters() { /**resets the counters used for tracking operations and lines.*/
        this.opCountSearch = 0;
        this.opCountInsert = 0;
        this.countLine = 0;
    }
    public int height (TreeNode node ){           /* Calculates the height of a TreeNode in the AVL tree. **/
      if (node != null)
         return node.height;
      return -1;
    }
    public void fixHeight ( TreeNode node ){        // Updates the height of a TreeNode in the AVL tree.
        node.height = Math.max (height (node.left), height (node.right)) + 1;
    }
        public int balanceFactor (TreeNode node ){      // Calculates the balance factor of the TreeNode which is difference between right and left subtree heights
        return height (node.right) - height (node.left);
    }
    public TreeNode rotateRight ( TreeNode p ){
        TreeNode q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight (q);
        return q;
        }
    public TreeNode rotateLeft ( TreeNode q ){
        TreeNode p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }
    
    public TreeNode balance ( TreeNode p ){     // Rebalances a subtree in the AVL tree when needed.
        fixHeight (p);
        if (balanceFactor (p) == 2)
        {
            if (balanceFactor (p.right) < 0)
                p.right = rotateRight (p.right);
            return rotateLeft (p);
        }
        if (balanceFactor (p) == -2)
        {
            if (balanceFactor (p.left) > 0)
                p.left = rotateLeft (p.left);
            return rotateRight (p);
        }
        return p;
    }
    /*
     * Loads the knowledge base from a file.
     * Reads the file line by line.
     * Splits each line by tab ("\t") to separate term, statement, and score.
     * Calls the 'insert' method to add each line (data) to the tree.
     * returns true if successful, false if file not found.
     */
    public boolean loadFileToAVL(){
        File file = new File(knowledgeBasefileName);
        try{
            Scanner input = new Scanner(file);
            while (input.hasNextLine()){
                String line = input.nextLine();
                insert(line.split("\t"));
            }
            input.close();
            return true; 
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }
    /**Loads the query from a file then return True if successful, false if file not found.FileNotFoundException If the query file does not exist.**/
    public boolean loadQueryFileToAVL(){    
        File file = new File(queryFileName);
        try{
            Scanner input = new Scanner(file);
            
            while (input.hasNextLine()){
                String line = input.nextLine();
                countLine++;        //increments the number of terms in query
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

        public void insert(String[] data) {
            root = insert(data, root);
        }
        /* adds node to the AVL tree 
        * if the AVL tree is empty, it creates a new node
        * Otherwise, traverses the tree and inserts is added as the left node if the new node is less than the root node,
        * and if the new node is greater than the current node, it is added as the right node.
        * Updates the balance factor and fixHeight.The new root node of the subtree after insertion and balancing.
        */
        public TreeNode insert(String[] data, TreeNode root ){
            if (root == null){
                return root = new TreeNode(data);
            }
            else {
                TreeNode fast = root;
                TreeNode slow = fast;
                while (fast != null) {
                    slow = fast;
                    opCountInsert++;
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
                    opCountInsert++;
                    if (slow.data[0].compareTo(data[0]) > 0){
                        slow.left = new TreeNode(data);
                    }
                    else {
                        slow.right = new TreeNode(data);
                    }
                }
            }
            fixHeight(root);
            balanceFactor(root);
            return balance(root);  //rebalance tree
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
        opCountSearch++;
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
    public String SearchByTerm(String term, TreeNode node) {
        // Load query data directly from file
        File queryFile = new File(queryFileName);
        try  {
            Scanner queryInput = new Scanner(queryFile);
            
            while (queryInput.hasNextLine()) {
                String queryTerm = queryInput.nextLine().trim();
                opCountSearch++;
                if (queryTerm.equals(term)  ) {
                    opCountSearch++;
                    // Term not found in query file, proceed to search in the AVL tree
                    String[] resultFromAVLTree = searchByTerm(term, node);
                    // Check if term is found in the AVL tree
                    if (!resultFromAVLTree[0].equals("None")) {
                        return  term + ": " + resultFromAVLTree[1] + "(" + resultFromAVLTree[2] + ")"+"\n"; // Return AVL tree data if found                      
                    } else {
                        // Term not found in query file or AVL tree
                        return"Term not found: " + term+"\n ";    }    
                }
            }
            return "Term not found: " + term;
        } catch (FileNotFoundException e) {
            return "Error loading query file";
        }
    }

    public void updateOutputArray() {       // Updates the outputArray with the previous counters before processing a new query file.
        int[] prevCounters = new int[]{prevCountLine, prevOpCountInsert, prevOpCountSearch};
        outputArray.add(prevCounters);      
    }
    public void CounterResultsFile() {  //Saves the counters (number of lines, insert/search operations) to a file named "results.txt".
        try (PrintWriter writer = new PrintWriter("results.txt")) {
            // Print previous counters
            if (numberOfFiles > 0) {
                for (int[] row : outputArray) {
                    writer.println(row[0] + "\t" + row[1] + "\t" + row[2]);
                }
            }   //print the final counters
            int[] currentCounters = new int[]{getCountLine(), getOpCountInsert(), getOpCountSearch()};
            
            writer.println(currentCounters[0] + "\t" + currentCounters[1] + "\t" + currentCounters[2]);
        } catch (FileNotFoundException e) {
            System.out.println("Error saving counters to file: ");
        }
    }
    
    
    
    /**
     * This method implements the main menu and user interaction for the application.
     * It presents a menu with options to:
     * 1. Load a knowledge base and queryfrom a file .
     * 2. Search for a statement by term in query file.
     * 3. Quit the application.
      * The method reads user input and calls appropriate methods based on the chosen option.
     */
    public void mainThread(){
        Scanner sc = new Scanner(System.in);
        String term;
        
        while (true) {
            System.out.println("Choose an action from the menu:");
            System.out.println("1. Load a knowledge base from a file");
            System.out.println("2. Search for an item in the knowledge base by term");
            System.out.println("3. Quit");
            System.out.println("");
            System.out.print("Enter your choice: ");
            int userInput = sc.nextInt();
            sc.nextLine();
            System.out.println("");
            if (userInput == 1) {
                if (numberOfFiles > 0) {
                    System.out.println("Previous query file statistics:");
                    System.out.println("Number of lines: " + prevCountLine);
                    System.out.println("Number of insert operations: " + prevOpCountInsert);
                    System.out.println("Number of search operations: " + prevOpCountSearch);
                    System.out.println("");
                }
                numberOfFiles++;
                setCounters(); // Reset counters before new query file
                System.out.print("Enter knowledge base file name: ");
                knowledgeBasefileName = sc.nextLine();
                loadFileToAVL();
                System.out.print("Load a query/list from a file: ");
                queryFileName = sc.nextLine();
                loadQueryFileToAVL();
                System.out.println("");
                System.out.println("Knowledge base loaded successfully.");
                System.out.println("");
                updateOutputArray(); // Add previous counters to outputArray
                CounterResultsFile();// Save counters after each search specificalty to the query file
            } else if (userInput == 2) {
                System.out.print("Enter the term to search: ");
                term = sc.nextLine();
                System.out.println(SearchByTerm(term, root));
                prevCountLine = getCountLine();
                prevOpCountInsert = getOpCountInsert();
                prevOpCountSearch = getOpCountSearch();
            } else {
                System.out.println("Number of lines: " + getCountLine());
                System.out.println("Number of insert operations: " + getOpCountInsert());
                System.out.println("Number of search operations: " + getOpCountSearch());
                CounterResultsFile(); // Save final counters
                break;
            }
        }
        sc.close();       
    }
}
    

