package ba.unsa.etf.rs;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static TransportDAO dao;

    private static void dodajVozaca(Scanner tok) {
        String ime, prezime, umcn;
        LocalDate hireDate=null, relaseDate=null;

        ime = tok.nextLine();
        prezime = tok.nextLine();
        umcn = tok.nextLine();
        hireDate = LocalDate.parse(tok.nextLine(), DateTimeFormatter.ofPattern("d.M.yyyy"));
        relaseDate = LocalDate.parse(tok.nextLine(), DateTimeFormatter.ofPattern("d.M.yyyy"));

        dao.addDriver(new Driver(ime,prezime,umcn,hireDate,relaseDate));
    }

    private static void dodajAutobus(Scanner tok) {
        String maker = tok.nextLine();
        String series = tok.nextLine();
        int seats = tok.nextInt();
        tok.nextLine();

        dao.addBus(new Bus(maker,series,seats));
    }

    private static void otpustiVozaca(Scanner tok) {
        for (int i = 0; i < dao.getDrivers().size(); i++) {
            System.out.println((i+1)+". "+dao.getDrivers().get(i));
        }
        int index = tok.nextInt()-1;

        Driver driver = dao.getDrivers().get(index);
        dao.deleteDriver(driver);
    }

    public static void ukloniAutobus (Scanner tok) {
        for (int i=0; i<dao.getBusses().size(); i++)
            System.out.println((i+1)+". "+dao.getBusses().get(i));
        int index = tok.nextInt()-1;
        tok.nextLine();
        Bus bus = dao.getBusses().get(index);
        dao.deleteBus(bus);
    }

    private static void ispisiAutobuse() {
        ArrayList<Bus> busevi = dao.getBusses();
        for (Bus bus : busevi)
            System.out.println(bus);
    }

    private static void dodijeliAutobusVozacu (Scanner tok) {
        ArrayList<Driver> vozaci = dao.getDrivers();
        ArrayList<Bus> busevi = dao.getBusses();

        System.out.println("Odaberite vozaca: ");
        for (var driver : vozaci)
            System.out.println(driver);

        System.out.println("Indeks: ");
        int indexDriver = tok.nextInt();

        System.out.println("Odaberite autobus: ");
        for (var bus : busevi)
            System.out.println(bus);

        System.out.println("Indeks: ");
        int indexBus = tok.nextInt();

        Bus bus = busevi.get(indexBus-1);
        Driver driver = vozaci.get(indexDriver-1);

        int kojiVozac = 1;
        if (bus.getDriverOne() != null && bus.getDriverTwo() != null) {
            System.out.println("Umjesto kojeg vozaca zelite postaviti trenutnog(1 ili 2) : ");
            kojiVozac = tok.nextInt();
        }
        else if (bus.getDriverTwo() != null)
                kojiVozac = 2;

        dao.dodijeliVozacuAutobus(driver,bus,kojiVozac);
    }

    public static void main(String[] args) {
        // write your code here
        dao = TransportDAO.getInstance();
        Scanner tok = new Scanner(System.in);
        String result="";
        while (tok.hasNextLine()) {
            result = tok.nextLine();
            switch (result){
                case "dodaj vozaca":
                    dodajVozaca(tok);
                    break;
                case "dodaj autobus":
                    dodajAutobus(tok);
                    break;
                case "otpusti vozaca":
                    otpustiVozaca(tok);
                    break;
                case "ukloni autobus":
                    ukloniAutobus(tok);
                    break;
                case "Ispisi autobuse":
                    ispisiAutobuse();
                    break;

            }
        }
    }

}
