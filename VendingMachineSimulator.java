import java.util.Scanner;
public class VendingMachineSimulator {
    class Product {
        int id;
        String name;
        double price;
        int stock;
        Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }
    Product[] products = new Product[5];
    VendingMachineSimulator() {
        products[0] = new Product(1, "Coke", 20, 5);
        products[1] = new Product(2, "Pepsi", 35, 3);
        products[2] = new Product(3, "Water", 20, 8);
        products[3] = new Product(4, "Chips", 25, 6);
        products[4] = new Product(5, "Chocolate",40, 3);
    }
    public void start() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n********** VENDING MACHINE **********");
            displayProducts();
            System.out.print("\nEnter Product ID : ");
            int id = sc.nextInt();
            if (id < 1 || id > 5) {
                System.out.println("Invalid Product ID");
                continue;
            }
            Product p = products[id - 1];
            if (p.stock == 0) {
                System.out.println("Out Of Stock");
                continue;
            }
            System.out.println("\nYou Selected : " + p.name);
            System.out.println("Price : ₹" + p.price);
            System.out.println("\nSelect Payment");
            System.out.println("1. UPI");
            System.out.print("To Scan UPI Press 1 : ");
            int choice = sc.nextInt();
            boolean success = false;
            double paid = 0;
            String paymentMethod = "";
            switch (choice) {
                case 1:
                    paymentMethod = "UPI";
                    System.out.print("Enter UPI Amount : ");
                    paid = sc.nextDouble();
                    if (paid == p.price) {
                        success = true;
                        System.out.println("Processing Payment...");
                        System.out.println("Payment Successful");
                    } else {
                        System.out.println("Payment Failed");
                    }
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
            if (success) {
                p.stock--;
                System.out.println("\nDispensing " + p.name + "...");
                System.out.println("\n---------------------------------------------");
                System.out.println("RECEIPT");
                System.out.println("---------------------------------------------");
                System.out.println("Product        : " + p.name);
                System.out.println("Price          : ₹" + p.price);
                System.out.println("Payment Method : " + paymentMethod);
                System.out.println("Amount Paid    : ₹" + paid);
                System.out.println("Status         : Success");
                System.out.println("Remaining Stock: " + p.stock);
                System.out.println("---------------------------------------------");
            }
                System.out.println("\nThank You for Visiting!");
                System.out.println("Please Visit Again.");
                break;
        }
    }
    public void displayProducts() {
        System.out.println("----------------------------------------------");
        System.out.println("ID\tProduct\t\tPrice\tStock");
        System.out.println("----------------------------------------------");
        for (Product p : products) {
            System.out.println(p.id + "\t" + p.name + "\t\t₹" + p.price + "\t" + p.stock);
        }
        System.out.println("----------------------------------------------");
    }
    public static void main(String[] args) {
        VendingMachineSimulator vm = new VendingMachineSimulator();
        vm.start();
    }
}
