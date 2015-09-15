package eu.motogymkhana.competition.rider;

import java.util.Collection;

import eu.motogymkhana.competition.model.Rider;

/**
 * Created by christine on 13-5-15.
 */
public interface GetRidersCallback {

    public void onSuccess(Collection<Rider> riders);

    public void onError(String error);
}
