package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.test.MainActivity.folderList;
import static com.example.test.MainActivity.videoFiles;


public class HomeFragment extends Fragment {


    FolderAdapter folderAdapter;
    RecyclerView recyclerView;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.folderRV);
        if (folderList !=null && folderList.size() > 0){
            folderAdapter = new FolderAdapter(folderList,videoFiles,getContext());
            recyclerView.setAdapter(folderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        }
        return view;
    }
}