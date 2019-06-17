package pro.novatech.solutions.kelasiapi.v1.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/10/2017.
 */

public class FeeResponse implements ApiResponse, Serializable {

    private int student_purse_id;
    private int enrolment_id;
    private double amount_paid_up;
    private double outstanding_balance;
    public double total_amount_to_pay;
    private JSONArray fees;
    //private String message;








    private FeeResponse(){}

    public FeeResponse(JSONObject response){



        for (Field f : FeeResponse.class.getDeclaredFields()){
            if("fees".equals(f.getName()))
                continue;

            try{
                if(f.getType() == String.class){
                    f.set(this, response.optString(f.getName()));
                }
                if(f.getType() == int.class){
                    f.set(this, response.optInt(f.getName()));
                }
                if(f.getType() == double.class){
                    f.set(this, response.optDouble(f.getName()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public FeeResponse(JSONArray fees){
        this.fees = fees;
    }

    public List<FeeResponse> getFeeList(){
        List<FeeResponse>  feeList  = new ArrayList<FeeResponse>();


        for(int i =0; i < fees.length(); i++){
            JSONObject response = null;
            try {
                response = fees.getJSONObject(i);
                feeList.add( new FeeResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return feeList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : FeeResponse.class.getDeclaredFields()){
            try {
                object.put(f.getName(), f.get(this));

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

    @Override
    public String ToJSONArrayString() {
        return fees.toString();
    }


    public int getStudent_purse_id() {
        return student_purse_id;
    }

    public void setStudent_purse_id(int student_purse_id) {
        this.student_purse_id = student_purse_id;
    }

    public int getEnrolment_id() {
        return enrolment_id;
    }

    public void setEnrolment_id(int enrolment_id) {
        this.enrolment_id = enrolment_id;
    }

    public double getAmount_paid_up() {
        return amount_paid_up;
    }

    public void setAmount_paid_up(double amount_paid_up) {
        this.amount_paid_up = amount_paid_up;
    }

    public double getOutstanding_balance() {
        return outstanding_balance;
    }

    public void setOutstanding_balance(double outstanding_balance) {
        this.outstanding_balance = outstanding_balance;
    }

    public double getTotal_amount_to_pay() {
        return total_amount_to_pay;
    }

    public void setTotal_amount_to_pay(double total_amount_to_pay) {
        this.total_amount_to_pay = total_amount_to_pay;
    }
}
