package pro.novatech.solutions.app.cicole.fragment.students;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.FeeResponse;
import pro.novatech.solutions.kelasiapi.v1.Student.Student;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;

/**
 * Created by p.lukengu on 4/8/2017.
 */

public class Fees extends Fragment {
    TextView full_amount;
    TextView paid_so_far;
    TextView balance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v  =  inflater.inflate(R.layout.fees, container, false);

        full_amount =   v.findViewById(R.id.full_amount);

        paid_so_far =  v.findViewById(R.id.paid_so_far);
        balance =  v.findViewById(R.id.balance);




        Bundle bundle = getArguments();
        StudentResponse studentResponse = (StudentResponse) bundle.getSerializable("student_response");

       // Toast.makeText(getActivity(), studentResponse.ToJSONObjectString(), Toast.LENGTH_LONG).show();


        new Student(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {

                FeeResponse feeResponse =  (FeeResponse) object;

                Log.e("fr", Double.toString(feeResponse.getAmount_paid_up()));

                //Toast.makeText(getActivity(), "ICI "+ feeResponse.ToJSONObjectString(), Toast.LENGTH_LONG).show();
               // System.out.println("ICI "+ feeResponse.ToJSONObjectString());

                double _full_amount = feeResponse.getTotal_amount_to_pay();


                full_amount.setText(Double.toString(_full_amount));
                paid_so_far.setText(Double.toString(feeResponse.getAmount_paid_up()));
                balance.setText(Double.toString(feeResponse.getOutstanding_balance()));

            }

            @Override
            public void onFailure(KelasiApiException e) {
                Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }, true).Fees(studentResponse.getStudent_id());



        return v;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return false;
    }


}


