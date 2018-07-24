package com.importio.nitin.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Review reviews[];
    private Context context;

    public ReviewAdapter(@NonNull Context context, Review reviews[]) {
        super(context, 0, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.row_list_review, parent, false);
        }

        Review curReview = getItem(position);
        TextView author_tv = listItem.findViewById(R.id.author);
        TextView review_tv = listItem.findViewById(R.id.review);

        author_tv.setText(curReview.getAuthor());
        review_tv.setText(curReview.getComment());

        return listItem;

    }
}
