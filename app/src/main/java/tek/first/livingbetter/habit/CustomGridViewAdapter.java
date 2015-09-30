package tek.first.livingbetter.habit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.model.InfoCollectedModel;

public class CustomGridViewAdapter extends BaseAdapter {

    private static final String LOG_TAG = CustomGridViewAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<InfoCollectedModel> resultInfoCollection;

    private ImageView imageViewResult;
    private TextView textViewName, textViewDistance, textViewComment;
    private RatingBar ratingBar;

    public CustomGridViewAdapter(Context context, ArrayList<InfoCollectedModel> infoCollections) {
        this.context = context;
        resultInfoCollection = infoCollections;
    }

    @Override
    public int getCount() {
        return resultInfoCollection.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.gridview_habit_detail, null);

        textViewName = (TextView) rowView.findViewById(R.id.detail_name_list);
        textViewDistance = (TextView) rowView.findViewById(R.id.detail_distance_list);
        textViewComment = (TextView) rowView.findViewById(R.id.detail_comment_list);
        imageViewResult = (ImageView) rowView.findViewById(R.id.imageview_details_habit_gridview);
        ratingBar = (RatingBar) rowView.findViewById(R.id.ratingBar_list);

        textViewName.setText(resultInfoCollection.get(position).getName());
        textViewDistance.setText(resultInfoCollection.get(position).getDistance() + "miles");
        textViewComment.setText(resultInfoCollection.get(position).getNumberComment() + "reviews");
        String imageUrl = resultInfoCollection.get(position).getImageUrl();
        Log.v(LOG_TAG, "imageUrl: " + imageUrl);
        if (imageUrl != null && imageUrl.length() > 0) {
            Picasso.with(context).load(imageUrl).into(imageViewResult);
        } else {
            imageViewResult.setImageResource(R.drawable.food);
        }
        ratingBar.setRating(resultInfoCollection.get(position).getRating());

        Gson gson = new Gson();
        final String tests = gson.toJson(resultInfoCollection.get(position));
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("resultObject", tests);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}