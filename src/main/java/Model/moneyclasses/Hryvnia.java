package Model.moneyclasses;

import Model.Money;
import Model.MoneyFactory;
import Model.Quality;

public class Hryvnia implements Money {

    Quality quality;
    double denomination;

    private Hryvnia(double denomination, Quality quality) {
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

    public static MoneyFactory moneyFactory = Hryvnia::new;
}
