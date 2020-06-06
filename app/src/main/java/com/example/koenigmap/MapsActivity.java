package com.example.koenigmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
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
    private EditText nSearchText;

    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        list = new ArrayList<>();
        listkirh = new ArrayList<Kirh>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
//        mSe
//        searchView = findViewById(R.id.input_search);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mMap);
        mapFragment.getMapAsync(this);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                String location = searchView.getQuery().toString();
//                List<Address> addressList = null;
//
//                if (location != null || !location.equals("")) {
//                    Geocoder geocoder = new Geocoder(MapsActivity.this);
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Address address = addressList.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    // mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//
//
//        });

        mapFragment.getMapAsync(this);


    }




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


        list.add(new Archiitect("Форт №1, Штайн", "Форт был построен в 1875–1879 годах для защиты шоссе на Инстербург, назван в честь прусского государственного деятеля Генриха Карла фон Штайна.", 54.70606519543658, 20.60565233230591, R.drawable.f1));
        list.add(new Archiitect("Форт №1a, Грёбен", "Малый форт, названный в честь прусского военного деятеля, генерала Карла фон дер Гребена.", 54.734873239025106, 20.609128475189213, R.drawable.f1a));
        list.add(new Archiitect("Форт №2, Бронзарт", "Форт построен в 1875–1879 годах для прикрытия дороги на Тильзит и назван в честь военного министра Пауля Бронзарта фон Шелленберга.", 54.748802070052355, 20.601339340209964, R.drawable.f2));
        list.add(new Archiitect("Форт №2а, Барнеков", "Малый форт, главная особенность которого — в цвете используемого кирпича. После войны использовался военными как склад.", 54.75568729921951, 20.571641921997074, R.drawable.f2a));
        list.add(new Archiitect("Форт №3, Король Фдрих-Вильгельм 1", "Форт возведен в 1879 году для обороны шоссе на Кранц, как раньше назывался Зеленоградск.", 54.761692343405734, 20.54651498794556, R.drawable.f3));
        list.add(new Archiitect("Форт №4, Гнайзенау", "Форт получил название в честь военачальника времен наполеоновских войн, реформатора прусской армии Августа Вильгельма фон Гнейзенау.", 54.764156011012226, 20.48784971237183, R.drawable.f4));
        list.add(new Archiitect("Форт №5, Король Фдрих-Вильгельм 3", "Форт построен в 1878 году из особо прочного дважды обожженного кирпича, позже укреплен армированным бетоном.", 54.75239343278619, 20.442960262298588, R.drawable.f5));
        list.add(new Archiitect("Форт №5a, Лендорф", "Малый форт, названный в честь прусского генерала Карла Фридриха Людвига фон Лендорфа, героя наполеоновских войн.", 54.73954355426537, 20.427478551864628, R.drawable.f5a));
        list.add(new Archiitect("Форт №6, Королева Луиза", "Форт построен в 1875 году для прикрытия железной дороги и шоссе на Пиллау, как тогда назывался современный Балтийск.", 54.72226565543917, 20.413584709167484, R.drawable.f6));
        list.add(new Archiitect("Форт №7, Герцог фон Хольштайн", "Форт, построенный на берегу реки Прегель, предназначался для защиты с запада реки и Кёнигсбергского канала.", 54.69391535, 20.387900272220783, R.drawable.f7));
        list.add(new Archiitect("Форт №8, Король Фридрих", "Форт размещается неподалеку от поселка Шоссейный рядом с перекрестком Калининградского шоссе и улицы Парковой.", 54.6647476772742145, 20.43045043945313, R.drawable.f8));
        list.add(new Archiitect("Форт №9, Донна", "Форт получил название в честь старинного дворянского рода фон Дона, занимавшего важное место при прусском королевском дворе.", 54.65327898585188, 20.485038757324222, R.drawable.f9));
        list.add(new Archiitect("Форт №10, Каницт", "Форт построен в 1877–1881 годах для прикрытия транспортных путей на города Цинтен и Домнау.", 54.65064718445805, 20.528469085693363, R.drawable.f10));
        list.add(new Archiitect("Форт №11, Денхофф", "Форт построили в 1877–1881 годах для защиты железной дороги на Инстербург (ныне Черняховск).", 54.65670503795502, 20.567607879638675, R.drawable.f11));
        list.add(new Archiitect("Форт №12, Ойленбург", "Форт построен в 1884 году и назван в честь немецких князей Ойленбургов, одних из древнейших домов Северной Европы.", 54.6713493947706, 20.599536895751957, R.drawable.f12));



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

                String title=marker.getTitle();
                String informations=marker.getSnippet();

                tv1.setText(title);
                tv2.setText(informations);

                int image=0;
                for (Archiitect i:list) {
                    if(title.equals(i.getTitle())) image=i.getImage();
                }
                for (Kirh i:listkirh) {
                    if(title.equals(i.getTite())) image=i.getImg();
                }
                im.setImageResource(image);
                im.setImageResource(image);

                return v;
            }

        });


        mMap.setOnInfoWindowClickListener(this);

        listkirh.add(new Kirh("Кирха Понарт", "Евангелическая кирха Кёнигсберга, построенная в 1897 году, название было дано из-за расположения кирхи в одном из самых старейших пригородов Кёнигсберга — Понарте.", 54.681168799999995, 20.480499081242428, R.drawable.f2));
        listkirh.add(new Kirh("Кирха Луизы", "Кирха строилась в память о королеве Пруссии Луизе. Архитектуру нельзя однозначно отнести к определённому стилю.",
                54.71948960162723, 20.475490093231205, R.drawable.luiz));
        listkirh.add(new Kirh("Бургкирха", "Первая реформаторская (протестантская) кирха, не представляющая собой уменьшенную копию Новой церкви в Гааге", 54.712241841984536, 20.51547110080719, R.drawable.f1));
        listkirh.add(new Kirh("Кафедральный собор", "собор являлся главным католическим храмом города Кёнигсберга, а затем главным лютеранским храмом Пруссии.", 54.70645005, 20.512169623964496, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Христа в Ратсхофе", "Последнее культовое сооружение, возведенное немцами в Кёнигсберге. Находилась в районе Ратсхоф, месте проживания в основном рабочего класса.", 54.7120745, 20.45344437337436, R.drawable.kaf));
        listkirh.add(new Kirh("Розенауская кирха", "Кирха в предместье Кенигсберга Розенау по проекту архитектора Пфлаум, получила статус объекта культурного наследия регионального значения.", 54.683260000000004, 20.53171112208188, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Святого семейства", "Католическая кирха из красного кирпича, являющаяся самым значимым творением Фридриха Хайтманна.", 54.6976962, 20.5096936, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Юдиттен", "Бывшая орденская католическая (а затем — евангелическая) приходская церковь Девы Марии в районе Юдиттен (Кёнигсберг).", 54.715707949999995, 20.4251923, R.drawable.kaf));
        listkirh.add(new Kirh("Россгартенсая кирха", "Кирха с традиционной ориентировкой по сторонам света и высокой церковной башней 84 метра.", 54.711854473339216, 20.49984455108643, R.drawable.kaf));
        listkirh.add(new Kirh("Кирха Лютера", " Последняя уничтоженная кирха Кенигсберга,освещённая в в честь Мартина Лютера. Стиль - поздний ренессанс.", 54.698880135101575, 20.517107248306278, R.drawable.kaf));
        //listkirh.add(new Kirh("Армянская Церковь Святого Степаноса", "Армянская Церковь Святого Степаноса", 54.7471144, 20.523380192129792, R.drawable.kaf));
        listkirh.add(new Kirh("Трагхаймская кирха", "Кирха крестообразной формы, построенная на месте небольшого кирпичного завода по проекту Шультхайса фон Унфрида.", 54.7161023, 20.5055122, R.drawable.f1));
        listkirh.add(new Kirh("Альтштатская  кирха",
                "евангелическая кирха Кёнигсберга, построенная в 1845 году (старое здание 1264 года было снесено)."
                , 54.712958, 20.509386, R.drawable.altshtadt));

        for (Kirh i:listkirh) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(i.getLn(),i.getLt())).title(i.getTite()).snippet(i.getDes()).icon(BitmapDescriptorFactory.fromResource(R.drawable.kirh_marker_icon)));
        }

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

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Загрузка информации", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onMapLongClick(LatLng latLng) {

    }

}


