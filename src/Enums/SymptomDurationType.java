package Enums;

public enum SymptomDurationType {
    MINUTES(1), HOURS(2), DAYS(3), WEEKS(4), MONTHS(5), YEARS(6);

    private int numVal;

    SymptomDurationType(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}