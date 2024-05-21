package com.thaikimhuynh.miladyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HelpCenterActivity extends AppCompatActivity {
    ListView listView;
    ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);

        listView = findViewById(R.id.listview);
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        ImageView backButton = findViewById(R.id.btnButtonHelpCenter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2; // Số lượng mục trong danh sách
        }

        @Override
        public Object getItem(int position) {
            return position; // Trả về vị trí của mục trong danh sách
        }

        @Override
        public long getItemId(int position) {
            return position; // Trả về ID của mục trong danh sách
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.expandable_item_helpcenter, parent, false);
            ConstraintLayout motherLayout = myView.findViewById(R.id.motherLayout);
            Button itemClicked = myView.findViewById(R.id.itemClicked);
            ImageView arrowImg = myView.findViewById(R.id.ic_arrow_right);
            LinearLayout discLayout = myView.findViewById(R.id.discLayout);
            ConstraintLayout secondLayout = myView.findViewById(R.id.secondLayout);
            TextView tvHelpCenter = myView.findViewById(R.id.tv_helpcenter);
            ImageView icFaq = myView.findViewById(R.id.ic_faq);

            // Small question views
            TextView question1 = myView.findViewById(R.id.tv_ask1);
            TextView answer1 = myView.findViewById(R.id.answer1);
            ImageView icDown1 = myView.findViewById(R.id.ic_down1);

            TextView question2 = myView.findViewById(R.id.tv_ask2);
            TextView answer2 = myView.findViewById(R.id.answer2);
            ImageView icDown2 = myView.findViewById(R.id.ic_down2);

            TextView question3 = myView.findViewById(R.id.tv_ask3);
            TextView answer3 = myView.findViewById(R.id.answer3);
            ImageView icDown3 = myView.findViewById(R.id.ic_down3);

            if (position == 0) {
                tvHelpCenter.setText("Contact Us");
            } else if (position == 1) {
                tvHelpCenter.setText("FAQs");
                icFaq.setImageResource(R.mipmap.ic_faq);
            }

            itemClicked.setOnClickListener(v -> {
                if (position == 0) {
                    if (discLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                        discLayout.setVisibility(View.VISIBLE);
                        arrowImg.setImageResource(R.mipmap.ic_arrow_up);
                    } else {
                        TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                        discLayout.setVisibility(View.GONE);
                        arrowImg.setImageResource(R.mipmap.ic_arrow_down);
                    }
                } else if (position == 1) {
                    if (secondLayout.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                        secondLayout.setVisibility(View.VISIBLE);
                        arrowImg.setImageResource(R.mipmap.ic_arrow_up);
                        icFaq.setImageResource(R.mipmap.ic_faq);
                    } else {
                        TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                        secondLayout.setVisibility(View.GONE);
                        arrowImg.setImageResource(R.mipmap.ic_arrow_down);
                    }
                }
            });

            // Small questions toggle
            icDown1.setOnClickListener(v -> {
                if (answer1.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer1.setVisibility(View.VISIBLE);
                    icDown1.setImageResource(R.mipmap.ic_arrow_up);
                } else {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer1.setVisibility(View.GONE);
                    icDown1.setImageResource(R.mipmap.ic_arrow_down);
                }
            });

            icDown2.setOnClickListener(v -> {
                if (answer2.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer2.setVisibility(View.VISIBLE);
                    icDown2.setImageResource(R.mipmap.ic_arrow_up);
                } else {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer2.setVisibility(View.GONE);
                    icDown2.setImageResource(R.mipmap.ic_arrow_down);
                }
            });

            icDown3.setOnClickListener(v -> {
                if (answer3.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer3.setVisibility(View.VISIBLE);
                    icDown3.setImageResource(R.mipmap.ic_arrow_up);
                } else {
                    TransitionManager.beginDelayedTransition(motherLayout, new AutoTransition());
                    answer3.setVisibility(View.GONE);
                    icDown3.setImageResource(R.mipmap.ic_arrow_down);
                }
            });
            Button itemClickedButton = findViewById(R.id.itemClicked);
            itemClickedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khởi tạo Intent để chuyển sang màn hình About Us
                    Intent aboutUsIntent = new Intent(HelpCenterActivity.this, AboutUsActivity.class);
                    startActivity(aboutUsIntent);
                }
            });
            ImageView backImageView = findViewById(R.id.btnButtonHelpCenter);
            backImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Khởi tạo Intent để chuyển sang màn hình Help Center
                    finish(); // Kết thúc màn hình About Us hiện tại
                }
            });




            return myView;
        }
    }
}
