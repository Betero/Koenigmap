package com.example.koenigmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.slice.SliceItem;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity<listkirh> extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {


    SearchView searchView;
    Location currentLocation;
    private int popupXOffset;
    private int popupYOffset;
    //слушатель, который будет обновлять смещения при изменении размеров окна
    private ViewTreeObserver.OnGlobalLayoutListener infoWindowLayoutListener;
    //контейнер всплывающего окна
    private View infoWindowContainer;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_location_Code = 99;
    private Marker Somewhere;
    private int markerclicked;
    private ArrayList<Archiitect> list;
    private ArrayList<Kirh> listkirh;
    private String TAG;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        list = new ArrayList<>();
        listkirh = new ArrayList<Kirh>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        searchView = findViewById(R.id.sv_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    // mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

        mapFragment.getMapAsync(this);

//        client = LocationServices.getFusedLocationProviderClient(this);
//
//        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocation();
//        } else {
//            //When permission denied
//            //Request permission
//            ActivityCompat.requestPermissions(MapsActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }

    }

//    private void getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Task<Location> task = client.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(final Location location) {
//
//                if (location != null){
//                  supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                      @Override
//                      public void onMapReady(GoogleMap googleMap) {
//                          LatLng latLng = new LatLng(location.getLongitude(), (location.getLatitude()));
//                          //Create marker options
//                          MarkerOptions options = new MarkerOptions().position(latLng)
//                                  .title("Я здесь, черти");
//                          //Zoom
//                          googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                          //Add marker
//                          googleMap.addMarker(options);
//                      }
//                  });
//                }
//            }
//        });
//
//
//    }


    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(54.70645005, 20.512169623964496)).zoom(10).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(54.70645005, 20.512169623964496)));


        list.add(new Archiitect("Форт №1, Штайн", "где этот текст?", 54.70606519543658, 20.60565233230591, R.drawable.f1));
        list.add(new Archiitect("Форт №1a, Грёбен", "что-то", 54.734873239025106, 20.609128475189213, R.drawable.f1a));
        list.add(new Archiitect("Форт №2, Бронзарт", "что-то", 54.748802070052355, 20.601339340209964, R.drawable.f2));
        list.add(new Archiitect("Форт №2а, Барнеков", "что-то", 54.75568729921951, 20.571641921997074, R.drawable.f2));
        list.add(new Archiitect("Форт №3, Король Фдрих-Вильгельм 1", "что-то", 54.761692343405734, 20.54651498794556, R.drawable.f2));
        list.add(new Archiitect("Форт №4, Гнайзенау", "что-то", 54.764156011012226, 20.48784971237183, R.drawable.f2));
        list.add(new Archiitect("Форт №5, Король Фдрих-Вильгельм 3", "что-то", 54.75239343278619, 20.442960262298588, R.drawable.f2));
        list.add(new Archiitect("Форт №5a, Лендорф", "что-то", 54.73954355426537, 20.427478551864628, R.drawable.f2));
        list.add(new Archiitect("Форт №6, Королева Луиза", "что-то", 54.72226565543917, 20.413584709167484, R.drawable.f2));
        list.add(new Archiitect("Форт №7, Герцог фон Хольштайн", "что-то", 54.69391535, 20.387900272220783, R.drawable.f2));
        list.add(new Archiitect("Форт №8, Король Фридрих", "что-то", 54.6647476772742145, 20.43045043945313, R.drawable.f2));
        list.add(new Archiitect("Форт №9, Донна", "что-то", 54.65327898585188, 20.485038757324222, R.drawable.f2));
        list.add(new Archiitect("Форт №10, Каницт", "что-то", 54.65064718445805, 20.528469085693363, R.drawable.f2));
        list.add(new Archiitect("Форт №11, Денхофф", "что-то", 54.65670503795502, 20.567607879638675, R.drawable.f2));
        list.add(new Archiitect("Форт №12, Ойленбург", "что-то", 54.6713493947706, 20.599536895751957, R.drawable.f2));
      //  list.add(new Archiitect("Форт №10, Каницт", "что-то", 54.65064718445805, 20.528469085693363, R.drawable.f2));


        for (Archiitect i:list) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(i.getLon(),i.getLat())).title(i.getTitle()).snippet(i.getDesc()).icon(BitmapDescriptorFactory.fromResource(R.drawable.i_fort)));
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_window, null);
                ImageView im = (ImageView) v.findViewById(R.id.imageView1);
                TextView tv1 = (TextView) v.findViewById(R.id.textView1);
                TextView tv2 = (TextView) v.findViewById(R.id.textView2);
                TextView tv3 = (TextView) v.findViewById(R.id.tosite);
                //Button bt = (Button) v.findViewById(R.id.butmap);
                String title=marker.getTitle();
                String informations=marker.getSnippet();

                tv1.setText(title);
                tv2.setText(informations);

