import java.util.Scanner;

// ======================= INTERFACES =======================

interface Food {
    String getDescription();
    double getCost();
    double totalPrice(double quantity);
}

interface PaymentStrategy {
    void pay(double amount);
    String getPaymentType();
}

// ======================= CONCRETE FOOD CLASSES =======================

class PizPrice implements Food {
    public String getDescription() {
        return "Pizza";
    }

    public double getCost() {
        return 4.25;
    }

    public double totalPrice(double quantity) {
        return quantity * getCost();
    }
}

class BurPrice implements Food {
    public String getDescription() {
        return "Burger";
    }

    public double getCost() {
        return 10.00;
    }

    public double totalPrice(double quantity) {
        return quantity * getCost();
    }
}

class SaldPrice implements Food {
    public String getDescription() {
        return "Salad";
    }

    public double getCost() {
        return 5.00;
    }

    public double totalPrice(double quantity) {
        return quantity * getCost();
    }
}

// ======================= DECORATORS =======================

abstract class FoodAdd implements Food {
    protected Food food;

    public FoodAdd(Food food) {
        this.food = food;
    }
}

class ExtraCheese extends FoodAdd {
    public ExtraCheese(Food food) {
        super(food);
    }

    public String getDescription() {
        return food.getDescription() + " + Extra Cheese";
    }

    public double getCost() {
        return food.getCost() + 1.00;
    }

    public double totalPrice(double quantity) {
        return quantity * getCost();
    }
}

class Sauce extends FoodAdd {
    public Sauce(Food food) {
        super(food);
    }

    public String getDescription() {
        return food.getDescription() + " + Sauce";
    }

    public double getCost() {
        return food.getCost() + 0.50;
    }

    public double totalPrice(double quantity) {
        return quantity * getCost();
    }
}

// ======================= PAYMENT STRATEGY =======================

class CreditCardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card.");
    }

    public String getPaymentType() {
        return "Credit Card";
    }
}

class PayPalPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal.");
    }

    public String getPaymentType() {
        return "PayPal";
    }
}

// ======================= TEMPLATE METHOD =======================

abstract class OrderTemplate {
    public final void processOrder() {
        selectItem();
        customizeMeal();
        pay();
        confirmOrder();
    }

    protected abstract void selectItem();
    protected abstract void customizeMeal();
    protected abstract void pay();

    protected void confirmOrder() {
        System.out.println("Order confirmed. Thank you!");
    }
}

class CustomerOrder extends OrderTemplate {
    private Food meal;
    private PaymentStrategy payment;
    private double quantity;

    public CustomerOrder(Food meal, PaymentStrategy payment, double quantity) {
        this.meal = meal;
        this.payment = payment;
        this.quantity = quantity;
    }

    protected void selectItem() {
        System.out.println("Item selected: " + meal.getDescription());
    }

    protected void customizeMeal() {
        System.out.println("Final customizations applied.");
    }

    protected void pay() {
        double total = meal.totalPrice(quantity);
        System.out.println("Processing payment...");
        payment.pay(total);
    }

    @Override
    protected void confirmOrder() {
        double total = meal.totalPrice(quantity);
        System.out.println("Order confirmed. Total paid: $" + total);
        System.out.println("Thank you!");
    }
}

// ======================= MAIN =======================

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Food selectedFood = null;
        int option;

        System.out.println("Welcome to the Online Food Ordering System");
        System.out.println("Select your meal:");
        System.out.println("\t1 - Pizza");
        System.out.println("\t2 - Burger");
        System.out.println("\t3 - Salad");
        System.out.print("Enter your option: ");
        option = sc.nextInt();

        switch (option) {
            case 1:
                selectedFood = new PizPrice();
                break;
            case 2:
                selectedFood = new BurPrice();
                break;
            case 3:
                selectedFood = new SaldPrice();
                break;
            default:
                System.out.println("Invalid option. Exiting.");
                sc.close();
                return;
        }

        // Customizations
        System.out.print("Add Extra Cheese? (yes/no): ");
        String cheese = sc.next();
        if (cheese.equalsIgnoreCase("yes")) {
            selectedFood = new ExtraCheese(selectedFood);
        }

        // Quantity
        System.out.print("Enter quantity: ");
        double qty = sc.nextDouble();
        double totalPrice = selectedFood.totalPrice(qty);

        // Payment Method
        System.out.println("Choose Payment Method:");
        System.out.println("\t1 - Credit Card");
        System.out.println("\t2 - PayPal");
        System.out.print("Enter option: ");
        int payOpt = sc.nextInt();

        PaymentStrategy paymentStrategy;
        if (payOpt == 1) {
            paymentStrategy = new CreditCardPayment();
        } else if (payOpt == 2) {
            paymentStrategy = new PayPalPayment();
        } else {
            System.out.println("Invalid payment method. Exiting.");
            sc.close();
            return;
        }

        // Receipt
        System.out.println("\n------ RECEIPT ------");
        System.out.println("Item: " + selectedFood.getDescription());
        System.out.println("Quantity: " + qty);
        System.out.println("Unit Price: $" + selectedFood.getCost());
        System.out.println("Total Price: $" + totalPrice);
        System.out.println("Payment Method: " + paymentStrategy.getPaymentType());

        // Process Order
        CustomerOrder order = new CustomerOrder(selectedFood, paymentStrategy, qty);
        order.processOrder();

        sc.close();
    }
}
