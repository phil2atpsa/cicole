package pro.novatech.solutions.app.cicole;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pro.novatech.solutions.app.cicole.helper.DateParser;
import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Notification.MessageResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;

public class TextMessageActivity extends AppCompatActivity  {
    private EditText text_message;
    private LinearLayout area;
    private TextView textView,receiver;
    private MessageResponse messageResponse;
    private  boolean sent;
  //  private TextView replyText;
   // private  View  inner_message2;
    private ImageView submit_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_message);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setTitle();

        text_message = (EditText) findViewById(R.id.text_message);

        receiver  = (EditText) findViewById(R.id.receiver);
        receiver.setEnabled(false);
        area = (LinearLayout) findViewById(R.id.area);
        View  inner_message = LayoutInflater.from(this).inflate(R.layout.inner_message_1,null);


        textView   =   (TextView) inner_message.findViewById(R.id.textView);

        submit_send = (ImageView) findViewById(R.id.submit_send);

        submit_send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isMessageTyped()){
                    MyPreferenceManager myPreferenceManager = new MyPreferenceManager(TextMessageActivity.this);
                    UserAccessResponse user = null;
                    try {
                        user = myPreferenceManager.getUserSession();
                    } catch (JSONException e) {

                    }


                    final String reply = text_message.getText().toString();
                    Map data = new HashMap();
                    data.put("reply", reply);
                    data.put("message_id", messageResponse.getMessage_id());
                    data.put("reply_to", messageResponse.getSender_id());
                    data.put("subject", messageResponse.getSubject());

                    try {
                        new pro.novatech.solutions.kelasiapi.v1.Notification.Message(TextMessageActivity.this,
                                new OnServiceResponseListener() {
                                    @Override
                                    public void onSuccess(Object object) {

                                       // if("success".equals(((MessageResponse) object).getMessage())){
                                       View inner_message2 = LayoutInflater.from(TextMessageActivity.this).inflate(R.layout.inner_message_2,null);
                                       TextView replyText =    (TextView) inner_message2.findViewById(R.id.textView);

                                            replyText.setText(reply);
                                            text_message.clearFocus();
                                            text_message.setText("");
                                            area.addView(inner_message2);
                                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.capisci);
                                            mediaPlayer.start();
                                           // setResult(RESULT_OK);

                                        //}

                                    }

                                    @Override
                                    public void onFailure(KelasiApiException e) {

                                        Toast.makeText(TextMessageActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }, true).SendReply(user.getAuth_key(), data);

                    } catch (UnsupportedEncodingException e) {

                    }


                }

            }
        });



        Bundle bundle = getIntent().getExtras();


        messageResponse =(MessageResponse) bundle.getSerializable("messageResponse");
        sent = bundle.getBoolean("sent");

            try {
               // MessageResponse response = new MessageResponse(new JSONArray(messages));

                String date = DateParser.getDate(Long.parseLong(messageResponse.getCreated_at()));
                String msg = messageResponse.getMessage();
                //  msg = msg.concat("<br />").concat("<font size='2'>"+date+"</font>");

                textView.setText(Html.fromHtml(msg));
                receiver.setText(sent ? messageResponse.getReceiver() :  messageResponse.getSender());

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(messageResponse.getSubject());
                }
                area.addView(inner_message);
                if (messageResponse.getIs_read() == 0) {
                    updateMessage(messageResponse.getMessage_id());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }
    private void updateMessage(int message_id) throws JSONException {
        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(TextMessageActivity.this);
        UserAccessResponse user = myPreferenceManager.getUserSession();


        new pro.novatech.solutions.kelasiapi.v1.Notification.Message(TextMessageActivity.this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onFailure(KelasiApiException e) {
                Toast.makeText(TextMessageActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }, false).MessageRead(user.getAuth_key(),message_id);
    }

    private boolean isMessageTyped() {
        return text_message.length() != 0;
    }


}
