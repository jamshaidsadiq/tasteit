package com.i360ihrd.tasteit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.Interface.ItemClickListener;
import com.i360ihrd.tasteit.Model.Food;
import com.i360ihrd.tasteit.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foodList;

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;
    String Categoryid;

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //init firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        //load menu
        recycler_food = (RecyclerView)findViewById(R.id.recycler_food);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);

        if(getIntent() !=null){
            Categoryid = getIntent().getStringExtra("CategoryId");
        }

        if(!Categoryid.isEmpty() && Categoryid !=null)
        {
            loadListFood(Categoryid);
        }

    }

    private void loadListFood(String categoryid) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,R.layout.food_item,FoodViewHolder.class,foodList.orderByChild("MenuId").equalTo(Categoryid)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.food_image);
                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {


                        //start new activity

                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(foodDetail);

                    }
                });

            }
        };

        recycler_food.setAdapter(adapter);
    }
}
