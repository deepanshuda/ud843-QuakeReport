package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuakeAdapter extends RecyclerView.Adapter<QuakeAdapter.QuakeViewHolder> {

    private List<Quake> quakes;
    private Context context;
    private int mLayout;

    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public QuakeAdapter(List<Quake> quakes, Context context, int mLayout, ListItemClickListener listItemClickListener) {
        this.quakes = quakes;
        this.context = context;
        this.mLayout = mLayout;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public QuakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(mLayout, parent, false);
        return new QuakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuakeViewHolder holder, int position) {
        Quake quake = quakes.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String magniTude = decimalFormat.format(quake.getMagnitude());


        GradientDrawable drawable = (GradientDrawable) holder.quakeMagnitude.getBackground();

        int magColor = getMagnitudeColor(quake.getMagnitude());

        drawable.setColor(magColor);
        holder.quakeMagnitude.setText(magniTude);

        String quakePlace = quake.getPlace();
        String location = "";
        String offset = "";

        if (quakePlace.contains("of")) {
            int offsetIndex = quakePlace.indexOf("of") + 2;
            offset = quakePlace.substring(0, offsetIndex);
            location = quakePlace.substring(offsetIndex + 1, quakePlace.length() - 1);
        }
        else {
            offset = "Near the";
            location = quakePlace;
        }

        holder.quakeOffset.setText(offset);
        holder.quakePlace.setText(location);

        Date dateObj = new Date(quake.getDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormat.format(dateObj);

        dateFormat = new SimpleDateFormat("HH:mm a");
        String timeToDisplay = dateFormat.format(dateObj);

        holder.quakeDate.setText(dateToDisplay);
        holder.quakeTime.setText(timeToDisplay);
    }

    @Override
    public int getItemCount() {
        return quakes.size();
    }

    public class QuakeViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private TextView quakeMagnitude;
        private TextView quakePlace;
        private TextView quakeOffset;
        private TextView quakeDate;
        private TextView quakeTime;

        public QuakeViewHolder(View itemView) {
            super(itemView);

            quakeMagnitude = (TextView) itemView.findViewById(R.id.quake_mag);
            quakeOffset = (TextView) itemView.findViewById(R.id.quake_location_offset);
            quakePlace = (TextView) itemView.findViewById(R.id.quake_location);
            quakeDate = (TextView) itemView.findViewById(R.id.quake_date);
            quakeTime = (TextView) itemView.findViewById(R.id.quake_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(getAdapterPosition());
        }
    }

    private int getMagnitudeColor(Double magnitude) {
        int magnitudeColorResourceId;
        int magFloor = (int) Math.floor(magnitude);

        switch (magFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }

        return ContextCompat.getColor(context, magnitudeColorResourceId);
    }
}
