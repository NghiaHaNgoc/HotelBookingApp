package com.hotel.hotel_booking_app;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hotel.hotel_booking_app.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public NavController navController;
    public SharedPreferences settingSharedPreferences;
    SharedPreferences accountSharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener accountPreferencesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment navHostFragment = (NavHostFragment)
                this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        navController = navHostFragment.getNavController();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_reservation_history)
                .setOpenableLayout(drawer)
                .build();


        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Share preferences
        accountSharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        settingSharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);
        settingSharedPreferences.edit().putString("language", "en").apply();

        handleSignIn();

        accountPreferencesListener = (sharedPreferences, s) -> {
            handleSignIn();
        };
        accountSharedPreferences.registerOnSharedPreferenceChangeListener(accountPreferencesListener);
//        AppCompatDelegate.setApplicationLocales(
//                LocaleListCompat.create(Locale.forLanguageTag("ja"))
//        );
//        handleSettingLanguage();

    }



    private void handleSignIn() {
        View headerView = binding.navView.getHeaderView(0);
        ImageView avatar = headerView.findViewById(R.id.nav_header_image_avatar);
        TextView email = headerView.findViewById(R.id.nav_header_text_email);
        TextView fullName = headerView.findViewById(R.id.nav_header_text_full_name);

        if (accountSharedPreferences.getBoolean("isSignedIn", false)) {
            email.setText(accountSharedPreferences.getString("email", ""));
            Glide.with(avatar).load(accountSharedPreferences.getString("linkAvatar", ""))
                    .placeholder(R.drawable.user).apply(RequestOptions.circleCropTransform()).into(avatar);
            fullName.setText(String.format("%s %s", accountSharedPreferences.getString(
                    "firstname", ""), accountSharedPreferences.getString("surname", "")));

        } else {
            avatar.setImageResource(R.drawable.user);
            fullName.setText(getResources().getString(R.string.account));
            email.setText("");
        }
        invalidateMenu();
    }

    private void handleSignOut() {
        getSharedPreferences("account", MODE_PRIVATE).edit().clear().apply();
        invalidateMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_in) {
            navController.navigate(R.id.nav_sign_in);
            return true;
        }

        if (item.getItemId() == R.id.action_sign_up) {
            navController.navigate(R.id.nav_sign_up);
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {
            navController.navigate(R.id.nav_setting);
            return true;
        }

        if (item.getItemId() == R.id.action_about_us) {
            navController.navigate(R.id.nav_about_us);
            return true;
        }

        if (item.getItemId() == R.id.action_sign_out) {
            handleSignOut();
            navController.popBackStack(R.id.nav_home, false);
            Toast.makeText(this, getResources().getString(R.string.sign_out_success_message),
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        if (accountSharedPreferences.getBoolean("isSignedIn", false)) {
            menu.findItem(R.id.action_sign_in).setVisible(false);
            menu.findItem(R.id.action_sign_up).setVisible(false);

        } else {
            menu.findItem(R.id.action_sign_out).setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}