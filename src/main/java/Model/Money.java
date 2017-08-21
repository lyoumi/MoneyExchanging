package Model;

public interface Money {

    Quality getQuality();

    double getDenomination();

    boolean canExchanging();

    double exchange(double course);
}
