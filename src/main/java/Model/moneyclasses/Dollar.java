package Model.moneyclasses;

import Model.Money;
import Model.MoneyFactory;
import Model.Quality;

public class Dollar implements Money {

    private Quality quality;
    private double denomination;

    private Dollar(double denomination, Quality quality) {
        this.quality = quality;
        this.denomination = denomination;
    }

    @Override
    public Quality getQuality() {
        return quality;
    }

    @Override
    public double getDenomination() {
        return denomination;
    }

    @Override
    public boolean canExchanging() {
        return quality != Quality.Damagerd;
    }

    @Override
    public double exchange(double course) {
        return course * denomination;
    }

    public String toString(){
        return this.getClass().getSimpleName() + ": " + getDenomination() + "; quality: " + getQuality();
    }

    public static MoneyFactory moneyFactory = Dollar::new;
}
