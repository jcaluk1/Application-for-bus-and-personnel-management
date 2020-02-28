package ba.unsa.etf.rs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class TransportDAO {

    private static TransportDAO instance = null;
    private Connection conn;
    private PreparedStatement addDriverStatement;
    private PreparedStatement addBusStatement;
    private PreparedStatement getDriversStatement;
    private PreparedStatement getBusesStatement;

    private PreparedStatement getDriverByIdStatement;

    private PreparedStatement deleteDriverStatement;
    private PreparedStatement deleteBusStatement;

    private PreparedStatement nextBusIdStatement;
    private PreparedStatement nextDriverIdStatement;

    private PreparedStatement upadateBusDriverStatements;


    private TransportDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/user/IdeaProjects/Tutorijal8_Z1_RS/src/ba/unsa/etf/rs/baza.db");

            InitializePreparedStatements();
        } catch (Exception e) {
            createTable();
            try {
                InitializePreparedStatements();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static TransportDAO getInstance() {
        if (instance == null) instance = new TransportDAO();
        return instance;
    }

    private void InitializePreparedStatements () throws SQLException {
        addDriverStatement = conn.prepareStatement("insert into drivers values (?,?,?,?,?,?)");
        addBusStatement = conn.prepareStatement("insert into busses values (?,?,?,?,?,?)");
        getDriversStatement = conn.prepareStatement("select* from drivers");
        getBusesStatement = conn.prepareStatement("select * from busses");

        getDriverByIdStatement = conn.prepareStatement("select * from drivers where id=?");

        deleteDriverStatement = conn.prepareStatement("delete from drivers where ucmn=?");
        deleteBusStatement = conn.prepareStatement("delete from busses where id=?");
        

        nextBusIdStatement = conn.prepareStatement("select max(id)+1 from busses");
        nextDriverIdStatement = conn.prepareStatement("select max(id)+1 from drivers");

        upadateBusDriverStatements = conn.prepareStatement("UPDATE busses set driverOne=?, driverTwo=? where id=?");
    }


    ArrayList<Driver> getDrivers () {
        ArrayList<Driver> drivers = new ArrayList<>();
        try {
            ResultSet rs = getDriversStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String surname = rs.getString(3);
                String ucmn = rs.getString(4);
                LocalDate hireDate = rs.getDate(5).toLocalDate();
                LocalDate realseDate = rs.getDate(6).toLocalDate();

                Driver driver = new Driver(name,surname,ucmn,hireDate,realseDate);
                driver.setId(id);
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    void addDriver (Driver driver) {
        ArrayList<Driver> drivers = getDrivers();
        for (Driver d : drivers)
            if (d.equals(driver))
                throw new IllegalArgumentException("Taj vozač već postoji!");
        int id = 1;
        try {
            ResultSet rs = nextDriverIdStatement.executeQuery();
            id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            addDriverStatement.setInt(1,id);
            addDriverStatement.setString(2,driver.getName());
            addDriverStatement.setString(3,driver.getSurname());
            addDriverStatement.setString(4,driver.getUMCN());
            addDriverStatement.setDate(5, Date.valueOf(driver.getHireDate()));
            addDriverStatement.setDate(6,Date.valueOf(driver.getRealseDate()));
            addDriverStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Driver getDriverByIdStatement (int id) {
        Driver driver = null;
        if (id == -1) return driver;
        driver = new Driver();
        try {
            getDriverByIdStatement.setInt(1,id);
            ResultSet rs = getDriverByIdStatement.executeQuery();
            while (rs.next()) {
                driver.setId(id);
                driver.setName(rs.getString("name"));
                driver.setSurname(rs.getString("surname"));
                driver.setUMCN(rs.getString("ucmn"));
                driver.setHireDate(rs.getDate("hireDate").toLocalDate());
                driver.setRealseDate(rs.getDate("releaseDate").toLocalDate());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public ArrayList<Bus> getBusses () {
        ArrayList<Bus> busevi = new ArrayList<>();
        try {
            ResultSet rs = getBusesStatement.executeQuery();
            while (rs.next()) {
                Bus bus = new Bus();
                bus.setId(rs.getInt(1));
                bus.setMaker(rs.getString(2));
                bus.setSeries(rs.getString(3));
                bus.setSeatNumber(rs.getInt(4));

                int idDriverOne, idDriverTwo;
                idDriverOne = rs.getInt(5);
                idDriverTwo = rs.getInt(6);

                Driver driverOne, driverTwo;
                bus.setDriverOne(getDriverByIdStatement(idDriverOne));
                bus.setDriverTwo(getDriverByIdStatement(idDriverTwo));

                busevi.add(bus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return busevi;
    }

    public void deleteDriver(Driver driver) {
        try {
            deleteDriverStatement.setString(1,driver.getUMCN());
            int broj = deleteDriverStatement.executeUpdate();
            System.out.println("Broj je : "+ broj);
            Statement statement = conn.createStatement();
            statement.execute("update busses set driverOne=-1 where driverOne=" + driver.getId());
            statement.execute("update busses set driverTwo=-1 where driverTwo=" + driver.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void addBus (Bus bus) {
        int id = 1;
        try {
            ResultSet rs = nextBusIdStatement.executeQuery();
            id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            addBusStatement.setInt(1,id);
            addBusStatement.setString(2,bus.getMaker());
            addBusStatement.setString(3,bus.getSeries());
            addBusStatement.setInt(4,bus.getSeatNumber());

            int idDriverOne=-1, idDriverTwo=-1;
            if (bus.getDriverOne() != null)
                idDriverOne = bus.getDriverOne().getId();
            if (bus.getDriverTwo() != null)
                idDriverTwo = bus.getDriverTwo().getId();
            addBusStatement.setInt(5,idDriverOne);
            addBusStatement.setInt(6,idDriverTwo);

            addBusStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteBus (Bus bus) {
        try {
            deleteBusStatement.setInt(1,bus.getId());
            deleteBusStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodijeliVozacuAutobus(Driver driver,Bus bus, int kojiVozac) {
        int driverOneId = -1, driverTwoId = -1;
        if (bus.getDriverOne() != null) driverOneId = bus.getDriverOne().getId();
        if (bus.getDriverTwo() != null) driverTwoId = bus.getDriverTwo().getId();
        if (kojiVozac == 1)
            driverOneId = driver.getId();
        else
            driverTwoId = driver.getId();
        try {
            upadateBusDriverStatements.setInt(1,driverOneId);
            upadateBusDriverStatements.setInt(2,driverTwoId);
            upadateBusDriverStatements.setInt(3,bus.getId());
            upadateBusDriverStatements.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }





    String[] getSqlStatementsFromFile () throws FileNotFoundException {
        String sql_string = "";
        URL file = getClass().getResource("/dbSetup.sql");
        Scanner tok = new Scanner(new FileReader(file.getFile()));
        while (tok.hasNextLine()) {
            sql_string += tok.nextLine();
        }
        tok.close();

        sql_string=sql_string.replace("\n","");
        sql_string=sql_string.replace(";","\n");
        return sql_string.split("\n");
    }

    private void createTable () {
        try {
            String[] sqlStatements = getSqlStatementsFromFile();
            try {
                Statement statement = conn.createStatement();
                for (String text:sqlStatements)
                    statement.execute(text);
            } catch (SQLException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void resetDatabase() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("delete from busses");
            statement.executeUpdate("delete from drivers");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

