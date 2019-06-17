package pro.novatech.solutions.app.cicole.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;

public class MyPreferenceManager
{
  private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
  private static final String PREF_NAME = "androidhive-welcome";
  int PRIVATE_MODE = 0;
  Context _context;
  Editor editor;
  SharedPreferences pref;

  public MyPreferenceManager(Context paramContext)
  {
    _context = paramContext;
    pref = _context.getSharedPreferences("androidhive-welcome", PRIVATE_MODE);
    editor = pref.edit();
  }

  public boolean isFirstTimeLaunch()
  {
    return pref.getBoolean("IsFirstTimeLaunch", true);
  }

  public void setFirstTimeLaunch(boolean paramBoolean)
  {
    editor.putBoolean("IsFirstTimeLaunch", paramBoolean).commit();
  }
  public boolean isUserSession(){
      return pref.contains("userSession");
  }

  public UserAccessResponse getUserSession() throws JSONException {
     return new UserAccessResponse(new JSONObject(pref.getString("userSession","")));
  }
  public void setUserSession(String userSession){
      editor.putString("userSession", userSession).commit();
  }

  public boolean hasCorrespondents(){ return pref.contains("correspondents");}

  public void setCorrespondents(String correspondents){
    editor.putString("correspondents", correspondents).commit();
  }

  public List<UserAccessResponse> getCorrespondents() throws JSONException {
    return new UserAccessResponse( new JSONArray(pref.getString("correspondents",""))).getUserList();
  }
  public void terminateUserSession(){
    editor.remove("userSession").commit();
  }
  public void clearCorrespondents(){
    editor.remove("correspondents").commit();
  }

}

