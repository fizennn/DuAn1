package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.FavouriteDishAdapter;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private FavouriteDishAdapter adapter;
    private List<ThucDon> favouriteDishList;
    private LottieAnimationView loading;
    private View viewLoad,empty;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

        empty = view.findViewById(R.id.empty);




        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rycFavouriteDish);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the list
        favouriteDishList = new ArrayList<>();

        // Set up the adapter
        adapter = new FavouriteDishAdapter(favouriteDishList, getContext());
        recyclerView.setAdapter(adapter);

        // Set up Firebase Database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NhaHang").child("FavouriteDish").child(userId);


        loading();

        // Retrieve the data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouriteDishList.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ThucDon thucDon = snapshot.getValue(ThucDon.class);
                    if (thucDon != null) {
                        favouriteDishList.add(thucDon); // Add the data to the list
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                loaded();

                if (favouriteDishList.size()==0){
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }
}