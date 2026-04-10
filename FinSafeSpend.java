import java.util.*;
import java.io.FileWriter;
import java.io.IOException;


class FinSafeAccount
{
    private String acc_holdername;
    private double acc_balance;
    private ArrayList<Double> acc_transactions;
    public FinSafeAccount(String name,double initialBalance)
    {
        this.acc_holdername=name;
        this.acc_balance=initialBalance;
        this.acc_transactions=new ArrayList<>();
        logToFile("Account created for " + name + " Balance: " + acc_balance);
    }
    public void creditamount(double amount)
    {
        verify_amt(amount);
        acc_balance+=amount;
        addTransaction(amount);
        logToFile("Deposited " + amount + " Balance: " + acc_balance);
        System.out.printf("%.2f Deposited Successfully\n",amount);
    }

    
    public void ProcessTrnx(double amount) throws InsufficientFundsException {
        verify_amt(amount);
        if(acc_balance<amount)
        {
          logToFile("FAILED Withdrawal " + amount + " | Reason: Insufficient Balance");
          throw new InsufficientFundsException("Insufficient Balance amount");
        }
        acc_balance-=amount;
        addTransaction(-amount);
        logToFile("Withdrawn " + amount + " | Balance: " + acc_balance);
        System.out.printf("%.2f has been withdrawn Successfully.\n" ,amount);
    }
    private void verify_amt(double amount){
        if(amount==0 || amount<0){
            logToFile("FAILED Transaction | Invalid amount: " + amount);
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
    private void addTransaction(double amount)
    {
        acc_transactions.add(amount);
    
        if (acc_transactions.size()>5)
        {
            acc_transactions.remove(0);
        }
    }
     
    public void displayaccbalance()
    {  
        System.out.println("\nAccount Holder Name: "+acc_holdername);
        System.out.printf("Current Balance in the account: %.2f\n ",acc_balance);
    }
    public void showMiniStatement()
    {
        System.out.println("\nLast 5 Transactions");
        if(acc_transactions.isEmpty()){
            System.out.println("No transactions yet.");
            return;
        }
        int i=1;
        for(double  t: acc_transactions)
        if(t>0)
        {
            System.out.println(i++  +" Deposited Amount: "+t);
        }
        else{
            System.out.println(i++  +" WithDrawn Amount"+Math.abs(t));
        }
    }
    private void logToFile(String message) {
        try {
            FileWriter fw = new FileWriter("audit_log.txt", true);
            fw.write(message + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Logging had failed: " + e.getMessage());
        }
    }

}
class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message)
    {
        super(message);
    }
}
public class FinSafeSpend {
     static HashMap<String,String> users = new HashMap<>();
    static HashMap<String,FinSafeAccount> accounts = new HashMap<>();
    public static void showAuthMenu()
    {
        System.out.println("\n----FinSafe----");
        System.out.println("1.Register");
        System.out.println("2.Login");
        System.out.println("3.Exit");
        System.out.print("Enter your choice: ");
    }
    //Menu
    public static void ShowMenu()
    {
        System.out.println("\n----FinSafe----");
        System.out.println("1.Deposit the amount");
        System.out.println("2.WithDraw the amount");
        System.out.println("3.Check your Balance");
        System.out.println("4.View Transactions");
        System.out.println("5.Exit");
        System.out.println("Enter your choice");
    }
    public static void register(Scanner sc)
    {
        System.out.print("Enter your name: ");
        String username = sc.nextLine();

        if(users.containsKey(username))
        {
            System.out.println("User already exists!");
            return;
        }

        System.out.print("Enter your Password: ");
        String password=sc.nextLine();

        System.out.print("Enter Initial Balance u want to deposit: ");
        double balance=sc.nextDouble();
        sc.nextLine();

        users.put(username,password);
        accounts.put(username,new FinSafeAccount(username,balance));

        System.out.println("Registration successful!!!");
    }
    public static FinSafeAccount login(Scanner sc)
    {
        System.out.print("Enter Username: ");
        String username=sc.nextLine();

        if(!users.containsKey(username))
        {
            System.out.println("User not found!");
            return null;
        }

        System.out.print("Enter Password: ");
        String password=sc.nextLine();

        if(!users.get(username).equals(password))
        {
            System.out.println("Incorrect password!");
            return null;
        }

        System.out.println("Login successful!");
        return accounts.get(username);
    }
    //Input
    public static double getAmount(Scanner sc, String message)
    {
        System.out.print(message);
        return sc.nextDouble();
    }
    //operations
    public static void handleDeposit(Scanner sc,FinSafeAccount account)
            throws InsufficientFundsException
            {
                double amount=getAmount(sc,"Enter Amount to Depost:");
                account.creditamount(amount);
            }
    public static void handlewithdraw(Scanner sc,FinSafeAccount account)
            throws InsufficientFundsException
            {
                double amount=getAmount(sc,"Enter Amount to WithDraw:");
                account.ProcessTrnx(amount);
            }
    public static void walletMenu(Scanner sc,FinSafeAccount account)
    {
        int choice;
        do
        {
            ShowMenu();
            choice=sc.nextInt();
            try{
                switch(choice)
                {
                    case 1:
                        handleDeposit(sc,account);
                        break;
                    case 2:
                        handlewithdraw(sc, account);
                        break;
                    case 3:
                        account.displayaccbalance();
                        break;
                    case 4:
                        account.showMiniStatement();
                        break;
                    case 5:
                        System.out.println("Thankyou!!!");
                        return;
                    default:
                        System.out.println("Invalid Choice");

                }
            }
            catch(InsufficientFundsException e)
            {
                System.out.println("Error:"+e.getMessage());
            }
        }while(choice!=5);

    }
    //Main
    public static void main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
         while(true)
        {
            showAuthMenu();
            int choice=sc.nextInt();
            sc.nextLine();

            switch(choice)
            {
                case 1:
                    register(sc);
                    break;

                case 2:
                    FinSafeAccount account = login(sc);
                    if(account!=null)
                    {
                        walletMenu(sc,account);
                    }
                    break;

                case 3:
                    System.out.println("Thank you!!!");
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
        
        