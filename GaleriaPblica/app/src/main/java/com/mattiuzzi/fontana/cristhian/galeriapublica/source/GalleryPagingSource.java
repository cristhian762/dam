package com.mattiuzzi.fontana.cristhian.galeriapublica.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mattiuzzi.fontana.cristhian.galeriapublica.data.ImageData;
import com.mattiuzzi.fontana.cristhian.galeriapublica.repository.GalleryRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Executors;

public class GalleryPagingSource extends ListenableFuturePagingSource<Integer, ImageData> {

    private final GalleryRepository galleryRepository;
    private final ListeningExecutorService executorService;
    private int initialLoadSize = 0;

    public GalleryPagingSource(@NonNull GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
        this.executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ImageData> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, ImageData>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        final int nextPageNumber = (loadParams.getKey() == null) ? 1 : loadParams.getKey();
        if (loadParams.getKey() == null) {
            initialLoadSize = loadParams.getLoadSize();
        }

        final int offset = calculateOffset(nextPageNumber, loadParams.getLoadSize());

        return executorService.submit(() -> loadImageData(loadParams.getLoadSize(), offset, nextPageNumber));
    }

    private int calculateOffset(int nextPageNumber, int loadSize) {
        if (nextPageNumber == 1) {
            return 0;
        }
        return ((nextPageNumber - 1) * loadSize) + (initialLoadSize - loadSize);
    }

    private LoadResult<Integer, ImageData> loadImageData(int loadSize, int offset, int nextPageNumber) {
        try {
            List<ImageData> imageDataList = galleryRepository.loadImageData(loadSize, offset);
            Integer nextKey = (imageDataList.size() >= loadSize) ? nextPageNumber + 1 : null;
            return new LoadResult.Page<>(imageDataList, null, nextKey);
        } catch (FileNotFoundException e) {
            return new LoadResult.Error<>(e);
        }
    }
}