package com.example.swetha_pt1880.blooddonar.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.swetha_pt1880.blooddonar.R;

public class TextViewExpand extends AppCompatActivity {
    EditText my_edit_text;
    TextView my_text_view, less_more;
    String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_expand);
        my_edit_text = (EditText)findViewById(R.id.my_edit_text);
        my_text_view = (TextView) findViewById(R.id.my_text_view);
        less_more = (TextView)findViewById(R.id.more_less);

        my_edit_text.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                text = editable.toString();
                my_text_view.setText(text);
                Paint paint =  new Paint();
                paint.setTextSize(my_text_view.getTextSize());
                final float size = paint.measureText(my_text_view.getText().toString()) / 4;
                if (((int)size ) > my_text_view.getWidth()) {
                    // text is elipsized.
                    Log.i("elipse", "yes");
                    less_more.setText("more");
                    less_more.setVisibility(View.VISIBLE);
                }else
                    less_more.setVisibility(View.INVISIBLE);


            }
        });
        
        


        less_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (my_text_view.getLineCount() == 4) {
                    less_more.setText("less");
                    my_text_view.setMaxLines(Integer.MAX_VALUE);
                }
                else if (my_text_view.getLineCount()  > 4){
                    my_text_view.setMaxLines(4);
                    less_more.setText("more");
                }else {
                    less_more.setVisibility(View.GONE);
                }
            }
        });
    }



}
