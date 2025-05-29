package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import java.util.ArrayList;

public class Navigation {
    public static void setupNavigation(Activity activity) {
        Button btn_home = activity.findViewById(R.id.btn_home);
        Button btn_shop_add = activity.findViewById(R.id.btn_shop_add);
        Button btn_shop_remove = activity.findViewById(R.id.btn_shop_remove);
        Button btn_user = activity.findViewById(R.id.btn_user);
        Button btn_msg = activity.findViewById(R.id.btn_message);

        if (btn_home != null) {
            btn_home.setOnClickListener(v -> {
                Intent intent = new Intent(activity, OrderActivity.class);
                activity.startActivity(intent);
            });
        }

        if (btn_shop_add != null) {
            btn_shop_add.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CartActivity.class);
                activity.startActivity(intent);
            });
        }

        if (btn_shop_remove != null) {
            btn_shop_remove.setOnClickListener(v -> {
                Intent intent = new Intent(activity, RequestActivity.class);
                activity.startActivity(intent);
            });
        }

        if (btn_user != null) {
            btn_user.setOnClickListener(v -> {
                Intent intent = new Intent(activity, UserProfileActivity.class);
                activity.startActivity(intent);
            });
        }

        if (btn_msg != null) {
            btn_msg.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ReviewActivity.class);
                activity.startActivity(intent);
            });
        }
    }
}
