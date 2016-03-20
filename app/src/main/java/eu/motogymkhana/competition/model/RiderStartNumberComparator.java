package eu.motogymkhana.competition.model;

import android.util.Log;

import java.util.Comparator;

/**
 * Created by christine on 19-5-15.
 */
public class RiderStartNumberComparator implements Comparator<Rider> {

    private static final String LOGTAG = RiderStartNumberComparator.class.getSimpleName();

    @Override
    public int compare(Rider rider1, Rider rider2) {

        if(rider1 == null || rider2 == null){
            return 0;
        }

        return rider1.getStartNumber() - rider2.getStartNumber();
    }
}
