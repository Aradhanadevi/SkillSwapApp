package anjali.learning.skilshare;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.io.Serializable;
import java.util.ArrayList;

import anjali.learning.skilshare.Adapter.CourseAdapter;
import anjali.learning.skilshare.Adapter.FeaturedCourseAdapter;
import anjali.learning.skilshare.model.Course;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerCourses;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;

    private ViewPager2 viewPagerFeatured;
    private ArrayList<Course> featuredList;
    private FeaturedCourseAdapter featuredAdapter;

    private TextView featuredTitle, featuredDescription, featuredDuration;

    private Handler carouselHandler = new Handler(Looper.getMainLooper());
    private int carouselIndex = 0;

    private EditText etSearchBar;
    private ImageView ivSearchIcon;
    private FloatingActionButton fabBot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        featuredTitle = view.findViewById(R.id.featuredCourseTitle);
        featuredDescription = view.findViewById(R.id.featuredCourseDescription);
        featuredDuration = view.findViewById(R.id.featuredCourseDuration);

        viewPagerFeatured = view.findViewById(R.id.viewPagerFeatured);

        recyclerCourses = view.findViewById(R.id.recyclerCourses);
        recyclerCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList);
        recyclerCourses.setAdapter(adapter);

        featuredList = new ArrayList<>();
        featuredAdapter = new FeaturedCourseAdapter(
                featuredList,
                getContext(),
                course -> updateFeaturedDetails(course)
        );
        viewPagerFeatured.setAdapter(featuredAdapter);

        etSearchBar = view.findViewById(R.id.etSearchBar);
        ivSearchIcon = view.findViewById(R.id.ivSearchIcon);
        fabBot = view.findViewById(R.id.fabBot);

        fabBot.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String email = userSnap.child("email").getValue(String.class);
                            if (email != null && email.equals(user.getEmail())) {
                                String name = userSnap.child("name").getValue(String.class);
                                String skills = userSnap.child("skills").getValue(String.class);

                                Bundle bundle = new Bundle();
                                bundle.putString("name", name);
                                bundle.putString("skills", skills);

                                ChatBottomSheet bottomSheet = new ChatBottomSheet();
                                bottomSheet.setArguments(bundle);
                                bottomSheet.show(getParentFragmentManager(), "chat");
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // You can log or toast error here
                    }
                });
            }
        });


        setupSearchBot();  // 🔍 Setup search icon to perform course search
        loadFeaturedCourses();  // 🎯 Load featured
        loadUserSkillsAndFilterCourses();  // 🎯 Load skill-based recommendations

        return view;
    }

    // 🔍 Setup Search Click (Updated)
    private void setupSearchBot() {
        ivSearchIcon.setOnClickListener(v -> {
            String query = etSearchBar.getText().toString().trim();
            if (query.isEmpty()) {
                etSearchBar.setError("Type something");
                return;
            }
            searchCourses(query);
        });
    }

    // 🔍 Firebase Search Implementation
    private void searchCourses(String query) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses");

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Course> resultList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Course course = snap.getValue(Course.class);
                    if (course != null && course.getCourseName() != null &&
                            course.getCourseName().toLowerCase().contains(query.toLowerCase())) {
                        resultList.add(course);
                    }
                }

                if (resultList.isEmpty()) {
                    Toast.makeText(getContext(), "No course found", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("courses", resultList);

                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, searchFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFeaturedDetails(Course course) {
        featuredTitle.setText(course.getCourseName());
        featuredDescription.setText(course.getDescription());
        featuredDuration.setText(course.getNoofvideos() + " videos");
    }

    private void loadUserSkillsAndFilterCourses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String currentUserEmail = user.getEmail();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String email = userSnap.child("email").getValue(String.class);

                    if (email != null && email.equals(currentUserEmail)) {
                        String userSkills = userSnap.child("skills").getValue(String.class);
                        if (userSkills != null && !userSkills.isEmpty()) {
                            fetchCoursesMatchingSkills(userSkills);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchCoursesMatchingSkills(String userSkills) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses");

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Course course = snap.getValue(Course.class);

                    if (course != null && course.getSkills() != null) {
                        for (String userSkill : userSkills.split(",")) {
                            if (course.getSkills().toLowerCase().contains(userSkill.trim().toLowerCase())) {
                                courseList.add(course);
                                break;
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadFeaturedCourses() {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses");

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                featuredList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Course course = snap.getValue(Course.class);
                    if (course != null && course.getNoofvideos() > 30) {
                        featuredList.add(course);
                    }
                }

                featuredAdapter.notifyDataSetChanged();

                if (!featuredList.isEmpty()) {
                    updateFeaturedDetails(featuredList.get(0));
                    startCarousel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void startCarousel() {
        carouselHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (featuredList.size() > 0) {
                    carouselIndex++;
                    if (carouselIndex >= featuredList.size()) {
                        carouselIndex = 0;
                    }
                    viewPagerFeatured.setCurrentItem(carouselIndex, true);
                    updateFeaturedDetails(featuredList.get(carouselIndex));
                }
                carouselHandler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        carouselHandler.removeCallbacksAndMessages(null);
    }
}
