package pro.novatech.solutions.app.cicole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.News.NewsResponse;

public class SingleNewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);
        TextView news_content = findViewById(R.id.news_content);
        Bundle bundle = getIntent().getExtras();
        NewsResponse newsresponse = (NewsResponse) bundle.getSerializable("news_response");
        news_content.setText(Html.fromHtml("<p style='text-align:justify'>"+newsresponse.getContent()+"</p>"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(newsresponse.getTitle());

    }
}
