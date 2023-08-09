package com.sassan.salathelper;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavBarResize {

    public static void resizeBottomNavBar(BottomNavigationView bottomNavigationView, Resources resources) {
        bottomNavigationView.post(() -> {
            int height = bottomNavigationView.getHeight();

            // Keep text size constant at 20sp
            float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, resources.getDisplayMetrics());

            // Adjust icon size based on available space. This is just an example calculation; you can adjust it based on your design.
            int iconSize = (int) (height * 0.5f); // Example calculation

            // Calculate required height for BottomNavigationView
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, resources.getDisplayMetrics()); // example padding
            int totalHeight = (int) (textSize + iconSize + (2 * padding)); // Add any additional margins or padding

            // Set the calculated height to the BottomNavigationView
            ViewGroup.LayoutParams params = bottomNavigationView.getLayoutParams();
            params.height = totalHeight;
            bottomNavigationView.setLayoutParams(params);

            // Adjust text size and icon size for each menu item
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem item = bottomNavigationView.getMenu().getItem(i);
                SpannableString spanString = new SpannableString(item.getTitle().toString());
                spanString.setSpan(new AbsoluteSizeSpan((int) textSize), 0, spanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                item.setTitle(spanString);

                Drawable icon = item.getIcon();
                if (icon != null) {
                    icon.setBounds(0, 0, iconSize, iconSize);
                    item.setIcon(icon);
                }
            }
        });
    }
}
