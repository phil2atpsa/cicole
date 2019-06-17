package pro.novatech.solutions.kelasiapi.v1.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pro.novatech.solutions.kelasiapi.v1.ApiResponse;

/**
 * Created by p.lukengu on 4/13/2017.
 */

public class NewsResponse  implements ApiResponse, Serializable {


    String title;
    String content;

    private JSONArray news;


    private NewsResponse(){}

    private NewsResponse(JSONObject response){
        for (Field f : NewsResponse.class.getDeclaredFields()){
            if("news".equals(f.getName()))
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

    public NewsResponse(JSONArray news){
        this.news = news;
    }

    public List<NewsResponse> getNews(){
        List<NewsResponse>  newsList  = new ArrayList<NewsResponse>();


        for(int i =0; i < news.length(); i++){
            JSONObject response = null;
            try {
                response = news.getJSONObject(i);
                newsList.add( new NewsResponse(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return newsList;
    }

    @Override
    public String ToJSONObjectString() {
        JSONObject object = new JSONObject();
        for (Field f : NewsResponse.class.getDeclaredFields()){
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
        return news.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
