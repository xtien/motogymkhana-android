package eu.motogymkhana.competition.model;

import java.util.Comparator;

/**
 * Created by christine on 19-5-15.
 */
public class RiderNumberComparator implements Comparator<Rider> {

    @Override
    public int compare(Rider rider1, Rider rider2) {
        return rider1.getRiderNumber() - rider2.getRiderNumber();
    }
}
