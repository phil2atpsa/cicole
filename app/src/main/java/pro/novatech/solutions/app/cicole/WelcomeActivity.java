package pro.novatech.solutions.app.cicole;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;

public class WelcomeActivity extends AppCompatActivity {
    private MyPreferenceManager myPreferenceManager;
    private Button btnNext;
    private Button btnSkip;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;

    private ViewPager viewPager;


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        public void onPageScrollStateChanged(int paramAnonymousInt)
        {
        }

        public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
        {
        }

        public void onPageSelected(int paramAnonymousInt)
        {
            addBottomDots(paramAnonymousInt);
            if (paramAnonymousInt == -1 + layouts.length)
            {
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else

              btnNext.setText(getString(R.string.next));
              btnSkip.setVisibility(View.VISIBLE);
            }
        };


    @Override
    protected  void onResume(){
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        myPreferenceManager = new MyPreferenceManager(this);
        if ((myPreferenceManager.isFirstTimeLaunch()) || (Build.VERSION.SDK_INT >= 21))
            getWindow().getDecorView().setSystemUiVisibility(1280);

        if(!myPreferenceManager.isFirstTimeLaunch()){
            launchHomeScreen();
        }

        super.onCreate(savedInstanceState);








        setContentView(R.layout.activity_welcome);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        dotsLayout  =((LinearLayout)findViewById(R.id.layoutDots));
        viewPager = (ViewPager) findViewById(R.id.view_pager);



        //int[] arrayOfInt = new int[4];
        int[] arrayOfInt = new int[]{ R.layout.welcome_side1};
       // arrayOfInt[0] = R.layout.welcome_side1;
       /* arrayOfInt[1] = R.layout.welcome_slide2;
        arrayOfInt[2] = R.layout.welcome_slide3;
        arrayOfInt[3] = R.layout.welcome_slide4;*/
        layouts = arrayOfInt;
        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                launchHomeScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                int i = getItem(1);

                if (i < layouts.length)
                    viewPager.setCurrentItem(i);
                else
                    launchHomeScreen();
            }
        });



    }

    private int getItem(int paramInt)
    {
        return paramInt + viewPager.getCurrentItem();
    }


    private void launchHomeScreen()
    {
        myPreferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }


    private void addBottomDots(int paramInt)
    {
        this.dots = new TextView[this.layouts.length];
        int[] arrayOfInt1 = getResources().getIntArray(R.array.array_dot_active);
        int[] arrayOfInt2 = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < this.dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35.0F);
            dots[i].setTextColor(arrayOfInt2[paramInt]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[paramInt].setTextColor(arrayOfInt1[paramInt]);
    }

    private void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            Window localWindow = getWindow();
            localWindow.addFlags(-2147483648);
            localWindow.setStatusBarColor(0);
        }
    }


    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter()
        {
        }

        public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
        {
            paramViewGroup.removeView((View)paramObject);
        }

        public int getCount()
        {
            return layouts.length;
        }

        public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
        {
            layoutInflater = getLayoutInflater();
            View localView = layoutInflater.inflate(layouts[paramInt], paramViewGroup, false);
            paramViewGroup.addView(localView);
            return localView;
        }

        public boolean isViewFromObject(View paramView, Object paramObject)
        {
          return (paramView == paramObject);
        }
    }
}
