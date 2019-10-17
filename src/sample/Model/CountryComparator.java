package sample.Model;

import java.text.Collator;
import java.util.Comparator;

public class CountryComparator implements Comparator<Country>{
    private Comparator<Object> comparator;

    public CountryComparator() {
        comparator = Collator.getInstance();
    }



    public int compare(Country c1, Country c2) {
        return comparator.compare(c1.getName(), c2.getName());
    }
}
