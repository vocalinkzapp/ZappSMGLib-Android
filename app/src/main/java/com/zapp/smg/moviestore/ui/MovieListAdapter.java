package com.zapp.smg.moviestore.ui;

import com.squareup.picasso.Picasso;
import com.zapp.library.smg.core.model.CurrencyAmount;
import com.zapp.smg.moviestore.R;
import com.zapp.smg.moviestore.model.Movie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Custom list adatpter for movie list.
 * @author msagi
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    public interface OnPurchaseListener {
        void onPurchase(UUID itemUuid);
    }

    private static final String TAG = MovieListAdapter.class.getSimpleName();

    private final List<Movie> mMovies;

    private final OnPurchaseListener mOnPurchaseListener;

    /**
     * List of visible view holders.
     */
    private final LinkedList<ViewHolder> mVisibleViewHolders = new LinkedList<>();

    public MovieListAdapter(@NonNull final OnPurchaseListener onPurchaseListener) {
        mMovies = new ArrayList<>();
        mOnPurchaseListener = onPurchaseListener;
    }

    public void setMovies(@NonNull final Movie[] movieList) {
        mMovies.clear();
        mMovies.addAll(Arrays.asList(movieList));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Log.e(TAG, "onCreateViewHolder");
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_movie_list_card, parent, /* attachToRoot */ false);

        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: position: " + position);

        final Movie movie = mMovies.get(position);

        final Context context = holder.mMovieTitleTextView.getContext();

        Picasso.with(context)
                .load(movie.getWallpaperImageUrl())
//                .resizeDimen(R.dimen.movie_wallpaper_width, R.dimen.movie_wallpaper_height)

                .fit()
               // .transform(new BlurTransform(context))
                .into(holder.mMovieWallpaperImageView);

        Picasso.with(holder.mMoviePackshotImageView.getContext())
                .load(movie.getPackshotImageUrl())
                .fit()
                .into(holder.mMoviePackshotImageView);

        final String priceString = String.format("£%s.%s", movie.getPrice() / 100, movie.getPrice() % 100);

        holder.mPurchaseButton.setText(context.getString(R.string.movie_card_purchase_button_text_with_price, priceString));
        holder.mPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mOnPurchaseListener.onPurchase(movie.getId());
            }
        });

        holder.mMovieTitleTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onViewAttachedToWindow(final ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //add holder to the list of visible holders
        mVisibleViewHolders.add(holder);

        holder.mTotalScrolling = 0;
    }

    @Override
    public void onViewDetachedFromWindow(final ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //remove holder from the list of visible holders
        mVisibleViewHolders.remove(holder);
    }

    /**
     * Apply scroll event to the list items.
     * @param dy The deltaY of scrolling.
     * @see #onViewAttachedToWindow(ViewHolder)
     * @see #onViewDetachedFromWindow(ViewHolder)
     */
    public void onScroll(final int dy) {
 //       Log.e(TAG, "onScroll: dy: " + dy + ", visible holders: " + mVisibleViewHolders.toString());
        for (final ViewHolder viewHolder : mVisibleViewHolders) {
            //forward scroll event to view holders
            viewHolder.onScroll(dy);
        }
    }

    /**
     * Custom view holder for movie list adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The movie packshot image.
         */
        private ImageView mMoviePackshotImageView;

        /**
         * The movie title.
         */
        private TextView mMovieTitleTextView;

        /**
         * The movie wallpaper image.
         */
        private ImageView mMovieWallpaperImageView;

        /**
         * The purchase button for the movie.
         */
        private Button mPurchaseButton;

        private int mTotalScrolling = 0;

        /**
         * Create new instance.
         * @param itemView The root view of the movie list item layout.
         */
        public ViewHolder(final View itemView) {
            super(itemView);
            mMoviePackshotImageView = (ImageView) itemView.findViewById(R.id.movie_packshot_image);
            mMovieTitleTextView = (TextView) itemView.findViewById(R.id.movie_title_text);
            mMovieWallpaperImageView = (ImageView) itemView.findViewById(R.id.movie_background_image);

            mPurchaseButton = (Button) itemView.findViewById(R.id.purchase_button);
        }

        public void onScroll(final int dy) {
            mTotalScrolling += dy;
            float translationY = mTotalScrolling / 10;
//            ViewCompat.setTranslationY(mMovieWallpaperImageView, translationY);
        }
    }
}
