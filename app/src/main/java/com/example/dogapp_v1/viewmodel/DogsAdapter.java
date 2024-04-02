package com.example.dogapp_v1.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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


public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable {


    private ArrayList<DogBreed> dogBreeds;
    private ArrayList<DogBreed> dogBreedsCopy;

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
                        parent, false);

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
                    for (DogBreed dog : dogBreedsCopy) {
                        if (dog.getName().toLowerCase().contains(strSearch.toLowerCase())) {
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

            /*
               Sự kiện onClick() trong code cũ của bạn có thể bị "tắt" khi sử dụng cả hai sự kiện onClick() và onTouchListener() vì sự kiện onTouchListener() sẽ ghi đè lên sự kiện onClick().
               Khi bạn sử dụng onTouchListener() và GestureDetector để xử lý các cử chỉ chạm và vuốt, nó sẽ ngăn sự kiện onClick() được kích hoạt vì nó xử lý các cử chỉ ngay từ khi chúng bắt đầu.
            */


            /*
                do đó ta thay thế sự OnClick() bằng định nghĩa cử chỉ chạm đơn bằng cách ghi đè onSingleTapConfirmed(MotionEvent e) trong GestureDetector
                Khi có một cử chỉ swipe sang trái, onSwipeLeft() sẽ được gọi và khi có một cử chỉ single tap, onSingleTapMotion() sẽ được gọi.
                Điều này cho phép bạn xử lý cả hai sự kiện (swipe left và single click) trong cùng một setOnTouchListener(new OnSwipeTouchListener()
             */
            itemView.setOnTouchListener(new OnSwipeTouchListener() {
                @Override
                public void onSwipeLeft() {
                    if (binding.layout1.getVisibility() == View.GONE) {
                        binding.layout1.setVisibility(View.VISIBLE);
                        binding.layout2.setVisibility(View.GONE);
                    } else {
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);
                    }
                    super.onSwipeLeft();
                }

                @Override
                public void onSingleTapMotion() {
                    DogBreed dog = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dog);
                    Navigation.findNavController(itemView).navigate(R.id.detailsFragment, bundle);
                }
            });
        }

    }

    public class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());


        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public void onDoubleTapMotion() {
        }

        public void onSingleTapMotion() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onDoubleTapMotion();
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onSingleTapMotion();
                return true;
            }


        }
    }
}
