public class Job {
    Integer id;
    transient Customer customer;
    String description;
    double cost;
    double time;
    String date;

    Job(Integer id, Customer customer, String description, double cost, double time, String date){
        this.id = id;
        this.customer = customer;
        this.description = description;
        this.cost = cost;
        this.time = time;
        this.date = date;
    }

    Job(Customer customer, String description, double cost, double time, String date){
        this(null, customer, description, cost, time, date);
    }

    public String toString(){
        return "Job "+id+" For "+customer.forename+" "+customer.surname;
    }
}
