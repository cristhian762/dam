package com.mattiuzzi.fontana.cristhian.socialnetworkv2.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.data.PostData;
import com.mattiuzzi.fontana.cristhian.socialnetworkv2.repository.PostRepository;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.Executors;

public class PostPagingSource extends ListenableFuturePagingSource<Integer, PostData> {

    private final PostRepository postRepository;
    private final ListeningExecutorService executorService;
    private int initialLoadSize = 0;

    public PostPagingSource(@NonNull PostRepository postRepository) {
        this.postRepository = postRepository;
        this.executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, PostData> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, PostData>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        final int nextPageNumber = (loadParams.getKey() == null) ? 1 : loadParams.getKey();
        if (loadParams.getKey() == null) {
            initialLoadSize = loadParams.getLoadSize();
        }

        final int offset = calculateOffset(nextPageNumber, loadParams.getLoadSize());

        return executorService.submit(() -> loadPostData(loadParams.getLoadSize(), offset, nextPageNumber));
    }

    private int calculateOffset(int nextPageNumber, int loadSize) {
        if (nextPageNumber == 1) {
            return 0;
        }
        return ((nextPageNumber - 1) * loadSize) + (initialLoadSize - loadSize);
    }

    private LoadResult<Integer, PostData> loadPostData(int loadSize, int offset, int nextPageNumber) {
        try {
            List<PostData> postDataList = postRepository.loadPostData(loadSize, offset);
            Integer nextKey = (postDataList.size() >= loadSize) ? nextPageNumber + 1 : null;
            return new LoadResult.Page<>(postDataList, null, nextKey);
        } catch (FileNotFoundException e) {
            return new LoadResult.Error<>(e);
        }
    }
}