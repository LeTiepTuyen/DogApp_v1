package com.example.dogapp_v1.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogapp_v1.R;
import com.example.dogapp_v1.model.DogBreed;
import com.example.dogapp_v1.viewmodel.DogsAdapter;
import com.example.dogapp_v1.viewmodel.DogsApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ListFragment extends Fragment {

    private DogsApiService  dogsApiService;
    private RecyclerView rvDogs;
    private ArrayList<DogBreed> dogBreeds = new ArrayList<DogBreed>();;
    private DogsAdapter dogsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDogs  = view.findViewById(R.id.rv_dogs);

        //Set layout cho Recyleview rvContacts:
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvDogs.setLayoutManager(linearLayoutManager);

        //Do du lieu len recyleview
        dogsAdapter = new DogsAdapter(dogBreeds);
        rvDogs.setAdapter(dogsAdapter);
        dogsAdapter.notifyDataSetChanged();



        dogsApiService = new DogsApiService();
        dogsApiService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<DogBreed> dogBreeds1) {
                        Log.d("DEBUG","Success");
                        for (DogBreed dogs: dogBreeds1) {
                            dogBreeds.add(dogs);
                            dogsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d("DEBUG","Fail" + e.getMessage());


                    }
                });
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                // Merely log undeliverable exceptions
                Log.d("DEBUG", e.getMessage());
            } else {
                // Forward all others to current thread's uncaught exception handler
                Thread thread = Thread.currentThread();
                thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
            }
        });
    }

}