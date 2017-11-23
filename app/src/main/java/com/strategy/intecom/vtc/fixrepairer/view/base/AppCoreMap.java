package com.strategy.intecom.vtc.fixrepairer.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelAddress;
import com.strategy.intecom.vtc.fixrepairer.model.VtcModelOrder;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GMapV2Direction;
import com.strategy.intecom.vtc.fixrepairer.utils.map.GPSTracker;

import org.w3c.dom.Document;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/2/16.
 */
public class AppCoreMap extends AppCoreAPI {

    private GoogleMap gMap;
    private MapView mapview;
    private LatLng point;
    private Map<String, VtcModelOrder> listOrder = new HashMap<>();

    public void onMapInitializer(Activity activity) {

        try {
            MapsInitializer.initialize(activity);
        } catch (Exception e) {
            AppCore.showLog("onCreateView Exception : " + e.getMessage());
        }
    }

    public void onMapResume() {
        try {
            if (mapview != null)
                mapview.onResume();
        } catch (NullPointerException e) {
            AppCore.showLog("onResume NullPointerException : " + e.getMessage());
        }
    }

    public void onMapDestroy() {
        try {
            if (mapview != null)
                mapview.onDestroy();
        } catch (NullPointerException e) {
            AppCore.showLog("onDestroy NullPointerException : " + e.getMessage());
        }
    }

    public void onMapLowMemory() {
        try {
            if (mapview != null)
                mapview.onLowMemory();
        } catch (NullPointerException e) {
            AppCore.showLog("onLowMemory NullPointerException : " + e.getMessage());
        }
    }

    public void onMapClear() {
        try {
            if (gMap != null)
                gMap.clear();
        } catch (NullPointerException e) {
            AppCore.showLog("onLowMemory NullPointerException : " + e.getMessage());
        }
    }

    public void onMapinitMapView(View view, @Nullable Bundle savedInstanceState, Activity activity, double[] location) {
        onMapinitMapView(view, savedInstanceState, activity, location, "", false);
    }

