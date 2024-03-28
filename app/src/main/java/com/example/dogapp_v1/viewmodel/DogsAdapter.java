package com.example.dogapp_v1.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogapp_v1.R;
import com.example.dogapp_v1.databinding.DogsItemBinding;
import com.example.dogapp_v1.model.DogBreed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable{


    private ArrayList<DogBreed> dogBreeds;
    private ArrayList<DogBreed> dogBreedsCopy ;

    public DogsAdapter(ArrayList<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.dogBreedsCopy = dogBreeds;
    }


    @NonNull
    @Override
    public DogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.dogs_item, parent, false);

        DogsItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.dogs_item,
                        parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DogsAdapter.ViewHolder holder, int position) {
        holder.binding.setDog(dogBreeds.get(position));
        Picasso.get().load(dogBreeds.get(position).getUrl()).into(holder.binding.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().toLowerCase();
                ArrayList<DogBreed> filteredDogs = new ArrayList<DogBreed>();
                if (strSearch.isEmpty()) {
                    filteredDogs.addAll(dogBreedsCopy);
                } else {
                    for (DogBreed dog: dogBreedsCopy) {
                        if(dog.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredDogs.add(dog);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDogs;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dogBreeds = (ArrayList<DogBreed>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public DogsItemBinding binding;


        public ViewHolder(DogsItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DogBreed dog = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed",dog);
                    Navigation.findNavController(itemView).navigate(R.id.detailsFragment, bundle);

                }
            });
        }


    }
}
