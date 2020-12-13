package io2.objectStream;

public class Manager extends Employee {
    private Employee secretary;

    public Manager(String n, double s, int year, int month, int day){
        super(n , s, year, month, day);
        secretary = null;
    }

    public void setSecretary(Employee e){
        secretary =e;
    }

    public String toString(){
        return super.toString() + "[secretary = " + secretary + "]";
    }
}
