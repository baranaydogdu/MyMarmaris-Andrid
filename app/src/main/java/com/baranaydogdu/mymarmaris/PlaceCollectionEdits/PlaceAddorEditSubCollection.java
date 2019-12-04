package com.baranaydogdu.mymarmaris.PlaceCollectionEdits;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
import com.baranaydogdu.mymarmaris.PlaceEdits.PlaceAddorEditSinglePlace;
import com.baranaydogdu.mymarmaris.PlaceEdits.PlaceEditSinglePlaces;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;
import com.baranaydogdu.mymarmaris.SelectIcon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlaceAddorEditSubCollection extends AppCompatActivity {

    TextView maincollection_name,addorplace_textview;
    ImageView subcollection_image,popupimage;
    Intent in;
    PlaceCollectionClass oldmaincollection,oldsubcollection;
    SharedPreferences sharedPreferences;
    EditText l1,l2,l3,l4,l5;
    AlertDialog.Builder builder;
    Button upload_button;
    DatabaseReference mref;
    ProgressBar progressBar;
    ConstraintLayout add_place_subcons;
    Button deletebutton;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_addor_edit_sub_collection);

        activity=this;
        sharedPreferences=activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);
        builder = new AlertDialog.Builder(this);
        in=getIntent();

        oldmaincollection=(PlaceCollectionClass) in.getSerializableExtra("MainCollection");

        mref= FirebaseDatabase.getInstance().getReference().child("Places").child(oldmaincollection.getId()).child("subs");

        l1=findViewById(R.id.addplace_english_edittext);
        l2=findViewById(R.id.addplace_turkish_edittext);
        l3=findViewById(R.id.addplace_russian_edittext);
        l4=findViewById(R.id.addplace_greek_edittext);
        l5=findViewById(R.id.addplace_german_edittext);
        addorplace_textview=findViewById(R.id.addorplace_textview);
        progressBar=findViewById(R.id.addplace_progressbar);
        popupimage=findViewById(R.id.popupimage3);
        deletebutton=findViewById(R.id.addplace_delete_button);
        add_place_subcons=findViewById(R.id.add_place_subcons);

        upload_button=findViewById(R.id.addplace_upload_button);
        maincollection_name=findViewById(R.id.maincollection_name);
        subcollection_image=findViewById(R.id.addplace_subcollection_image);
        subcollection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, SelectIcon.class);
                intent.putExtra("main/sub","sub");
                startActivity(intent);
            }
        });

        maincollection_name.setText(oldmaincollection.getName().get(0));

        if (in.getStringExtra("from").equals("old")){ //DUZENLEME YAPILACAK ISE
            addorplace_textview.setText("Edit Place Sub Collection");
            upload_button.setText("Apply Changes");

            oldsubcollection=(PlaceCollectionClass) in.getSerializableExtra("SubCollection");

            sharedPreferences.edit().putInt("selected_subiconnumber",oldsubcollection.getIconnumber()).apply();

            l1.setText(oldsubcollection.getName().get(0));
            l2.setText(oldsubcollection.getName().get(1));
            l3.setText(oldsubcollection.getName().get(2));
            l4.setText(oldsubcollection.getName().get(3));
            l5.setText(oldsubcollection.getName().get(4));

            upload_button.setText("Apply Changes");
            addorplace_textview.setText("Edit Sub Place Collection");

            popupimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popupMenu_allplaces = new PopupMenu(activity, popupimage);
                    popupMenu_allplaces.getMenuInflater().inflate(R.menu.popup_menu_singleplaces, popupMenu_allplaces.getMenu());

                    popupMenu_allplaces.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getItemId() == R.id.all_places_item) {
                                Intent intentt = new Intent(activity, PlaceEditSinglePlaces.class);

                                intentt.putExtra("MainCollection",oldmaincollection);
                                intentt.putExtra("SubCollection",oldsubcollection);
                                startActivity(intentt);

                            }

                            return true;
                        }
                    });

                    popupMenu_allplaces.show();

                }
            });

        } else add_place_subcons.removeView(deletebutton);

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletesubcollection(activity,oldmaincollection,oldsubcollection);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebaseSubCollection();
            }
        });

    }

    private void deletesubcollection(final Activity activity,final PlaceCollectionClass maincollection, final PlaceCollectionClass subcollection) {

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        builder.setIcon(R.drawable.warningicon);
        builder.setTitle("ARE YOU SURE");
        builder.setMessage("You are removing all the content. This progress can not be back.");
        builder.setNegativeButton("CANCEL", null);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressBar.setVisibility(View.VISIBLE);
                upload_button.setEnabled(false);

                removesubcollectionfilesfromfirebase(activity,maincollection,subcollection);

                mref.child("Places").child(maincollection.getId()).
                        child("subs").child(subcollection.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.INVISIBLE);
                        upload_button.setEnabled(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setIcon(R.drawable.checkicon);
                        builder.setTitle("SUCCESS");
                        builder.setMessage("Deleting success");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        });
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(true).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.setVisibility(View.INVISIBLE);
                        upload_button.setEnabled(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        builder.setIcon(R.drawable.error_icon);
                        builder.setTitle("ERROR");
                        builder.setMessage("Unknown error : "+e.getLocalizedMessage());
                        builder.setPositiveButton("OK", null );
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(true).show();

                    }
                });
            }
        }).setCancelable(false).show();


    }

    public static void removesubcollectionfilesfromfirebase(Activity activity,PlaceCollectionClass maincollection,PlaceCollectionClass subcollection) {

        for (PlaceClass place:PreSets.getAllPlaces_from_subcollection(activity,subcollection.getId())){

            PlaceAddorEditSinglePlace.removeplacefilesfromfirebase(place);
        }

    }


    private void uploadFirebaseSubCollection() {

        if (l1.getText().toString().equals("")){ //INGILIZCE METIN YOK ISE
            builder.setIcon(R.drawable.error_icon);
            builder.setTitle("ERROR");
            builder.setMessage("English text can not be empty! Please give a english name.");
            builder.setPositiveButton("OK",null);
            builder.setNegativeButton(null,null);
            builder.setCancelable(true).show();

        } else { //INGILIZCE NETIN VAR ISE

            if (l2.getText().toString().equals("") ||
                    l3.getText().toString().equals("") ||
                    l4.getText().toString().equals("") ||
                    l4.getText().toString().equals("")  ){ //DIGER METINLER YOK ISE

                builder.setIcon(R.drawable.warningicon);
                builder.setTitle("Name texts are empty !");
                builder.setMessage("When name text is empty Users could see only the english texts...");
                builder.setNegativeButton("CANCEL", null)
                        .setPositiveButton("ANYWAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //BAZI METINLER EKSIK ISE
                                uploadFirebase();
                            }
                        });

                builder.setCancelable(false).show();

            } else { //TUM METINLER VAR ISE

                uploadFirebase();
            }
        }
    }

    private void uploadFirebase() {

        progressBar.setVisibility(View.VISIBLE);
        upload_button.setEnabled(false);

        if (in.getStringExtra("from").equals("old")){ // GUNCELLEME ISE

            final PlaceCollectionClass newsubcollection = new PlaceCollectionClass();

            newsubcollection.setId(oldsubcollection.getId());
            ArrayList<String> names=new ArrayList();

            names.add(l1.getText().toString());
            names.add(l2.getText().toString());
            names.add(l3.getText().toString());
            names.add(l4.getText().toString());
            names.add(l5.getText().toString());

            newsubcollection.setName(names);
            newsubcollection.setIconnumber(sharedPreferences.getInt("selected_subiconnumber", 0));
            newsubcollection.setSortnumber(oldsubcollection.getSortnumber());

            mref.child(newsubcollection.getId()).child("info").setValue(newsubcollection).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressBar.setVisibility(View.INVISIBLE);
                    upload_button.setEnabled(true);

                    if (task.isSuccessful()) {  //DATA KAYDEDILDI ISE

                        builder.setIcon(R.drawable.checkicon);
                        builder.setTitle("SUCCESS");
                        builder.setMessage("Upload is successed.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(false).show();

                    } else { //DATA KAYIT EDILEMEDI ISE

                        builder.setIcon(R.drawable.error_icon);
                        builder.setTitle("ERROR");
                        builder.setMessage("ERROR : " + task.getException().getLocalizedMessage());
                        builder.setNegativeButton(null, null);
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(true).show();

                    }
                }
            });


        } else {//YENI ALT KOLEKSIYON

            Query query=mref.orderByChild("info/sortnumber");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int newnumber=0;
                    if (dataSnapshot.exists()){
                        for (DataSnapshot ds:dataSnapshot.getChildren()){
                            newnumber++;

                        }
                    }

                    final PlaceCollectionClass newsubcollection = new PlaceCollectionClass();

                    newsubcollection.setId(mref.push().getKey());
                    ArrayList<String> names=new ArrayList();

                    names.add(l1.getText().toString());
                    names.add(l2.getText().toString());
                    names.add(l3.getText().toString());
                    names.add(l4.getText().toString());
                    names.add(l5.getText().toString());

                    newsubcollection.setName(names);
                    newsubcollection.setIconnumber(sharedPreferences.getInt("selected_subiconnumber", 0));
                    newsubcollection.setSortnumber(newnumber);

                    mref.child(newsubcollection.getId()).child("info").setValue(newsubcollection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            progressBar.setVisibility(View.INVISIBLE);
                            upload_button.setEnabled(true);

                            if (task.isSuccessful()) {  //DATA KAYDEDILDI ISE

                                builder.setIcon(R.drawable.checkicon);
                                builder.setTitle("SUCCESS");
                                builder.setMessage("Upload is successed.");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.setNegativeButton(null, null);
                                builder.setCancelable(false).show();

                            } else { //DATA KAYIT EDILEMEDI ISE

                                builder.setIcon(R.drawable.error_icon);
                                builder.setTitle("ERROR");
                                builder.setMessage("ERROR : " + task.getException().getLocalizedMessage());
                                builder.setNegativeButton(null, null);
                                builder.setPositiveButton("OK", null);
                                builder.setCancelable(true).show();

                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreSets.setImageIcon(subcollection_image,sharedPreferences.getInt("selected_subiconnumber",0));
    }
}
