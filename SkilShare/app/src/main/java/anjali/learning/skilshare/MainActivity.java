package anjali.learning.skilshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNav;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.bottom_explore) {
                selectedFragment = new ExploreSkilCoursesFragment();
            } else if (id == R.id.bottom_favourites) {
                selectedFragment = new FavouritesFragment();
            } else if (id == R.id.bottom_profile) {
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                profileIntent.putExtra("username", username);
                startActivity(profileIntent);
                return true;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.name);
        TextView emailTextView = headerView.findViewById(R.id.email);
        ImageView profileImage = headerView.findViewById(R.id.profileimg);

        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "anjali valani"; // fallback
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    nameTextView.setText(name);
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch user info", Toast.LENGTH_SHORT).show();
            }
        });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
            bottomNav.setSelectedItemId(R.id.bottom_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_requestskilcourse) {
            selectedFragment = new RequestSkilCourseFragment();
        } else if (id == R.id.nav_uploadskilcourse) {
            selectedFragment = new UploadSkillCourseFragment();
        } else if (id == R.id.nav_exploremoreskilcourse) {
            selectedFragment = new ExploreSkilCoursesFragment();
        } else if (id == R.id.nav_skilprofessionals) {
            selectedFragment = new SkilProfessionalsFragment();
        } else if (id == R.id.nav_exploremore) {
            selectedFragment = new ExploreMoreFragment();
        } else if (id == R.id.nav_registeredcourse) {
            selectedFragment = new RegisteredCourseFragment();
        } else if (id == R.id.nav_favourites) {
            selectedFragment = new FavouritesFragment();
        } else if (id == R.id.nav_settings) {
            selectedFragment = new SettingsFragment();
        } else if (id == R.id.nav_feedback) {
            selectedFragment = new FeedbackFragment();
        } else if (id == R.id.nav_barter) {
            selectedFragment = new SwapFragment();
            Bundle args = new Bundle();
            args.putString("username", username);
            selectedFragment.setArguments(args);}
//        } else if (id == R.id.nav_view_swap_requests) { // ✅ YOUR NEW OPTION
//            selectedFragment = new ViewSwapRequestsFragment();
//            Bundle args = new Bundle();
//            args.putString("username", username);
//            selectedFragment.setArguments(args);
//        }
//        else if (id == R.id.nav_explore_swap_users) {
//            selectedFragment = new ExploreSwapUsersFragment();
//            Bundle args = new Bundle();
//            args.putString("username", username);
//            selectedFragment.setArguments(args);
//        }

        else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
