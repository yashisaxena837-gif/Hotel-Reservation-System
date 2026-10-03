import java.sql.*;
import java.util.*;
public class Hotel_Reservation {

    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "MySQL@123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc=new Scanner(System.in);
            while (true) {
                System.out.println();
                System.out.println("hotel management");
                System.out.println("1,reserve a room");
                System.out.println("2,view reservation");
                System.out.println("3,room num");
                System.out.println("4,update reservation");
                System.out.println("5,delete reservation");
                System.out.println("0,exit");
                System.out.println("choose option");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        reserve(con, sc);
                        break;
                    case 2:
                        viewreservation(con);
                        break;
                    case 3:
                        gotroomnum(con, sc);
                        break;
                    case 4:
                        update(con, sc);
                        break;
                    case 5:
                        delete(con, sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("invalid choice");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserve(Connection con, Scanner sc) {
        try {
            System.out.println("guest name");
            String guestname = sc.next();
            System.out.println("enter num");
            int roomnum = sc.nextInt();
            System.out.println("contact num");
            String contactnumber = sc.next();

            String sql = "INSERT INTO resevation(guest_name,room_num,contact_num)" +
                    "VALUES ('" + guestname + "'," + roomnum + ",'" + contactnumber + "')";

            try (Statement stat = con.createStatement()) {

                int affected = stat.executeUpdate(sql);
                System.out.println(affected);
                if (affected > 0) {
                    System.out.println("successful");
                } else {
                    System.out.println("failed");
                }
            }
        } catch (SQLException e) {
            System.out.println("error"+e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewreservation(Connection con) throws SQLException {
        String sql = "SELECT resrvation_id,guest_name,room_num,contact_num,reservation_date from resevation";

        try (Statement stat = con.createStatement();
             ResultSet rs = stat.executeQuery(sql)) {
            while (rs.next()) {
                int reservationid = rs.getInt("resrvation_id");
                String guestname = rs.getString("guest_name");
                int roomnum = rs.getInt("room_num");
                String contactnum = rs.getString("contact_num");
                String reservationdate = rs.getTimestamp("reservation_date").toString();
                System.out.println(reservationid + guestname + roomnum + contactnum + reservationdate);
            }
        }


    }

    private static void gotroomnum(Connection con, Scanner sc) {
        try {
            System.out.println("enter id");
            int reservationid = sc.nextInt();
            System.out.println("guest name");
            String guestname = sc.next();


            String sql = "SELECT room_num FROM resevation" + "WHERE resrvation_id =" + reservationid + "AND guest_name ='" + guestname+"'";
            try(Statement stat=con.createStatement();
            ResultSet rs=stat.executeQuery(sql)){
                if(rs.next()){
                    int roomnum=rs.getInt("room_num");
                    System.out.println(roomnum);
                }else{
                    System.out.println("reservation not found");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void update(Connection con,Scanner sc){
        try{
            System.out.println("reservation id");
            int reservationid=sc.nextInt();

            if(!reservationexit(con,reservationid)){
                System.out.println("reservation not found");
                return;
            }
            System.out.println("guest name");
            String guestname=sc.next();
            System.out.println("room num");
            int roomnum=sc.nextInt();
            System.out.println("contact num");
            String contactnum=sc.next();
            String sql="UPDATE resevation SET guest_name ='"+guestname+"',"+
                    "room_num ="+roomnum+","+
                    "contact_num ='"+contactnum+"'"+
                    "WHERE resrvation_id ="+reservationid;
            try(Statement stat=con.createStatement()){
                int affected =stat.executeUpdate(sql);

            if (affected > 0) {
                System.out.println("reservation updated");
            }else{
                System.out.println("update failed");
            }
        }
    }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void delete(Connection con,Scanner sc){
        try{
            System.out.println("enter reservation id");
            int reservationid=sc.nextInt();
            if(!reservationexit(con,reservationid)){
                System.out.println("reservation not found");
                return;
            }
            String sql="DELETE FROM resevation WHERE resrvation_id ="+reservationid;
            try(Statement stat=con.createStatement()){
                int affected=stat.executeUpdate(sql);
                if(affected>0){
                    System.out.println("reservation deleted");
                }else{
                    System.out.println("deletion failed");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static boolean reservationexit(Connection con,int reservationid){
        try{
            String sql="SELECT resrvation_id FROM resevation where resrvation_id="+reservationid;
          try(Statement stat=con.createStatement();
          ResultSet rs=stat.executeQuery(sql)){
              return rs.next();
          }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static void exit() throws InterruptedException{
        System.out.println("existing system");
        int i=5;
        while(i!=0){
            System.out.println(".");
            Thread.sleep(1000);
            i--;
        }
        System.out.println();
        System.out.println("thankyou");
    }
}




