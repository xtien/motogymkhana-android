package eu.motogymkhana.competition.model;

import java.util.Comparator;

/**
 * Created by christine on 19-5-15.
 */
public class RiderStartNumberComparator implements Comparator<Rider> {

    private static final String LOGTAG = RiderStartNumberComparator.class.getSimpleName();

    @Override
    public int compare(Rider rider1, Rider rider2) {

        if (rider1 == null || rider2 == null) {
            return 0;
        }
        if (rider1.getStartNumber() == 0 && rider2.getStartNumber() == 0) {
            return rider1.getRiderNumber() - rider2.getRiderNumber();
        } else {
            return rider1.getStartNumber() - rider2.getStartNumber();
        }
    }
}
