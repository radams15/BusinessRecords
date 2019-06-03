import com.google.gson.*;

import java.util.ArrayList;

import java.sql.*;

public class Records {

    String dbLocation = "database.db";
    Connection connection;

    public Records(){
        connection = null;

        try{
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbLocation);
        }catch (SQLException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    void close(){
        try {
            connection.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ResultSet read(String sql){
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    void addCustomer(Customer customer){
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO customers VALUES (?,?,?,?)");
            statement.setNull(1,0);
            statement.setString(2, customer.forename);
            statement.setString(3, customer.surname);
            statement.setString(4, customer.address);
            statement.execute();
            connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                connection.setAutoCommit(true);
            }catch(SQLException e){e.printStackTrace();}
        }
    }

    void addJob(Job job){
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO jobs VALUES (?,?,?,?,?,?)");
            if(job.id != null){
                statement.setInt(1, job.id);
            }else{
                statement.setNull(1,0);
            }
            statement.setString(2, job.description);
            statement.setDouble(3, job.cost);
            statement.setDouble(4, job.time);
            statement.setInt(5, job.customer.id);
            statement.setString(6, job.date);
            statement.execute();
            connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try {
                connection.setAutoCommit(true);
            }catch(SQLException e){e.printStackTrace();}
        }
    }

    void deleteJob(Job job){
        System.out.println("Deleting "+job);
        run("DELETE FROM jobs WHERE id IS "+job.id);
    }

    void deleteCustomer(Customer customer){
        System.out.println("Deleting "+customer);
        run("DELETE FROM customers WHERE id IS "+customer.id);
    }

    Customer getCustomer(int id){
        ResultSet customerInfo = read("SELECT * FROM customers WHERE ID IS "+id);

        try{if(customerInfo.next()) {
            return new Customer(customerInfo.getInt("id"),
                    customerInfo.getString("forename"),
                    customerInfo.getString("surname"),
                    customerInfo.getString("address")
            );
        }}catch(SQLException e){e.printStackTrace();}
        return null;
    }

    void run(String sql){
        try{
            Statement statement = connection.createStatement();
            statement.execute(sql);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    Job getJob(int id){
        ResultSet customerInfo = read("SELECT * FROM jobs WHERE ID IS "+id);

        try{if(customerInfo.next()) {
            return new Job(
                    customerInfo.getInt("id"),
                    getCustomer((int)customerInfo.getObject("customer_id")),
                    customerInfo.getString("description"),
                    customerInfo.getDouble("cost"),
                    customerInfo.getDouble("time"),
                    customerInfo.getString("date")
            );
        }}catch(SQLException e){e.printStackTrace();}
        return null;
    }

    ArrayList<Job> getJobs(){
        ResultSet jobInfo = read("SELECT * FROM jobs");

        return getJobs(jobInfo);
    }

    ArrayList<Job> getJobs(int customerId){
        ResultSet jobInfo = read("SELECT * FROM jobs WHERE customer_id IS "+customerId);

        return getJobs(jobInfo);
    }

    private ArrayList<Job> getJobs(ResultSet jobInfo){
        ArrayList<Job> out = new ArrayList<>();
        try{while(jobInfo.next()) {
            out.add(new Job(
                    jobInfo.getInt("id"),
                    getCustomer(jobInfo.getInt("customer_id")),
                    jobInfo.getString("description"),
                    jobInfo.getDouble("cost"),
                    jobInfo.getDouble("time"),
                    jobInfo.getString("date")
            ));
        }}catch(SQLException e){e.printStackTrace();}
        return out;
    }

    ArrayList<Customer> getCustomers(){
        ArrayList<Customer> out = new ArrayList<>();
        ResultSet customerInfo = read("SELECT * FROM customers");
        try{while(customerInfo.next()){
            out.add(new Customer(customerInfo.getInt("id"),
                    customerInfo.getString("forename"),
                    customerInfo.getString("surname"),
                    customerInfo.getString("address")
            ));
        }
        }catch(SQLException e){e.printStackTrace();}
        return out;
    }

    int getJobAmount(int customerId){
        ResultSet jobAmount = read("SELECT COUNT(id) FROM jobs WHERE customer_id IS  "+customerId);
        try{if(jobAmount.next()){
            return jobAmount.getInt("COUNT(id)");
        }
        }catch(SQLException e){e.printStackTrace();}
        return 0;
    }

    double getMoneyMade(){
        return getMoneyMade(read("SELECT sum(cost) FROM jobs"));

    }

    private String formatJson(String jsonString){
        JsonParser parser = new JsonParser();
        JsonArray json = parser.parse(jsonString).getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    String toJson(){
        String out = "";
        Gson gson = new Gson();

        out+="[";
        for(Customer customer : getCustomers()){
            String json = gson.toJson(customer);
            out+=json.substring(0, json.length()-1);
            out+=", \"Jobs\":";

            ArrayList<Job> jobs = getJobs(customer.id);
            if(!jobs.isEmpty()){
                out += "[";
                for(Job job : jobs){
                    out+=gson.toJson(job)+",";
                }
                out = out.substring(0, out.length()-1);
                out += "]";
            }else{
                out+="null";
            }
            out+="}";
            out+=",";
        }
        out = out.substring(0, out.length()-1);
        out+="]";

        return formatJson(out);
    }

    double getMoneyMade(int customerId){
        return getMoneyMade(read("SELECT sum(cost) FROM jobs WHERE customer_id IS "+customerId));
    }

    private double getMoneyMade(ResultSet moneyMadeResults){
        try{
            return moneyMadeResults.getDouble("sum(cost)");
        }catch(SQLException e){ return 0;}
    }

}
