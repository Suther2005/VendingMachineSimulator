import java.util.InputMismatchException;
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
    double totalSales = 0.0;

    VendingMachineSimulator() {
        products[0] = new Product(1, "Coke", 20, 5);
        products[1] = new Product(2, "Pepsi", 35, 3);
        products[2] = new Product(3, "Water", 20, 8);
        products[3] = new Product(4, "Chips", 25, 6);
        products[4] = new Product(5, "Chocolate", 40, 3);
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n********** VENDING MACHINE **********");
            displayProducts();
            System.out.println("0. Exit");
            System.out.print("\nEnter Product ID (or 0 to exit) : ");
            
            int id = -1;
            try {
                id = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine(); // Clear the invalid input from scanner
                continue;
            }

            if (id == 0) {
                System.out.println("\nThank You for Visiting!");
                System.out.println("Please Visit Again.");
                break;
            } else if (id == 999) {
                adminMode(sc);
                continue;
            } else if (id < 1 || id > 5) {
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
            System.out.println("\nSelect Payment Method");
            System.out.println("1. UPI");
            System.out.println("2. Cash");
            System.out.println("3. Card");
            System.out.print("Enter choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Transaction cancelled.");
                sc.nextLine(); // Clear input
                continue;
            }

            boolean success = false;
            double paid = 0;
            String paymentMethod = "";
            double change = 0;

            switch (choice) {
                case 1:
                    paymentMethod = "UPI";
                    System.out.print("Enter UPI Amount : ");
                    try {
                        paid = sc.nextDouble();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid amount! Transaction cancelled.");
                        sc.nextLine();
                        continue;
                    }
                    if (paid == p.price) {
                        success = true;
                        System.out.println("Processing Payment...");
                        System.out.println("Payment Successful");
                    } else if (paid > p.price) {
                        System.out.println("UPI expects exact amount. Payment Failed.");
                    } else {
                        System.out.println("Insufficient Amount. Payment Failed.");
                    }
                    break;
                case 2:
                    paymentMethod = "Cash";
                    System.out.print("Insert Cash Amount : ");
                    try {
                        paid = sc.nextDouble();
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid amount! Transaction cancelled.");
                        sc.nextLine();
                        continue;
                    }
                    if (paid >= p.price) {
                        success = true;
                        change = paid - p.price;
                        System.out.println("Processing Payment...");
                        System.out.println("Payment Successful");
                        if (change > 0) {
                            System.out.println("Dispensing Change : ₹" + change);
                        }
                    } else {
                        System.out.println("Insufficient Amount. Payment Failed.");
                    }
                    break;
                case 3:
                    paymentMethod = "Card";
                    System.out.println("Processing Card Payment of ₹" + p.price + "...");
                    success = true;
                    paid = p.price;
                    System.out.println("Payment Successful");
                    break;
                default:
                    System.out.println("Invalid Choice. Transaction cancelled.");
            }

            if (success) {
                p.stock--;
                totalSales += p.price;
                System.out.println("\nDispensing " + p.name + "...");
                System.out.println("\n---------------------------------------------");
                System.out.println("RECEIPT");
                System.out.println("---------------------------------------------");
                System.out.println("Product        : " + p.name);
                System.out.println("Price          : ₹" + p.price);
                System.out.println("Payment Method : " + paymentMethod);
                System.out.println("Amount Paid    : ₹" + paid);
                if (paymentMethod.equals("Cash") && change > 0) {
                    System.out.println("Change Given   : ₹" + change);
                }
                System.out.println("Status         : Success");
                System.out.println("Remaining Stock: " + p.stock);
                System.out.println("---------------------------------------------");
            }
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

    private void adminMode(Scanner sc) {
        System.out.println("\n--- ADMIN MODE ---");
        while (true) {
            System.out.println("\n1. View Total Sales");
            System.out.println("2. Restock Product");
            System.out.println("0. Exit Admin Mode");
            System.out.print("Enter choice: ");
            
            int choice = -1;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input!");
                sc.nextLine();
                continue;
            }

            if (choice == 1) {
                System.out.println("Total Sales: ₹" + totalSales);
            } else if (choice == 2) {
                displayProducts();
                System.out.print("Enter Product ID to restock: ");
                try {
                    int pId = sc.nextInt();
                    if (pId >= 1 && pId <= 5) {
                        System.out.print("Enter amount to add to stock: ");
                        int amount = sc.nextInt();
                        if (amount > 0) {
                            products[pId - 1].stock += amount;
                            System.out.println("Restocked successfully!");
                        } else {
                            System.out.println("Amount must be greater than 0.");
                        }
                    } else {
                        System.out.println("Invalid Product ID.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input!");
                    sc.nextLine();
                }
            } else if (choice == 0) {
                break;
            } else {
                System.out.println("Invalid Choice.");
            }
        }
    }

    public static void main(String[] args) {
        VendingMachineSimulator vm = new VendingMachineSimulator();
        vm.start();
    }
}
