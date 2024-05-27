package com.example.doramyclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private List<CastModel> castModels;
    private List<PartModel> partModels;
    private CastAdapter castAdapter;
    private PartAdapter partAdapter;

    private RecyclerView part_recycler_view, cast_recycler_view;

    private ImageView thumb, cover;
    private TextView title, desc;
    private FloatingActionButton actionButton;

    private String title_movie;
    private String des_movie;
    private String thumb_movie;
    private String link_movie;
    private String cover_movie;
    private String cast_movie;
    private String trailer_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        thumb = findViewById(R.id.thumb);
        cover = findViewById(R.id.cover);
        title = findViewById(R.id.title_details);
        desc = findViewById(R.id.desc);
        actionButton = findViewById(R.id.floatingActionButton2);

        cast_recycler_view = findViewById(R.id.recyclerView_casts);
        part_recycler_view = findViewById(R.id.recyclerView_parts);

        title_movie = getIntent().getStringExtra("title");
        des_movie = getIntent().getStringExtra("desc");
        thumb_movie = getIntent().getStringExtra("thumb");
        link_movie = getIntent().getStringExtra("link");
        cover_movie = getIntent().getStringExtra("cover");
        cast_movie = getIntent().getStringExtra("cast");
        trailer_movie = getIntent().getStringExtra("t_link");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title_movie);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Glide.with(this).load(thumb_movie).into(thumb);
        Glide.with(this).load(cover_movie).into(cover);

        thumb.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));
        cover.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation));

        title.setText(title_movie);
        desc.setText(des_movie);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("link").child(trailer_movie).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String vidUrl = snapshot.getValue(String.class);
                        Intent intent = new Intent(DetailsActivity.this, PlayerActivity.class);
                        intent.putExtra("vid", vidUrl);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadCast();
        loadPart();
    }

    private void loadPart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference partRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        part_recycler_view.setLayoutManager(layoutManager);

        partModels = new ArrayList<>();
        partAdapter = new PartAdapter(partModels);
        part_recycler_view.setAdapter(partAdapter);

        partRef.child("link").child(link_movie).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot content:snapshot.getChildren()){
                    CastModel castModel = content.getValue(CastModel.class);
                    castModels.add(castModel);
                }
                partAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCast() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference castRef = database.getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        cast_recycler_view.setLayoutManager(layoutManager);

        castModels = new ArrayList<>();
        castAdapter = new CastAdapter(castModels);
        cast_recycler_view.setAdapter(castAdapter);

        castRef.child("cast").child(cast_movie).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot content:snapshot.getChildren()){
                    CastModel castModel = content.getValue(CastModel.class);
                    castModels.add(castModel);
                }
                castAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}