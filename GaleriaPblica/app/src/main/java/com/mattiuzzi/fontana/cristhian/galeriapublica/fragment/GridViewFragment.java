package com.mattiuzzi.fontana.cristhian.galeriapublica.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattiuzzi.fontana.cristhian.galeriapublica.R;
import com.mattiuzzi.fontana.cristhian.galeriapublica.adapter.GridAdapter;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageData;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageDataComparator;
import com.mattiuzzi.fontana.cristhian.galeriapublica.model.MainViewModel;
import com.mattiuzzi.fontana.cristhian.galeriapublica.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GridViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridViewFragment extends Fragment {

    private MainViewModel mViewModel;
    private View view;

    public static GridViewFragment newInstance() {
        return new GridViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        GridAdapter gridAdapter = new GridAdapter(new ImageDataComparator());

        LiveData<PagingData<ImageData>> liveData = mViewModel.getPageLv();
        liveData.observe(getViewLifecycleOwner(), new Observer<PagingData<ImageData>>() {
            @Override
            public void onChanged(PagingData<ImageData> objectPagingData) {
                gridAdapter.submitData(getViewLifecycleOwner().getLifecycle(), objectPagingData);
            }
        });

        float w = getResources().getDimension(R.dimen.im_width);
        int numberOfColumns = Utils.calculateNoOfColumns(requireContext(), w);

        RecyclerView rvGallery = view.findViewById(R.id.rvGrid);
        rvGallery.setAdapter(gridAdapter);
        rvGallery.setLayoutManager(new GridLayoutManager(requireContext(), numberOfColumns));
    }
}