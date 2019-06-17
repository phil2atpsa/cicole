package pro.novatech.solutions.app.cicole.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pro.novatech.solutions.app.cicole.SingleNewsActivity;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.News.NewsResponse;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder>  {

    private ArrayList<NewsResponse> news;
    private Context context;
    private  ArrayList<NewsResponse> internal = new ArrayList<>();




    public NewsDataAdapter(ArrayList<NewsResponse> news, Context context) {
        this.news = news;
        this.context = context;
        internal.addAll(news);


    }

    public void search(String query){
        news.clear();
        if (query.length() == 0) {
            news.addAll(internal);

        }

        for(NewsResponse newsResponse : internal){

            if(newsResponse.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    newsResponse.getContent().toLowerCase().contains(query.toLowerCase())){
                news.add(newsResponse);
            }
        }

        notifyDataSetChanged();
    }



    @Override
    public NewsDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsDataAdapter.ViewHolder holder, int position) {

        NewsResponse newsResponse = news.get(position);
        holder.title.setText(newsResponse.getTitle().toString());
        String content = newsResponse.getContent();
        if(content.length() >= 120) {
            content = content.substring(0, 120);
            content = content + " ...";
        }
        holder.content.setText(Html.fromHtml("<p style='text-align:justify'>"+content+"</p>"));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            View.OnClickListener, View.OnLongClickListener {
        private TextView  title, content;

        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            content = (TextView)view.findViewById(R.id.content);



            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
           /* menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Read");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Reply");
            menu.add(0, v.getId(), 0, "Delete");*/
        }


        @Override
        public void onClick(View v) {
            NewsResponse newsResponse = news.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable("news_response", newsResponse);
            context.startActivity(new Intent(context, SingleNewsActivity.class).putExtras(bundle));
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
