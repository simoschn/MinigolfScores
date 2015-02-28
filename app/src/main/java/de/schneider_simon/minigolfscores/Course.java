package de.schneider_simon.minigolfscores;

/**
 * Created by root on 28.02.15.
 */

public class Course {

    private static final String NOT_SET = "-";

    private String club;
    private String system;
    private String street;
    private String streetNumber;
    private String zipcode;
    private String city;

    public Course(){
        this.club = NOT_SET;
        this.system = NOT_SET;
        this.street = NOT_SET;
        this.streetNumber = NOT_SET;
        this.zipcode = NOT_SET;
        this.city = NOT_SET;
    }

    public String getClub(){
        return this.club;
    }
    public void setClub(String club){
        this.club = club;
    }

    public String getSystem(){
        return this.system;
    }
    public void setSystem(String system){
        this.system = system;
    }

    public String getStreet(){
        return this.street;
    }
    public void setStreet(String street){
        this.street = street;
    }

    public String getStreetNumber(){
        return this.streetNumber;
    }
    public void setStreetNumber(String streetNumber){
        this.streetNumber = streetNumber;
    }

    public String getZipcode(){
        return this.zipcode;
    }
    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }

    public String getCity(){
        return this.city;
    }
    public void setCity(String city){
        this.city = city;
    }
}
