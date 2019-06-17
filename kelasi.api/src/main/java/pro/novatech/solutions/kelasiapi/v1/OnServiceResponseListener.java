package pro.novatech.solutions.kelasiapi.v1;

/**
 * Created by p.lukengu on 4/1/2017.
 */

public interface OnServiceResponseListener<T>  {

     void onSuccess(T object);
     void onFailure(KelasiApiException e);
}
