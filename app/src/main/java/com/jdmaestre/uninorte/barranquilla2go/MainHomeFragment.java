package com.jdmaestre.uninorte.barranquilla2go;



        import android.app.AlertDialog;
        import android.app.Fragment;
        import android.content.Context;
        import android.content.Intent;
        import android.location.Location;
        import android.os.Bundle;
        import android.os.Debug;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GooglePlayServicesClient;
        import com.google.android.gms.location.LocationClient;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.MapView;
        import com.google.android.gms.maps.MapsInitializer;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.parse.FindCallback;
        import com.parse.GetCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;


        import java.util.List;

/**
 * Created by Jose on 05/11/2014.
 */
public class MainHomeFragment extends Fragment implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    MapView mMapView;
    private GoogleMap googleMap;
    LocationClient loc ;
    public GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loc= new LocationClient(getActivity(),this,this);
        loc.connect();

        View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();




        return rootView;









    }



    public Thread localizar = new Thread(new Runnable() {
        @Override



        public void run(){


             mMap= null;
            Location act = loc.getLastLocation();
            if(act!=null){

                if (mMap == null) {

                      mMap =googleMap;

                }

                if (mMap != null) {

                    LatLng pos = new LatLng(act.getLatitude(),act.getLongitude());
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    mMap.setMyLocationEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            act.getLatitude(), act.getLongitude()), 15));











                        ParseQuery<ParseObject> query = ParseQuery.getQuery("PuntosGPS");


                   query.findInBackground(new FindCallback<ParseObject>(){
                        public void done(List<ParseObject> score, ParseException e) {
                    if (  e==null  ) {
                        ParseObject puntos = new ParseObject("PuntosGPS");

                        for ( int i=0;i<score.size();i++){

                            ParseObject ps= score.get(i);
                            String nom= ps.getString("nombre");
                            String lat= ps.getString("lat");
                            String lon= ps.getString("long");


                            LatLng locs = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

                            Marker myMaker = mMap.addMarker(new MarkerOptions()
                                    .position(locs)
                                    .title(nom)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marks)));

                               loc.disconnect();


                        }
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                Intent intent = new Intent(getActivity(), RestaurantesActivity.class);
                                intent.putExtra("nr",marker.getTitle());
                                startActivity(intent);
                            }
                        });











                                  }


                            else{

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(e.getMessage())
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }

                        }
                        });

                }




            }


        }
        });

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(Bundle bundle) {
        localizar.run();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
