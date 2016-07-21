package com.lz.www.ambrm.ui;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.lz.www.ambrm.R;

import java.util.List;

/**
 * Created by Administrator on 2016-07-19.
 */
public class MapActivity extends Activity {

    TextView txtLocation;
    MapView mapView;

    private Location location;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapAmb);
        txtLocation = (TextView) findViewById(R.id.txtMapLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setXY();



        BaiduMap baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//卫星地图（MAP_TYPE_SATELLITE）
        baiduMap.setTrafficEnabled(true);//交通图
        //baiduMap.setBaiduHeatMapEnabled(true);//热力图
//        baiduMap.setMyLocationEnabled(true);//开启定位图层
//        MyLocationData locationData=new MyLocationData.Builder().accuracy(1).direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
//        baiduMap.setMyLocationData(locationData);
//        BitmapDescriptorFactory.fromResource(R.drawable.qq);
//        MyLocationConfiguration config=new MyLocationConfiguration();
//        baiduMap.setMyLocationData();


    }

    //显示经纬度
    public void setXY() {

        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "no provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                showLocation(location);
            }
        }catch (SecurityException ex){
            ex.printStackTrace();
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1,locationListener);
        }catch (SecurityException ex){
            ex.printStackTrace();
        }


    }

    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void showLocation(Location location){

        String currentPositioin="经度："+location.getLatitude()+"  "+"纬度："+location.getLongitude();
        txtLocation.setText(currentPositioin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(locationManager!=null){
            try {
            locationManager.removeUpdates(locationListener);
            }catch (SecurityException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
