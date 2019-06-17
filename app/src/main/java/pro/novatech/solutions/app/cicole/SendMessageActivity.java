package pro.novatech.solutions.app.cicole;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
public class SendMessageActivity extends AppCompatActivity {
    private Spinner receiver ;
    private Map<String,Integer> correspondent_map = new HashMap<>();
    private EditText text_message,subject;
    private LinearLayout area;
    private ImageView submit_send;
    private   MyPreferenceManager myPreferenceManager;
    private View  inner_message;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            myPreferenceManager =
                    new MyPreferenceManager(this);
            final List<UserAccessResponse> correspondents = myPreferenceManager.getCorrespondents();
            List<String> correspondentsList = new ArrayList<>();

            receiver = (Spinner) findViewById(R.id.receiver);
            text_message = (EditText) findViewById(R.id.text_message);
            area = (LinearLayout) findViewById(R.id.area);
            submit_send = (ImageView) findViewById(R.id.submit_send);
            subject = (EditText) findViewById(R.id.subject);

            for(UserAccessResponse correspondent : correspondents){
                String name = correspondent.getName()+" "+correspondent.getSurname();
                correspondentsList.add(name);
                correspondent_map.put(name, correspondent.getId());
            }

            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    correspondentsList.toArray( new String[]{}));

            receiver.setAdapter(spinnerArrayAdapter);

            inner_message = LayoutInflater.from(this).inflate(R.layout.inner_message_2,null);
            textView   =   (TextView) inner_message.findViewById(R.id.textView);

            submit_send.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    submit_send(v);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
        receiver = (Spinner) findViewById(R.id.receiver);

    }


    public void submit_send(View v) {
        if(subject.getText().length() == 0){
            subject.setError(getString(R.string.err_subj));
        } else if(text_message.getText().length() == 0){
            text_message.setError(getString(R.string.err_msg));
        } else {
            int receiver_id = correspondent_map.get(receiver.getSelectedItem().toString()).intValue();
            Map params = new HashMap();
            params.put("message", text_message.getText().toString());
            params.put("to", receiver_id);
            params.put("subject", subject.getText().toString());

            String message = text_message.getText().toString();
            textView.setText(message);



            try {
                new pro.novatech.solutions.kelasiapi.v1.Notification.Message(SendMessageActivity.this, new OnServiceResponseListener() {
                    @Override
                    public void onSuccess(Object object) {

                        if("success".equals(((MessageResponse)object).getMessage())){
                            area.addView(inner_message);
                            text_message.setText("");
                            subject.setText("");
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.capisci);
                            mediaPlayer.start();
                        } else{
                            Toast.makeText(SendMessageActivity.this,((MessageResponse)object).getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(KelasiApiException e) {
                        Toast.makeText(SendMessageActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }, true).SendMessage(myPreferenceManager.getUserSession().getAuth_key(),  params);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
