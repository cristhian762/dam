package com.mattiuzzi.fontana.cristhian.galeriapublica.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.mattiuzzi.fontana.cristhian.galeriapublica.R;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageData;
import com.mattiuzzi.fontana.cristhian.galeriapublica.repository.GalleryRepository;
import com.mattiuzzi.fontana.cristhian.galeriapublica.source.GalleryPagingSource;

import kotlinx.coroutines.CoroutineScope;

public class MainViewModel extends AndroidViewModel {

    int navigationOpSelected = R.id.gridViewOp;
    LiveData<PagingData<ImageData>> pageLv;

    public MainViewModel(@NonNull Application application) {
        super(application);

        // Configurando biblioteca Paging3
        GalleryRepository galleryRepository = new GalleryRepository(application);
        GalleryPagingSource galleryPagingSource = new GalleryPagingSource(galleryRepository);
        Pager<Integer, ImageData> pager = new Pager<>(new PagingConfig(10), () -> galleryPagingSource);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }

    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }

    public LiveData<PagingData<ImageData>> getPageLv() {
        return pageLv;
    }
}

