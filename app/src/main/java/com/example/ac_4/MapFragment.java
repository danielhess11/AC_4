package com.example.ac_4;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_maps);
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = new ArrayList<>();

                if (location != null || !location.equals("")) {
                    // Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Set the global variables in MainActivity
                    ((MainActivity) getActivity()).setLatitude(address.getLatitude());
                    ((MainActivity) getActivity()).setLongitude(address.getLongitude());

                    // Create WeatherFragment
                    WeatherFragment weatherFragment = new WeatherFragment();


                    // Update the map view
                    supportMapFragment.getMapAsync(googleMap -> {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    });
                }
                return false;
            }




            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        googleMap.clear();
                        googleMap.addMarker(markerOptions);

                        // Update the latitude and longitude values in MainActivity
                        ((MainActivity) getActivity()).setLatitude(latLng.latitude);
                        ((MainActivity) getActivity()).setLongitude(latLng.longitude);

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng, 10
                                // Set the latitude and longitude values


                        ));
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}