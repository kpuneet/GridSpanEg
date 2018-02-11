package com.puneet.newscorp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FullScreenImageActivity extends AppCompatActivity {

    public static final String IMAGE_MODEL = "com.puneet.newscorp.IMAGE_MODEL";
    public static final String TITLE_WIDTH = "com.puneet.newscorp.TITLE_WIDTH";

    private ImageModel imageModel;
    private Unbinder unbinder;

    @BindView(R.id.fullscreen_image) ImageView fullscreenImage;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fullscreen_image_title) TextView fullscreenImageText;

    public static void showImage(Activity activity, ImageView imageView, TextView titleView, ImageModel imageModel, int titleWidth) {
        Intent intent = new Intent(activity, FullScreenImageActivity.class);
        intent.putExtra(IMAGE_MODEL, imageModel);
        intent.putExtra(TITLE_WIDTH, titleWidth);
        Pair<View, String> p1 = Pair.create((View) imageView, activity.getString(R.string.activity_image_trans));
        Pair<View, String> p2 = Pair.create((View) titleView, activity.getString(R.string.activity_title_trans));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageModel = (ImageModel) getIntent().getSerializableExtra(IMAGE_MODEL);
        setContentView(LayoutInflater.from(this).inflate(R.layout.activity_fullscreen_image, null));
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.fullscreen_imageview);
        postponeEnterTransition();
        ViewGroup.LayoutParams lp = fullscreenImageText.getLayoutParams();
        lp.width = getIntent().getIntExtra(TITLE_WIDTH, ActionBar.LayoutParams.MATCH_PARENT);
        fullscreenImageText.setLayoutParams(lp);
        fullscreenImageText.setText(imageModel.title);
        Picasso.with(this).load(imageModel.url).into(fullscreenImage, new Callback() {
            @Override
            public void onSuccess() {
                startPostponedEnterTransition();
            }

            @Override
            public void onError() {
                Toast.makeText(FullScreenImageActivity.this, R.string.error_loading_fullscreen_image, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
