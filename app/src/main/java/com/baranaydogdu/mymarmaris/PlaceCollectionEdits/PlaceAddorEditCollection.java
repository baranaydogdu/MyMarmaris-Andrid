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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.Classes.PlaceClass;
import com.baranaydogdu.mymarmaris.Classes.PlaceCollectionClass;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceAddorEditCollection extends AppCompatActivity {

    EditText l1,l2,l3,l4,l5,collection_link_edx;
    TextView addoredit;
    ImageView placeimage,collection_type_icon;
    Button uploadbutton,delete_button;
    DatabaseReference mRef;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;
    ProgressBar progressBar;
    Intent intent;
    PlaceCollectionClass oldmaincollection;
    ImageView popupimage;
    Activity activity;
    ConstraintLayout add_place_subcons;
    Switch is_inside_switch;
    int collection_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_add_or_edit__collection);

        intent=getIntent();
        activity=this;

        mRef= FirebaseDatabase.getInstance().getReference();
        builder = new AlertDialog.Builder(this);
        sharedPreferences = activity.getSharedPreferences("com.baranaydogdu.mymarmaris", Context.MODE_PRIVATE);

        popupimage=findViewById(R.id.popupimage3);
        uploadbutton=findViewById(R.id.addplace_upload_button);
        placeimage=findViewById(R.id.addplace_image);
        progressBar=findViewById(R.id.addplace_progressbar);
        delete_button=findViewById(R.id.addplace_delete_button);
        collection_type_icon=findViewById(R.id.collection_type_icon);

        l1=findViewById(R.id.addplace_english_edittext);
        l2=findViewById(R.id.addplace_turkish_edittext);
        l3=findViewById(R.id.addplace_russian_edittext);
        l4=findViewById(R.id.addplace_greek_edittext);
        l5=findViewById(R.id.addplace_german_edittext);
        collection_link_edx=findViewById(R.id.collection_link_edx);
        addoredit=findViewById(R.id.addorplace_textview);
        add_place_subcons=findViewById(R.id.add_place_subcons);
        is_inside_switch=findViewById(R.id.is_inside_switch);

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletemaincollection(activity,oldmaincollection);
            }
        });

        placeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SelectIcon.class);
                intent.putExtra("main/sub","main");
                startActivity(intent);
            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_place_collection();
            }
        });

        if (intent.getStringExtra("from").equals("old")){ // DUZNELEME YAPILACAK ISE

            oldmaincollection=(PlaceCollectionClass) intent.getSerializableExtra("MainCollection");

            sharedPreferences.edit().putInt("selectediconnumber",oldmaincollection.getIconnumber()).apply();

            l1.setText(oldmaincollection.getName().get(0));
            l2.setText(oldmaincollection.getName().get(1));
            l3.setText(oldmaincollection.getName().get(2));
            l4.setText(oldmaincollection.getName().get(3));
            l5.setText(oldmaincollection.getName().get(4));
            collection_link_edx.setText(oldmaincollection.getLink());

            collection_type=oldmaincollection.getType();
            uploadbutton.setText("Apply Changes");
            addoredit.setText("Edit Place Collection");

            if (oldmaincollection.getType()>3){

                add_place_subcons.removeView(delete_button);
            }

            if (oldmaincollection.getType()==PlaceCollectionClass.INSIDE_LINKKED_COLLECTION){
                is_inside_switch.setChecked(true);

            } else if (oldmaincollection.getType()==PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION) {
                is_inside_switch.setChecked(false);
            }

        } else { //YENI GELDIK

            add_place_subcons.removeView(delete_button);

        collection_type=intent.getIntExtra("type",0);

        }

        collection_link_edx.setVisibility(View.INVISIBLE);
        is_inside_switch.setVisibility(View.INVISIBLE);

        collection_type_icon.setVisibility(View.VISIBLE);

        if (collection_type == PlaceCollectionClass.COLLECTION_WITH_SUB) {
            collection_type_icon.setImageResource(R.drawable.maincollection);

        } else if (collection_type == PlaceCollectionClass.COLLECTION_NON_SUB) {
            collection_type_icon.setImageResource(R.drawable.tinycollection);

        } else if (collection_type == PlaceCollectionClass.INSIDE_LINKKED_COLLECTION ||
                collection_type == PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION) {
            collection_type_icon.setImageResource(R.drawable.linked);
            collection_link_edx.setVisibility(View.VISIBLE);
            is_inside_switch.setVisibility(View.VISIBLE);

            is_inside_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (is_inside_switch.isChecked()) collection_type=PlaceCollectionClass.INSIDE_LINKKED_COLLECTION;
                    else collection_type=PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION;
                }
            });

        } else collection_type_icon.setVisibility(View.INVISIBLE);



    }

    private void deletemaincollection(final Activity activity,final PlaceCollectionClass maincollection) {

        final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();

        builder.setIcon(R.drawable.warningicon);
        builder.setTitle("ARE YOU SURE");
        builder.setMessage("You are removing all the content. This progress can not be back.");
        builder.setNegativeButton("CANCEL", null);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressBar.setVisibility(View.VISIBLE);
                uploadbutton.setEnabled(false);

                removemaincollectionfilesfromfirebase(activity,maincollection);

                mref.child("Places").child(maincollection.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override

                    public void onSuccess(Void aVoid) {

                        progressBar.setVisibility(View.INVISIBLE);
                        uploadbutton.setEnabled(true);
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
                        builder.setCancelable(false).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressBar.setVisibility(View.INVISIBLE);
                        uploadbutton.setEnabled(true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setIcon(R.drawable.error_icon);
                        builder.setTitle("ERROR");
                        builder.setMessage("Unknown error : "+e.getLocalizedMessage());
                        builder.setPositiveButton("OK", null);
                        builder.setNegativeButton(null, null);
                        builder.setCancelable(true).show();

                    }
                });
            }
        }).setCancelable(false).show();


    }

    public static void removemaincollectionfilesfromfirebase(Activity activity,PlaceCollectionClass maincollection) {

        for (PlaceCollectionClass subcollection:PreSets.getAll_SubCollectionfrom_from_mainCollection(activity,maincollection.getId())){

            PlaceAddorEditSubCollection.removesubcollectionfilesfromfirebase(activity,maincollection,subcollection);
        }

    }

    private void upload_place_collection() {

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
                l4.getText().toString().equals("")  ){ //DIGER METINLER EKSIK ISE

                builder.setIcon(R.drawable.warningicon);
                builder.setTitle("Name texts are empty !");
                builder.setMessage("When name text is empty Users could see only the english texts...");
                builder.setNegativeButton("CANCEL", null)
                        .setPositiveButton("ANYWAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        uploadbutton.setEnabled(false);

        if (intent.getStringExtra("from").equals("old")) {  //DUZNELEME ISE

            final PlaceCollectionClass newcollection = new PlaceCollectionClass();

            newcollection.setId(oldmaincollection.getId());
            ArrayList<String> names=new ArrayList<>();

            names.add(l1.getText().toString());
            names.add(l2.getText().toString());
            names.add(l3.getText().toString());
            names.add(l4.getText().toString());
            names.add(l5.getText().toString());

            newcollection.setName(names);
            newcollection.setSortnumber(oldmaincollection.getSortnumber());
            newcollection.setType(collection_type);
            newcollection.setLink(collection_link_edx.getText().toString());

            newcollection.setIconnumber(sharedPreferences.getInt("selectediconnumber", 0));

            mRef.child("Places").child(newcollection.getId()).child("info").setValue(newcollection).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressBar.setVisibility(View.INVISIBLE);
                    uploadbutton.setEnabled(true);

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

                        progressBar.setVisibility(View.INVISIBLE);
                        uploadbutton.setEnabled(true);

                        builder.setIcon(R.drawable.error_icon);
                        builder.setTitle("ERROR");
                        builder.setMessage("ERROR : " + task.getException().getLocalizedMessage());
                        builder.setNegativeButton(null, null);
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(true).show();

                    }


                }
            });

        }else { //YENI KAYIT ISE

            mRef.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int newnumber=0;

                        for(DataSnapshot ds:dataSnapshot.getChildren()){

                            newnumber++;
                        }

                    final PlaceCollectionClass newcollection = new PlaceCollectionClass();

                    newcollection.setId(mRef.child("Places").push().getKey());
                    ArrayList<String> names=new ArrayList<>();

                    names.add(l1.getText().toString());
                    names.add(l2.getText().toString());
                    names.add(l3.getText().toString());
                    names.add(l4.getText().toString());
                    names.add(l5.getText().toString());

                    newcollection.setName(names);
                    newcollection.setSortnumber(newnumber);
                    newcollection.setType(collection_type);
                    newcollection.setLink(collection_link_edx.getText().toString());
                    newcollection.setIconnumber(sharedPreferences.getInt("selectediconnumber", 0));

                    HashMap<String,Object> hashMap_new=new HashMap<>();
                    hashMap_new.put(newcollection.getId()+"/info",newcollection);

                    if (newcollection.getType()==PlaceCollectionClass.COLLECTION_NON_SUB){
                        hashMap_new.put(newcollection.getId()+"/subs/"+newcollection.getId()+"/info",newcollection);

                    } else if (newcollection.getType()==PlaceCollectionClass.INSIDE_LINKKED_COLLECTION ||
                                newcollection.getType()==PlaceCollectionClass.OUTSIDE_LINKKED_COLLECTION){

                        hashMap_new.put(newcollection.getId()+"/subs/"+newcollection.getId()+"/info",newcollection);
                        PlaceClass emtyplace=new PlaceClass(newcollection.getId(),newcollection.getName());
                        hashMap_new.put(newcollection.getId()+"/subs/"+newcollection.getId()+"/places/"+newcollection.getId(),emtyplace);

                    }

                    mRef.child("Places").updateChildren(hashMap_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressBar.setVisibility(View.INVISIBLE);
                                uploadbutton.setEnabled(true);

                                if (task.isSuccessful()) {  //DATA KAYDEDILDI ISE
                                    progressBar.setVisibility(View.INVISIBLE);
                                    uploadbutton.setEnabled(true);

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

                    progressBar.setVisibility(View.INVISIBLE);
                    uploadbutton.setEnabled(true);

                    builder.setIcon(R.drawable.error_icon);
                    builder.setTitle("ERROR");
                    builder.setMessage("ERROR : " + databaseError.getMessage());
                    builder.setNegativeButton(null, null);
                    builder.setPositiveButton("OK", null);
                    builder.setCancelable(true).show();

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreSets.setImageIcon(placeimage,sharedPreferences.getInt("selectediconnumber",0));
    }

}
