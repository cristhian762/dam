package com.mattiuzzi.fontana.cristhian.socialnetworkv2.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.mattiuzzi.fontana.cristhian.socialnetworkv2.data.PostData;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository.PostRepository;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.source.PostPagingSource;

import kotlinx.coroutines.CoroutineScope;

public class FeedViewModel extends AndroidViewModel {
    public LiveData<PagingData<PostData>> pageLv;

    public FeedViewModel(@NonNull Application application) {
        super(application);
        PostRepository postRepository = new PostRepository(application);
        PostPagingSource postPagingSource = new PostPagingSource(postRepository);
        Pager<Integer, PostData> pager = new Pager<>(new PagingConfig(10), () -> postPagingSource);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }
}