//                                          if(onMarkerClick(marker)==true && markerclicked==1){
                int image=0;
                for (Archiitect i:list) {
                    if(title.equals(i.getTitle())) image=i.getImage();
                }
                for (Kirh i:listkirh) {
                    if(title.equals(i.getTite())) image=i.getImg();
                }
                im.setImageResource(image);
                im.setImageResource(image);
//                                          }

                return v;
            }

        });


        mMap.setOnInfoWindowClickListener(this);

        listkirh.add(new Kirh("Кирха Понарт", "где этот текст?", 54.681168799999995, 20.480499081242428, R.drawable.f2));
        listkirh.add(new Kirh("Кирха Луизы", "В 1901 г., по проекту архитекторов Хайтмана и Краха была сооружена мемориальная кирха в честь королевы Луизы.",
                54.71948960162723, 20.475490093231205, R.drawable.luiz));
        listkirh.add(new Kirh("Бургкирха", "где этот текст?", 54.712241841984536, 20.51547110080719, R.drawable.f1));
        listkirh.add(new Kirh("Кафедральный собор", "Основан в 1333 году", 54.70645005, 20.512169623964496, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Христа в Ратсхофе", "что-то", 54.7120745, 20.45344437337436, R.drawable.kaf));
        listkirh.add(new Kirh("Розенауская кирха", "что-то", 54.683260000000004, 20.53171112208188, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Святого семейства", "что-то", 54.6976962, 20.5096936, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Юдиттен", "Кирха Юдиттен", 54.715707949999995, 20.4251923, R.drawable.kaf));
        listkirh.add(new Kirh("Россгартенсая кирха", "Россгартенсая кирха", 54.711854473339216, 20.49984455108643, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Лютера", "Кирха Лютера", 54.698880135101575, 20.517107248306278, R.drawable.kaf));
        listkirh.add(new Kirh("Армянская Церковь Святого Степаноса", "Армянская Церковь Святого Степаноса", 54.7471144, 20.523380192129792, R.drawable.kaf));
        listkirh.add(new Kirh("Трагхаймская кирха", "Трагхаймская кирха", 54.7161023, 20.5055122, R.drawable.frida));
        listkirh.add(new Kirh("Альтштатская  кирха",
                "евангелическая кирха Кёнигсберга, построенная в 1845 году (старое здание 1264 года было снесено). Немецкое название было дано по географическому расположению кирхи в одном из трёх первых городов Кёнигсберга — Альтштадте."
                , 54.712958, 20.509386, R.drawable.altshtadt));

        for (Kirh i:listkirh) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(i.getLn(),i.getLt())).title(i.getTite()).snippet(i.getDes()).icon(BitmapDescriptorFactory.fromResource(R.drawable.kirh_marker_icon)));
        }

//        Task<Location> task = client.getLastLocation();
//
//        Location location = null;
//        Location location = locationRequest.equals();
//        LatLng latLng = new LatLng(i.getLongitude(), (location.getLatitude()));
//                          //Create marker options
//        MarkerOptions options = new MarkerOptions().position(latLng)
//                .title("Я здесь, черти");
//        //Zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//        //Add marker
//        mMap.addMarker(options);
    }



    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(Somewhere))
        {
            markerclicked=1;
            return true;
        }
        return false;
    }


    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_location_Code);
            }
            return false;
        } else {
            return true;
        }


//        MarkerOptions options = new MarkerOptions().position().title("Я здесь, черти");
//        //Zoom
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
//        //Add marker
//        mMap.addMarker(options);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Загрузка информации", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == 44){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                //When  ermission granted
//                // Call method
//                getCurrentLocation();
//            }
//        }
//    }
}


