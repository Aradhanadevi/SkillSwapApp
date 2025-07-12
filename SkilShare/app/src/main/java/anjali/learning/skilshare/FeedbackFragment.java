package anjali.learning.skilshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FeedbackFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText feedbackInput = view.findViewById(R.id.feedbackInput);
        Button submitBtn = view.findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String feedback = feedbackInput.getText().toString().trim();

            if (!feedback.isEmpty()) {
                Toast.makeText(getContext(), "Thank you for your feedback!\nRating: " + rating, Toast.LENGTH_SHORT).show();
                feedbackInput.setText("");
                ratingBar.setRating(0);
            } else {
                Toast.makeText(getContext(), "Please enter feedback before submitting.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
