import java.util.ArrayList;
import java.util.List;

abstract class Item {
    private String title;
    private String uniqueID;
    private boolean isBorrowed;

    public Item(String title, String uniqueID) {
        this.title = title;
        this.uniqueID = uniqueID;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public abstract void borrowItem();

    public abstract void returnItem();
}

class Book extends Item {
    private String author;

    public Book(String title, String uniqueID, String author) {
        super(title, uniqueID);
        this.author = author;
    }

    @Override
    public void borrowItem() {
        if (!isBorrowed()) {
            setBorrowed(true);
        }
    }

    @Override
    public void returnItem() {
        if (isBorrowed()) {
            setBorrowed(false);
        }
    }
}

class DVD extends Item {
    private int duration;

    public DVD(String title, String uniqueID, int duration) {
        super(title, uniqueID);
        this.duration = duration;
    }

    @Override
    public void borrowItem() {
        if (!isBorrowed()) {
            setBorrowed(true);
        }
    }

    @Override
    public void returnItem() {
        if (isBorrowed()) {
            setBorrowed(false);
        }
    }
}

class Patron {
    private String name;
    private String ID;
    List<Item> borrowedItems;

    public Patron(String name, String ID) {
        this.name = name;
        this.ID = ID;
        this.borrowedItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public void borrow(Item item) {
        if (!borrowedItems.contains(item)) {
            borrowedItems.add(item);
        }
    }

    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }
}

interface IManageable {
    void add(Item item);

    void remove(Item item);

    List<Item> listAvailable();

    List<Item> listBorrowed();
}

class Library implements IManageable {
    private List<Item> items;
    private List<Patron> patrons;

    public Library() {
        this.items = new ArrayList<>();
        this.patrons = new ArrayList<>();
    }

    public void registerPatron(Patron patron) {
        patrons.add(patron);
    }

    public void lendItem(Patron patron, Item item) {
        if (items.contains(item) && !item.isBorrowed()) {
            patron.borrow(item);
            item.borrowItem();
        }
    }

    public void returnItem(Patron patron, Item item) {
        if (patron.borrowedItems.contains(item) && item.isBorrowed()) {
            patron.returnItem(item);
            item.returnItem();
        }
    }

    @Override
    public void add(Item item) {
        items.add(item);
    }

    @Override
    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public List<Item> listAvailable() {
        List<Item> availableItems = new ArrayList<>();
        for (Item item : items) {
            if (!item.isBorrowed()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    @Override
    public List<Item> listBorrowed() {
        List<Item> borrowedItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isBorrowed()) {
                borrowedItems.add(item);
            }
        }
        return borrowedItems;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create a library
        Library library = new Library();

        // Create some items (books and DVDs)
        Item book1 = new Book("Book 1", "B001", "Author 1");
        Item book2 = new Book("Book 2", "B002", "Author 2");
        Item dvd1 = new DVD("DVD 1", "D001", 120);
        Item dvd2 = new DVD("DVD 2", "D002", 90);

        // Add items to the library
        library.add(book1);
        library.add(book2);
        library.add(dvd1);
        library.add(dvd2);

        // Create patrons
        Patron patron1 = new Patron("Patron 1", "P001");
        Patron patron2 = new Patron("Patron 2", "P002");

        // Register patrons with the library
        library.registerPatron(patron1);
        library.registerPatron(patron2);

        // Test borrowing and returning items
        library.lendItem(patron1, book1);
        library.lendItem(patron2, dvd1);

        System.out.println("Available items:");
        for (Item item : library.listAvailable()) {
            System.out.println(item.getTitle());
        }

        System.out.println("Borrowed items:");
        for (Item item : library.listBorrowed()) {
            System.out.println(item.getTitle());
        }

        library.returnItem(patron1, book1);
        library.returnItem(patron2, dvd1);

        System.out.println("Available items after returning:");
        for (Item item : library.listAvailable()) {
            System.out.println(item.getTitle());
        }

        System.out.println("Borrowed items after returning:");
        for (Item item : library.listBorrowed()) {
            System.out.println(item.getTitle());
        }
    }
}

