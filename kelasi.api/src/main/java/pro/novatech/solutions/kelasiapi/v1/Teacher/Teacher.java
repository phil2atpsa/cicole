package pro.novatech.solutions.kelasiapi.v1.Teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pro.novatech.solutions.kelasiapi.R;
import pro.novatech.solutions.kelasiapi.v1.Exams.ExamsResponse;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiConfiguration;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiUtils;
import pro.novatech.solutions.kelasiapi.v1.Login.SuccessResponse;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.MimeType;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
import pro.novatech.solutions.kelasiapi.v1.Student.StudentResponse;
import pro.novatech.solutions.kelasiapi.v1.Subject.SubjectResponse;

/**
 * Created by pro on 2018/02/10.
 */

public class Teacher extends AsyncHttpClient {

    private OnServiceResponseListener<Object> mCallBack;
    private Context mContext;
    private ProgressDialog progressBar;
    private boolean  showProgressDialog;


    public Teacher(Context context, OnServiceResponseListener callback, boolean showProgressDialog) {
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

    public void Correspondents(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"teacher/correspondents",params,
                new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray users) {
                // Pull out the first event on the public timeline
                UserAccessResponse userAccessResponse = new UserAccessResponse(users);
                mCallBack.onSuccess(userAccessResponse);

                if(showProgressDialog)
                    progressBar.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //  Log.e("ERROR", t.getMessage());
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                if(showProgressDialog)
                    progressBar.dismiss();
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }

    public void Subjects(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"teacher/subjects",params,
                new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray subjects) {
                        // Pull out the first event on the public timeline
                        SubjectResponse subjectResponse = new SubjectResponse(subjects);
                        mCallBack.onSuccess(subjectResponse);

                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //  Log.e("ERROR", t.getMessage());
                        JSONArray array = null;
                        String error_message = res;

                        KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                        if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                            JSONObject object = null;
                            try {
                                object = new JSONObject(res);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                            try {
                                array = new JSONArray(res);
                                JSONObject object = array.getJSONObject(0);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                        if(showProgressDialog)
                            progressBar.dismiss();
                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                });

    }



    public void Exams(String access_token, int subject_id){
        Map map = new HashMap();
        map.put("access-token",access_token);
        map.put("subject_id",String.valueOf(subject_id));

        RequestParams params =  new RequestParams(map);

        get(KelasiApiConfiguration.END_POINT_URL+"teacher/exam",params,
                new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray exams) {
                        // Pull out the first event on the public timeline
                        ExamsResponse examsResponse = new ExamsResponse(exams);
                        mCallBack.onSuccess(examsResponse);

                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //  Log.e("ERROR", t.getMessage());
                        JSONArray array = null;
                        String error_message = res;

                        KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                        if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                            JSONObject object = null;
                            try {
                                object = new JSONObject(res);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                            try {
                                array = new JSONArray(res);
                                JSONObject object = array.getJSONObject(0);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                        if(showProgressDialog)
                            progressBar.dismiss();
                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                });

    }

    public void Marks( String access_token, int student_id) throws UnsupportedEncodingException {
        Map map = new HashMap();
        map.put("access-token",access_token);
        map.put("student_id",String.valueOf(student_id));

        RequestParams params =  new RequestParams(map);

        get(KelasiApiConfiguration.END_POINT_URL+"teacher/marks",params,
                new JsonHttpResponseHandler(){


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray marks) {
                        // Pull out the first event on the public timeline
                        TeacherMarksResponse teacherMarkResponse  = new TeacherMarksResponse(marks);
                        mCallBack.onSuccess(teacherMarkResponse);

                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //  Log.e("ERROR", t.getMessage());
                        JSONArray array = null;
                        String error_message = res;

                        KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                        if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                            JSONObject object = null;
                            try {
                                object = new JSONObject(res);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                            try {
                                array = new JSONArray(res);
                                JSONObject object = array.getJSONObject(0);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                        if(showProgressDialog)
                            progressBar.dismiss();
                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                });

    }

    public void  Attendance(  JSONObject params, String access_token) throws UnsupportedEncodingException {

        StringEntity entity = new StringEntity(params.toString());

        post(mContext,KelasiApiConfiguration.END_POINT_URL+"teacher/attendance?access-token="+access_token, entity, "application/json",
                new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject message) {
                        // Pull out the first event on the public timeline
                        SuccessResponse successResponse = new SuccessResponse(message);
                        mCallBack.onSuccess(successResponse);

                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //  Log.e("ERROR", t.getMessage());
                        JSONArray array = null;
                        String error_message = res;

                        KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                        if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                            JSONObject object = null;
                            try {
                                object = new JSONObject(res);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                            try {
                                array = new JSONArray(res);
                                JSONObject object = array.getJSONObject(0);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                        if(showProgressDialog)
                            progressBar.dismiss();
                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                });


    }

    public void  Mark(  JSONObject params, String access_token) throws UnsupportedEncodingException {

        StringEntity entity = new StringEntity(params.toString());

        post(mContext,KelasiApiConfiguration.END_POINT_URL+"teacher/marks?access-token="+access_token, entity, "application/json",
                new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject message) {
                        // Pull out the first event on the public timeline
                        SuccessResponse successResponse = new SuccessResponse(message);
                        mCallBack.onSuccess(successResponse);

                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        //  Log.e("ERROR", t.getMessage());
                        JSONArray array = null;
                        String error_message = res;

                        KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                        if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                            JSONObject object = null;
                            try {
                                object = new JSONObject(res);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                            try {
                                array = new JSONArray(res);
                                JSONObject object = array.getJSONObject(0);
                                error_message = object.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                        if(showProgressDialog)
                            progressBar.dismiss();
                        if(showProgressDialog)
                            progressBar.dismiss();
                    }
                });


    }

    public void Attendances(String access_token){
        RequestParams params = new RequestParams("access-token", access_token);
        get(KelasiApiConfiguration.END_POINT_URL+"teacher/students",params, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray  students) {
                // Pull out the first event on the public timeline
                StudentResponse studentResponse = new StudentResponse(students);
                mCallBack.onSuccess(studentResponse);


                if(showProgressDialog)
                    progressBar.dismiss();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //  Log.e("ERROR", t.getMessage());
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                if(showProgressDialog)
                    progressBar.dismiss();
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }
    public void Student(String access_token, int student_id){
        Map map = new HashMap();
        map.put("access-token",access_token);
        map.put("student_id",String.valueOf(student_id));

        RequestParams params =  new RequestParams(map);

        get(KelasiApiConfiguration.END_POINT_URL+"teacher/students",params, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject  student) {
                // Pull out the first event on the public timeline
                StudentResponse studentResponse = new StudentResponse(student);
                mCallBack.onSuccess(studentResponse);


                if(showProgressDialog)
                    progressBar.dismiss();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                //  Log.e("ERROR", t.getMessage());
                JSONArray array = null;
                String error_message = res;

                KelasiApiUtils.JSONTYPE type = KelasiApiUtils.isJSONValid(res);
                if(type.equals( KelasiApiUtils.JSONTYPE.JSON_OBJECT)){

                    JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if(type.equals( KelasiApiUtils.JSONTYPE.JSON_ARRAY)){
                    try {
                        array = new JSONArray(res);
                        JSONObject object = array.getJSONObject(0);
                        error_message = object.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mCallBack.onFailure(new KelasiApiException(res != null ? error_message : t.getMessage()));

                if(showProgressDialog)
                    progressBar.dismiss();
                if(showProgressDialog)
                    progressBar.dismiss();
            }
        });

    }
}
