package pro.novatech.solutions.kelasiapi.v1.Student;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.Mark.MarkResponse;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;

/**
 * Created by p.lukengu on 4/10/2017.
 */

public class Student extends AsyncHttpClient {

    private OnServiceResponseListener<Object> mCallBack;
    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;

    public Student(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
        mCallBack = callback;
        mContext = context;
        this.showProgressDialog = showProgressDialog;
        addHeader("Accept", MimeType.APPLICATION_JSON.type());
        if(this.showProgressDialog) {
            progressBar = new ProgressDialog(mContext, R.style.TransparentProgressDialog);
            //progressBar.setProgressStyle(R.style.TransparentProgressDialog);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar.setProgressDrawable(context.getDrawable(R.drawable.loading));
            } else {
                progressBar.setProgressDrawable( context.getResources().getDrawable(R.drawable.loading));
            }


            progressBar.show();
        }
    }

    public  void FromUSer(int user_id){
        RequestParams params = new RequestParams();
        params.add("user_id", String.valueOf(user_id));
        get(KelasiApiConfiguration.END_POINT_URL+"student/user", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                StudentResponse studentResponse = new StudentResponse(response);
                mCallBack.onSuccess(studentResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });



    }
    public void AbsentDays(int student_id, int option_id){
        RequestParams params = new RequestParams();
        params.add("student_id", String.valueOf(student_id));
        params.add("option_id", String.valueOf(option_id));

        System.out.println(KelasiApiConfiguration.END_POINT_URL+"student/attendance");

        get(KelasiApiConfiguration.END_POINT_URL+"student/attendance", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                AttendanceResponse attendanceResponse = new AttendanceResponse(response);
                mCallBack.onSuccess(attendanceResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }

    public void  TimeTable (int student_id){
        RequestParams params = new RequestParams();
        params.add("student_id",String.valueOf(student_id));
        get(KelasiApiConfiguration.END_POINT_URL+"student/timetable", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                TimeTableResponse timeTableResponse = new TimeTableResponse(response);
                mCallBack.onSuccess(timeTableResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });
    }

    public void Subject(int student_id){
        RequestParams params = new RequestParams("student_id",String.valueOf(student_id));
        get(KelasiApiConfiguration.END_POINT_URL+"student/subjects", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                SubjectResponse subjectResponse = new SubjectResponse(response);
                mCallBack.onSuccess(subjectResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });
    }

    public void Mark(int student_id, int subject_id){
        RequestParams params = new RequestParams();
        params.add("student_id",String.valueOf(student_id));
        params.add("subject_id",String.valueOf(subject_id));

        get(KelasiApiConfiguration.END_POINT_URL+"student/mark", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                MarkResponse markResponse = new MarkResponse(response);
                mCallBack.onSuccess(markResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });
    }

    public void Fees(int student_id) {
        RequestParams params = new RequestParams();
        params.add("student_id",String.valueOf(student_id));

        System.out.println(KelasiApiConfiguration.END_POINT_URL+"student/fees?student_id="+String.valueOf(student_id));


        get(KelasiApiConfiguration.END_POINT_URL+"student/fees", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                System.out.println(response.toString());

                System.out.println("SUCCESS@2");
                FeeResponse feeResponse = new FeeResponse(response);



                mCallBack.onSuccess(feeResponse);
                if (showProgressDialog)
                    progressBar.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);

                if (type.equals(KelasiApiUtils.JSONTYPE.JSON_OBJECT)) {

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (type.equals(KelasiApiUtils.JSONTYPE.JSON_ARRAY)) {
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));
                if (showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }
}
