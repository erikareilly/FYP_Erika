package com.example.fyp;

public class PatientInfo {
    String weight;
    String age;
    String allergies;
    String medicalNote;
    boolean gender;

    public PatientInfo(){

    }

    public PatientInfo(String weight, String age, String allergies, String medicalNote, boolean gender) {
        this.weight = weight;
        this.age = age;
        this.allergies = allergies;
        this.medicalNote = medicalNote;
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalNote() {
        return medicalNote;
    }

    public void setMedicalNote(String medicalNote) {
        this.medicalNote = medicalNote;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
