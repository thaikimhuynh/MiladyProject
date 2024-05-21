// FeedbackAdapter.java
package com.thaikimhuynh.miladyapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thaikimhuynh.miladyapp.R;
import com.thaikimhuynh.miladyapp.model.Feedback;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;
    boolean showAll;

    public FeedbackAdapter(List<Feedback> feedbackList, boolean showAll) {

        this.feedbackList = feedbackList;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.textViewName.setText(feedback.getUserName());
        holder.rating.setText(String.valueOf(feedback.getRatingStar()));
        holder.textViewFeedback.setText(feedback.getFeedbackContent());
    }

    @Override
    public int getItemCount() {
        // Return only 2 items if the list size is greater than 2
        return showAll ? feedbackList.size() : Math.min(feedbackList.size(), 2);
    }

    public void updateFeedbackList(List<Feedback> filteredList) {
        feedbackList = filteredList;
        notifyDataSetChanged();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView rating;
        TextView textViewFeedback;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.feedbackUserName);
            rating = itemView.findViewById(R.id.rating);
            textViewFeedback = itemView.findViewById(R.id.textViewFeedback);
        }
    }
}
