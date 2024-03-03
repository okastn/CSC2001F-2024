import java.util.Scanner;
public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter app to test:");
        System.out.println("1. Array app");
        System.out.println("2. BST app");

        int input = sc.nextInt();
        sc.nextLine();
        if (input == 1){
            GenericsKbArrayApp arrayApp = new GenericsKbArrayApp(100000);
            arrayApp.mainThread();
        }
        else{
            GenericsKbBSTApp bstApp = new GenericsKbBSTApp();
            bstApp.mainThread();
        }
        sc.close();
    }
}
