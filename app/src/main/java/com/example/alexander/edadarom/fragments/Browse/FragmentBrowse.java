package com.example.alexander.edadarom.fragments.Browse;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alexander.edadarom.NewItem.AddNewItemActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.adapters.UserAdapter;
import com.example.alexander.edadarom.fragments.Browse.Models.Ad;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentBrowse extends Fragment implements BrowseFragmentContract.View {

    private BrowseFragmentContract.Presenter presenter;
    private View view;
    private RecyclerView recyclerView;
    FloatingActionButton mFab;
    private UserAdapter adapter;
    public static final int NEW_ITEM = 1;
    private List<UserAdsModel> result;
    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    public String key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_browse, container, false);
        result = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.items);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new UserAdapter(result);
        recyclerView.setAdapter(adapter);
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                 startActivityForResult(intent, NEW_ITEM);
            }
        });


        updateList();
        //checkEmpty();
        return view;
    }

    private void updateList() {

        mRootRef.child("new").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                result.add(dataSnapshot.getValue(UserAdsModel.class));
                adapter.notifyDataSetChanged();
                //checkEmpty();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserAdsModel model = dataSnapshot.getValue(UserAdsModel.class);
                int index = getItemIndex(model);
                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserAdsModel model = dataSnapshot.getValue(UserAdsModel.class);
                int index = getItemIndex(model);
                result.remove(index);
                adapter.notifyItemRemoved(index);
                //checkEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getItemIndex(UserAdsModel user) {
        int index = -1;
        for(int i = 0; i < result.size(); i++) {
            if(result.get(i).equals(user)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void removeUser(int position){
        mRootRef.child("new").child(result.get(position).description);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void addDate(ArrayList<Ad> arAds) {

    }
}
