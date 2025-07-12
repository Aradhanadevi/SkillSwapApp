package anjali.learning.skilshare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import anjali.learning.skilshare.Adapter.CourseAdapter;
import anjali.learning.skilshare.model.Course;

public class ExploreSkilCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;
    private List<Course> allCourses;

    private DatabaseReference databaseReference;

    private EditText etSearchBar;
    private ImageView ivSearchIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_skil_courses, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        allCourses = new ArrayList<>();
        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);

        etSearchBar = view.findViewById(R.id.etExploreSearchBar);
        ivSearchIcon = view.findViewById(R.id.ivExploreSearchIcon);

        databaseReference = FirebaseDatabase.getInstance().getReference("courses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allCourses.clear();
                for (DataSnapshot courseSnap : snapshot.getChildren()) {
                    Course course = courseSnap.getValue(Course.class);
                    if (course != null) {
                        allCourses.add(course);
                    }
                }
                courseList.clear();
                courseList.addAll(allCourses);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load courses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 🔍 Search logic
//        ivSearchIcon.setOnClickListener(v -> {
//            String query = etSearchBar.getText().toString().trim();
//            if (TextUtils.isEmpty(query)) {
//                // Show all if empty
//                courseList.clear();
//                courseList.addAll(allCourses);
//            } else {
//                courseList.clear();
//                for (Course course : allCourses) {
//                    if (course.getCourseName().toLowerCase().contains(query.toLowerCase()) ||
//                            (course.getSkills() != null && course.getSkills().toLowerCase().contains(query.toLowerCase()))) {
//                        courseList.add(course);
//                    }
//                }
//                if (courseList.isEmpty()) {
//                    Toast.makeText(getContext(), "No course found", Toast.LENGTH_SHORT).show();
//                }
//            }
//            adapter.notifyDataSetChanged();
//        });

        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCourses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });




        return view;
    }
    private void filterCourses(String query) {
        courseList.clear();
        if (TextUtils.isEmpty(query)) {
            courseList.addAll(allCourses);
        } else {
            for (Course course : allCourses) {
                if (course.getCourseName().toLowerCase().contains(query.toLowerCase()) ||
                        (course.getSkills() != null && course.getSkills().toLowerCase().contains(query.toLowerCase()))) {
                    courseList.add(course);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
