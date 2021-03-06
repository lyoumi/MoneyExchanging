package Model.moneyclasses;

import Model.Money;
import Model.MoneyFactory;
import Model.Quality;

public class Euro implements Money {

    double denomination;
    Quality quality;

    private Euro(double denomination, Quality quality) {
        this.denomination = denomination;
        this.quality = quality;
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

    public static MoneyFactory moneyFactory = Euro::new;
}
