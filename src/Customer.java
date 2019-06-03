public class Customer {

    int id;
    String forename;
    String surname;
    String address;

    Customer(int id, String forename, String surname, String address){
        this.id = id;
        this.forename = forename;
        this.surname = surname;
        this.address = address;
    }

    Customer(String forename, String surname, String address){
        this(0, forename, surname, address);
    }

    public String toString(){
        return forename + " " + surname;
    }
}
