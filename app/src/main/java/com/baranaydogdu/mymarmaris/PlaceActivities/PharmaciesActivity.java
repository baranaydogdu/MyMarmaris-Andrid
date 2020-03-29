package com.baranaydogdu.mymarmaris.PlaceActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baranaydogdu.mymarmaris.Classes.LocationClass;
import com.baranaydogdu.mymarmaris.Classes.PharmacyClass;
import com.baranaydogdu.mymarmaris.PreSets;
import com.baranaydogdu.mymarmaris.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class PharmaciesActivity extends AppCompatActivity implements LocationListener {

    RecyclerView recyclerView;
    public Myadapapter pharmacyAdapter;
    public ArrayList<PharmacyClass> pharmacieslist;
    TextView date;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);

        recyclerView=findViewById(R.id.pharmacy_recyclerview);
        date=findViewById(R.id.textView5);

        pharmacieslist = new ArrayList<>();

        pharmacyAdapter = new Myadapapter(this,pharmacieslist);
        recyclerView.setAdapter(pharmacyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (PreSets.networkConnection(this)){

            new AsynTask().execute("https://www.eczaneler.gen.tr/iframe.php?lokasyon=1517");

        }
         //new AsynTask().execute("https://enyakinatm.bkm.com.tr/assets/js/scripts.js");
        //new AsynTask().execute("https://maps.googleapis.com/maps/api/js?key=AIzaSyAMR2F9V9efC0GI2j_HXhIBzjv1tptCbYI&callback=bkm.enyakinatm.initialize&v=3.27");



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            //System.out.println("11111");
        }else {

            //System.out.println("else");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PlacesActivity.LOCATIONMILISECOND, 0, this);

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PlacesActivity.LOCATIONMILISECOND, 0, this);

        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PlacesActivity.LOCATIONMILISECOND, 0, this);

                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //System.out.println("LOCATION GELDI");
        int i=0;
        for (PharmacyClass pharmacyClass : pharmacieslist) {

            Double lat = pharmacyClass.getLocatin().getLat();
            Double log = pharmacyClass.getLocatin().getLog();

            Location pharmacy_location = new Location("Pharmacy");
            pharmacy_location.setLongitude(log);
            pharmacy_location.setLatitude(lat);

            float distance = location.distanceTo(pharmacy_location);
            pharmacieslist.get(i).setDistance(distance);

            i++;
        }

        Collections.sort(pharmacieslist, new Comparator<PharmacyClass>() {
            @Override
            public int compare(PharmacyClass o1, PharmacyClass o2) {
                return o1.getDistance() - o2.getDistance();
            }

        });

        pharmacyAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public class AsynTask extends AsyncTask<String, Void, String> {

        String server_response;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getData(server_response);

            //System.out.println("server presponce : "+server_response);

        }



        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                System.out.println("responceCode : "+responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    //Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Converting InputStream to String
        private String readStream(InputStream in) {

            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {

                    response.append(line);
                    //System.out.println(reader.readLine());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }

    public void getData(String server_response){

        server_response=server_response.replaceAll("\\?","");
        //System.out.println("server_response : "+server_response);

        //TARIH USTUNDEKI GERKESIZ KISMI ATIYOR - BASLANGIZ TARIH KALIYOR
        String key1 = "<table class='table table-sm table-striped' style='font-size:80%;max-width:400px;margin:0px;padding:0px;'><thead class='thead-dark'><th scope='col' style='text-align:center' colspan='2' class='py-1'>";
        String[] s1 = server_response.split(key1);

        //TARIH ILE ECZANE LISTEISNI AYIRIYOR - 0 INDEX TARIH
        String key2 = "Nöbetçi Eczaneleri</a></th></thead><tbody>";
        String[] main=s1[1].split(key2);

        String keysonradan="<a href=\"https://";

        String[] ilkitarih=main[0].split(keysonradan);

        date.setText(ilkitarih[0]+"\nNöbetçi Eczaneler");

        //System.out.println("Tarih : "+ilkitarih[0]);
        //HER ECZANE BASINI AYIRIYOR

        String key3="<tr><td><img style='margin-right:5px;margin-left:0px;' width='24' height='24' src='/resimler/eczane-sm.jpg'></td><td><font color='#000'><b>";

        String[] pharmacies=main[1].split(key3);

        pharmacieslist.clear();

        for (int i=1;i<pharmacies.length;i++){

            PharmacyClass eachpharmacy=new PharmacyClass();

            String singlepharmacy = pharmacies[i];

            //ECZANE ADI ILE OZELLIKLERINI AYIRIYOR 0 INDEX BASINDA ECZANE ISMI - 1 INDEX BASINDA KONUNM KALIYOR
            String key4="</b></font><a href=\"https://www.google.com/mapsdaddr=";

            String[] eczaneadı_devamı=singlepharmacy.split(key4);

            //System.out.println("ECZANE ISMI : "+eczaneadı_devamı[0]);
            eachpharmacy.setName(""+eczaneadı_devamı[0]);

            //KONUM VE DEVAMINI AYIRIYOR  0 INDEX KONUM - DEVAMI DEVAM
            String key5="\" target=\"_blank\"><img src=\"https://www.eczaneler.gen.tr/resimler/tarif-ikon.png\" alt=\"";

            String[] konumvedevamı=eczaneadı_devamı[1].split(key5);

            //System.out.println("konum : "+konumvedevamı[0]);

            //LAT LOG AYIRIYOR
            String key6=",";

            String[] lat_log=konumvedevamı[0].split(key6);

            //System.out.println("LAT : "+lat_log[0]);
            //System.out.println("LOG : "+lat_log[1]);

            LocationClass locationClass=new LocationClass();
            locationClass.setLat(Double.valueOf(lat_log[0]));
            locationClass.setLog(Double.valueOf(lat_log[1]));
            eachpharmacy.setLocatin(locationClass);

            //GEREKSIZ YERLER VE TELEFONDEVAMI
            String key7="Harita Konumu\" class=\"px-2\"></a></td></tr><tr><td><img style='margin-right:5px;margin-left:0px;' width='24' height='24' src='/resimler/telefon.png'></td><td>";

            String[] gereksizyerler_telefonbası=konumvedevamı[1].split(key7);

            String key8="</td><tr class='bottomborder'><td class='align-middle'><img width='24' height='24' src='/resimler/adres.png'></td><td>";

            String[] telefonnumarası_adresvedevamı=gereksizyerler_telefonbası[1].split(key8);

            //System.out.println("telefon numarası : "+telefonnumarası_adresvedevamı[0].replaceAll("[\\W]", ""));
            eachpharmacy.setPhonenumber(""+telefonnumarası_adresvedevamı[0]);
            String key9="</td></tr>";
            String[] adres=telefonnumarası_adresvedevamı[1].split(key9);
            //System.out.println("Adres : "+ adres[0]);

            //EK BILGILER DE CIKTI AMK
            String key10="<span class=\"text-muted font-italic\">";
            String[] solu_adresvebr_sagıgereksiz_bilgi=adres[0].split(key10);

            //System.out.println("Adres : "+ solu_adresvebr_sagıgereksiz_bilgi[0]);

            String key11="<br><span class=\"text-muted\">";
            String[] adress_v2=solu_adresvebr_sagıgereksiz_bilgi[0].split(key11);

            //System.out.println("Adres : "+adress_v2[0]);
            eachpharmacy.setAdress(""+adress_v2[0]);

            pharmacieslist.add(eachpharmacy);

        }

        pharmacyAdapter.notifyDataSetChanged();



    }

    public class Myadapapter extends RecyclerView.Adapter<Myadapapter.MyViewholder> {

        Activity activity;
        ArrayList<PharmacyClass> mpharmacies;

        public Myadapapter(Activity activity, ArrayList<PharmacyClass> mpharmacies) {
            this.activity = activity;
            this.mpharmacies = mpharmacies;
        }


        @Override
        public Myadapapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pharmacy_single_item,
                    parent, false);
            return new Myadapapter.MyViewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Myadapapter.MyViewholder holder, final int position) {

            final PharmacyClass pharmacyClass = mpharmacies.get(position);
            holder.name_tv.setText(pharmacyClass.getName());
            holder.phone_number_tv.setText(pharmacyClass.getPhonenumber());
            holder.adress_tv.setText(pharmacyClass.getAdress());
            holder.pharmacy_phone_carvdiew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneIntent(pharmacyClass);
                }
            });

            holder.pharmacy_seeyheroute_carvdiew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    settherouteIntent(pharmacyClass);
                }
            });

            System.out.println("Distance +"+pharmacyClass.getDistance() );
            if (pharmacyClass.getDistance()!=0){

                holder.pharmacy_range.setVisibility(View.VISIBLE);
                if (pharmacyClass.getDistance()< 1000) {

                    holder.pharmacy_range.setText(((int) (pharmacyClass.getDistance() / 10)) + "0 m");

                } else holder.pharmacy_range.setText(((float) ((int) (pharmacyClass.getDistance() / 100))) / 10 + " km");



            } else holder.pharmacy_range.setVisibility(View.INVISIBLE);

        }


        @Override
        public int getItemCount() {

            return mpharmacies.size();

        }

        public class MyViewholder extends RecyclerView.ViewHolder {

            EditText name_tv;
            EditText phone_number_tv;
            EditText adress_tv;
            TextView pharmacy_range;
            CardView pharmacy_phone_carvdiew,pharmacy_seeyheroute_carvdiew;

            public MyViewholder(View itemView) {
                super(itemView);

                name_tv = itemView.findViewById(R.id.pharmacy_name);
                phone_number_tv = itemView.findViewById(R.id.pharmacy_phone_edx);
                adress_tv = itemView.findViewById(R.id.pharmacy_adress);
                pharmacy_phone_carvdiew = itemView.findViewById(R.id.pharmacy_phone_carvdiew);
                pharmacy_seeyheroute_carvdiew=itemView.findViewById(R.id.pharmacy_seeyheroute_carvdiew);
                pharmacy_range=itemView.findViewById(R.id.pharmacy_range);

            }
        }

    }


    private void settherouteIntent(PharmacyClass pharmacy) {

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+pharmacy.getLocatin().getLat()+","+pharmacy.getLocatin().getLog());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();

        }

        /*
         Uri gmmIntentUri = Uri.parse("geo:"+pharmacy.getLocatin().getLat()+","+pharmacy.getLocatin().getLog()+"?q=test");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);




        String uri = String.format(Locale.getDefault(), "http://maps.google.com/maps?daddr=%f,%f (%s)",
                pharmacy.getLocatin().getLat(),pharmacy.getLocatin().getLog(), pharmacy.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
        */

    }

    private void phoneIntent(PharmacyClass pharmacy) {

        String number=pharmacy.getPhonenumber().replaceAll("[\\W]", "");
        if (!number.startsWith("+9")) number="+9"+number;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

}



