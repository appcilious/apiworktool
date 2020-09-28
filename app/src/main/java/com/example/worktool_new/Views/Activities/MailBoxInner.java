package com.example.worktool_new.Views.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worktool_new.R;
import com.example.worktool_new.Util.SharedPreference.AppConstants;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class MailBoxInner extends AppCompatActivity {
    CircleImageView inboxPersonImage;
    ImageView ivForward;
    ImageView ivMailAttachment;
    TextView tvMailDate;
    TextView tvMailEmail;
    TextView tvMailName;
    TextView tvMailSubject;
    TextView tvMailTimme;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_mail_box_inner);
        this.ivForward = (ImageView) findViewById(R.id.ivForward);
        this.inboxPersonImage = (CircleImageView) findViewById(R.id.inboxPersonImage);
        this.ivMailAttachment = (ImageView) findViewById(R.id.ivMailAttachment);
        this.tvMailName = (TextView) findViewById(R.id.tvMailName);
        this.tvMailDate = (TextView) findViewById(R.id.tvMailDate);
        this.tvMailTimme = (TextView) findViewById(R.id.tvMailTimme);
        this.tvMailEmail = (TextView) findViewById(R.id.tvMailEmail);
        this.tvMailSubject = (TextView) findViewById(R.id.tvMailSubject);
        String stringExtra = getIntent().getStringExtra("mailId");
        String name = getIntent().getStringExtra("name");
        String subject = getIntent().getStringExtra("subject");
        String image = getIntent().getStringExtra("image");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String recieverimage = getIntent().getStringExtra("recieverimage");
        this.tvMailName.setText(name);
        this.tvMailDate.setText(date);
        this.tvMailTimme.setText(time);
        this.tvMailSubject.setText(subject);
        if (image != null) {
            Picasso.get().load(AppConstants.IMAGEURL + image).placeholder((int) R.drawable.profileplaceholder).resize(300, 300).error((int) R.drawable.profileplaceholder).into(this.ivMailAttachment);
        } else {
            this.ivMailAttachment.setImageResource(R.drawable.profileplaceholder);
        }
        if (recieverimage != null) {
            Picasso.get().load(AppConstants.IMAGEURL + recieverimage).placeholder((int) R.drawable.profileplaceholder).resize(300, 300).error((int) R.drawable.profileplaceholder).into((ImageView) this.inboxPersonImage);
        } else {
            this.inboxPersonImage.setImageResource(R.drawable.profileplaceholder);
        }
        this.ivForward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MailBoxInner.this.startActivity(new Intent(MailBoxInner.this, SelectUserActivity.class));
            }
        });
    }
}
