package ba.unsa.etf.rs;

public class Bus {
    private String maker, series;
    private int SeatNumber, id;
    Driver driverOne, driverTwo;

    public Bus () {}
    public Bus(String maker, String series, int SeatNumber) {
        this.maker = maker;
        this.series = series;
        this.SeatNumber = SeatNumber;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.SeatNumber = seatNumber;
    }

    public Driver getDriverOne() {
        return driverOne;
    }

    public void setDriverOne(Driver driverOne) {
        this.driverOne = driverOne;
    }

    public Driver getDriverTwo() {
        return driverTwo;
    }

    public void setDriverTwo(Driver driverTwo) {
        this.driverTwo = driverTwo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return maker + " " + series + " ( seats: " + SeatNumber +" )"
                     + ( (driverOne!=null) ? " - (" + driverOne + ")" : "" ) +" "
                     +( (driverTwo!=null) ? " - (" + driverTwo + ")" : "");
    }
}
