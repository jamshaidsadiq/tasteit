package com.i360ihrd.tasteit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Interface.ItemClickListener;
import com.i360ihrd.tasteit.Model.Category;
import com.i360ihrd.tasteit.Service.ListenOrder;
import com.i360ihrd.tasteit.ViewHolder.MenuViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    FirebaseStorage storage;
    StorageReference storageReference;

    TextView txtFullName;
    FloatingActionButton fab;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    MaterialEditText edtName;
    FButton btnUpload,btnSelect;


    Category newCategory;
    Uri uriSave;
    private final int PICK_IMAGE_REQUEST = 71;
    DrawerLayout drawer;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(this);
        //init firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("gs:images/");


         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUploadPopup();
//    Intent cartIntent = new Intent(Home.this,Cart.class);
//    startActivity(cartIntent);

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set fullname for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());


        //load menu
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        if(Common.isConnectTointernet(getBaseContext())) {
            loadMenu();
        }else{
            Toast.makeText(this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

        Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh) ;

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public
            void onRefresh() {
                loadMenu();
            }
        });
    }

    private void showUploadPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Add new Category");
        alertDialog.setMessage("Please Fill full Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                choosImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                if(newCategory !=null){
                    category.push().setValue(newCategory);
                    Snackbar.make(drawer,"New Category "+newCategory.getName()+" was added",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();




    }

    private void uploadImage() {

        if(uriSave !=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading....!!!!");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child(imageName);
            imageFolder.putFile(uriSave).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public
                void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(Home.this, "uploaded...!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public
                        void onSuccess(Uri uri) {
                            newCategory = new Category(edtName.getText().toString(),uri.toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public
                void onFailure(@NonNull Exception e) {
               dialog.dismiss();
                    Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public
                void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }

    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() != null){

            uriSave = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    private void choosImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    private void loadMenu() {
         adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {
                menuViewHolder.txtMenuName.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(menuViewHolder.imageView);

                final Category clickedItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent food = new Intent(Home.this,FoodList.class);
                        food.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(food);
                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);
        if(mSwipeRefreshLayout != null)
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id == R.id.refresh){
           loadMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

                Intent phone = new Intent(Home.this,PhoneVerify.class);
                startActivity(phone);
//            Intent cart = new Intent(Home.this,Cart.class);
//            startActivity(cart);

        } else if (id == R.id.nav_orders) {
            Intent orders = new Intent(Home.this,CartStatus.class);
            startActivity(orders);

        } else if (id == R.id.nav_log_out) {
            Paper.init(this);
            Paper.book().destroy();


            Intent signin = new Intent(Home.this,SignIn.class);
            signin.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signin);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //update / delete


    @Override
    public
    boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }


        return super.onContextItemSelected(item);
    }

    private
    void deleteCategory(String key) {
        category.child(key).removeValue();
        Toast.makeText(this, "Item Deleted....!!!", Toast.LENGTH_SHORT).show();
    }

    private
    void showUpdateDialog(final String key, final Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update Category");
        alertDialog.setMessage("Please Fill full Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);

        edtName.setText(item.getName());

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                choosImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
               changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

             item.setName(edtName.getText().toString());
             category.child(key).setValue(item);

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();


    }

    private void changeImage(final Category item) {

        if(uriSave !=null){
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading....!!!!");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child(imageName);
            imageFolder.putFile(uriSave).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public
                void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(Home.this, "uploaded...!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public
                        void onSuccess(Uri uri) {
                          item.setImage(uri.toString());


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public
                void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public
                void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded "+progress+"%");
                }
            });
        }

    }


}