    public void onMapinitMapView(View view, @Nullable Bundle savedInstanceState, Activity activity, double[] location, String address, boolean isViewMark) {
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)) {
            case ConnectionResult.SUCCESS:
                try {
                    mapview = (MapView) view.findViewById(R.id.map);
                    if (mapview != null) {
                        try {
                            mapview.onCreate(savedInstanceState);
                            LatLng point = new LatLng(location[0], location[1]);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 15);
                            gMap = mapview.getMap();
                            gMap.animateCamera(cameraUpdate);
                            if (isViewMark) {
                                gMap.addMarker(new MarkerOptions().position(point).title(address));
                            }
                        } catch (NullPointerException e) {
                            AppCore.showLog("onCreateView NullPointerException : " + e.getMessage());
                        }
                    }
                } catch (Resources.NotFoundException e) {
                    AppCore.showLog("onCreateView Resources.NotFoundException : " + e.getMessage());
                }

                break;
            case ConnectionResult.SERVICE_MISSING:
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                break;
            default:
        }
    }

    protected void goToMyLocation(Activity activity) {
        if(gMap != null && mapview != null) {
            GPSTracker gpsTracker = getGpsTracker(activity);
            LatLng target = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 15);
            gMap = mapview.getMap();
            gMap.animateCamera(cameraUpdate);
        }
    }

    public void initAddMyMarker(double[] location, String title, String desscription) {
        if (gMap != null) {
            LatLng point = new LatLng(location[0], location[1]);

            MarkerOptions marker = new MarkerOptions().position(point).title(title).snippet(desscription);
            gMap.addMarker(marker);
        }
    }

    public void initActionDragMap() {
        if (gMap != null && mapview != null) {
            gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    cmdOnClickMap(Boolean.FALSE);
                }
            });


            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    cmdOnClickMap(Boolean.TRUE);
                }
            });

            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    cmdOnClickViewDetail();
                }
            });

        }
    }

    public void initAddGuestMarker(double[] location, String title, String desscription) {
        if (gMap != null) {
            LatLng point = new LatLng(location[0], location[1]);

            MarkerOptions marker = new MarkerOptions().position(point).title(title).snippet(desscription);
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_mark));
            gMap.addMarker(marker);
        }
    }

    public void initShowDistance(LatLng sourcePosition, String myAddress, LatLng destPosition, String address, String description) {
        if (gMap != null) {
            gMap.addMarker(new MarkerOptions().position(sourcePosition).title(myAddress));

            MarkerOptions marker = new MarkerOptions().position(destPosition).title(address);
            marker.snippet(CalculationByDistance(sourcePosition, destPosition));
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_mark));
            gMap.addMarker(marker);

            new ReadTask().execute(sourcePosition, destPosition);
        }
    }

    Polyline polyline;
    public void initShowDrawStreest(LatLng sourcePosition, LatLng destPosition) {
        new ReadTask().execute(sourcePosition, destPosition);
    }

    private class ReadTask extends AsyncTask<LatLng, Void, Document> {
        GMapV2Direction md;

        @Override
        protected Document doInBackground(LatLng... sourcePosition) {
            md = new GMapV2Direction();
            Document doc = md.getDocument(sourcePosition[0], sourcePosition[1], GMapV2Direction.MODE_WALKING);

            return doc;
        }

        @Override
        protected void onPostExecute(Document result) {
            super.onPostExecute(result);

            if(polyline != null){
                polyline.remove();
            }

            ArrayList<LatLng> directionPoint = md.getDirection(result);
            PolylineOptions rectLine = new PolylineOptions().width(5).color(
                    Color.RED);

            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }

            if (gMap != null) {
                polyline = gMap.addPolyline(rectLine);

            }
        }
    }

    public void showLocations(Cursor c) {
        MarkerOptions markerOptions = null;
        LatLng position = null;
        if (gMap != null) {
            gMap.clear();
        }
        if (c != null) {
            while (c.moveToNext()) {
                markerOptions = new MarkerOptions();
                position = new LatLng(Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)));
                markerOptions.position(position);
                markerOptions.title(c.getString(0));
                if (gMap != null) {
                    gMap.addMarker(markerOptions);
                }
            }
            AppCore.showLog("Cursor Count : " + c.getCount());
        } else {
            AppCore.showLog("Cursor null");
        }
        if (position != null) {
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            if (gMap != null) {
                gMap.animateCamera(cameraPosition);
            }
        }
    }


    protected void initActionPinEvent() {
        if (gMap != null) {
            gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    cmdOnGetLocation(cameraPosition.target);
                }
            });
        }
    }

    public void initShowAllJob(final VtcModelOrder vtcModelOrder) {
        if (gMap != null && vtcModelOrder != null && vtcModelOrder.getAddress() != null) {

            listOrder.put(vtcModelOrder.getAddress().getName(), vtcModelOrder);

            GPSTracker gpsTracker = getGpsTracker(getmActivity());

            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            LatLng position = new LatLng(Double.parseDouble(vtcModelOrder.getAddress().getSlat()), Double.parseDouble(vtcModelOrder.getAddress().getSlong()));

            MarkerOptions marker = new MarkerOptions().position(position).title(vtcModelOrder.getAddress().getName()).snippet(CalculationByDistance(latLng, position));
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location_mark));
            gMap.addMarker(marker);

            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    if (listOrder != null) {
                        VtcModelOrder vtcModelOrder = listOrder.get(marker.getTitle());

                        cmdOnClickMarkerMap(vtcModelOrder);

                    }
                    return false;
                }
            });
        }
    }

    protected VtcModelAddress getAddressFromLocation(Context context, LatLng destPosition) {
        try {
            if(context == null){
                return null;
            }
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(destPosition.latitude, destPosition.longitude, 1);
            if (addresses != null && addresses.size() > 0) {

                Address address = addresses.get(0);

                String straddress = "";

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                    straddress += address.getAddressLine(i);

                    if(i < (address.getMaxAddressLineIndex() - 1)){
                        straddress += ", ";
                    }
                }
                if(straddress.isEmpty()){

                    String road = address.getThoroughfare();

                    if(road != null && !road.isEmpty() && !road.equals("null")){
                        straddress += road;
                    }

                    String district = address.getLocality();

                    if(district != null && !district.isEmpty() && !district.equals("null")){
                        straddress += ", " + district;
                    }

                    String area = address.getAdminArea();

                    if(area != null && !area.isEmpty() && !area.equals("null")){
                        straddress += ", " + area;
                    }
                }

                VtcModelAddress vtcModelAddress = new VtcModelAddress();

                vtcModelAddress.setAddress(straddress);
                vtcModelAddress.setLongitude(address.getLongitude());
                vtcModelAddress.setLatitude(address.getLatitude());

                return vtcModelAddress;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = Math.round(valueResult);
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        BigDecimal bigDecimal = new BigDecimal(valueResult);

        AppCore.showLog("Radius Value", "" + bigDecimal.setScale(1, BigDecimal.ROUND_CEILING).toString() + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

//        return Radius * c;

        String distance = String.valueOf(bigDecimal.setScale(1, BigDecimal.ROUND_CEILING).toString() + " Km");

        return distance;
    }
}
