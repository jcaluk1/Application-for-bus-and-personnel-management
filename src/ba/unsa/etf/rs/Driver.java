package ba.unsa.etf.rs;

import java.time.LocalDate;

public class Driver {
    private String name, surname, UMCN;
    private LocalDate hireDate, realseDate;
    int id = -1;

    public Driver () {}

    public Driver(String name, String surname, String UMCN, LocalDate hireDate, LocalDate realseDate) {
        this.name = name;
        this.surname = surname;
        this.UMCN = UMCN;
        this.hireDate = hireDate;
        this.realseDate = realseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUMCN() {
        return UMCN;
    }

    public void setUMCN(String UMCN) {
        this.UMCN = UMCN;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getRealseDate() {
        return realseDate;
    }

    public void setRealseDate(LocalDate realseDate) {
        this.realseDate = realseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;
        if (UMCN != null ? !UMCN.equals(driver.UMCN) : driver.UMCN != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (UMCN != null ? UMCN.hashCode() : 0);
        result = 31 * result + (hireDate != null ? hireDate.hashCode() : 0);
        result = 31 * result + (realseDate != null ? realseDate.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        return name + " " + surname + "(" + UMCN + ")";
    }
}
