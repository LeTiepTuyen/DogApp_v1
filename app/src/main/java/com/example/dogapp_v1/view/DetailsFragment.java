package com.example.dogapp_v1.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogapp_v1.R;
import com.example.dogapp_v1.databinding.FragmentDetailsBinding;
import com.example.dogapp_v1.model.DogBreed;

public class DetailsFragment extends Fragment {



    private DogBreed dogBreed;
    private FragmentDetailsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dogBreed = (DogBreed) getArguments().getSerializable("dogBreed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding  = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_details,null,false);
        View viewRoot = binding.getRoot();
        binding.setDog(dogBreed);

        return viewRoot;
    }
}