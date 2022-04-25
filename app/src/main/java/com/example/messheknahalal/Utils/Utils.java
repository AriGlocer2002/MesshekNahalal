package com.example.messheknahalal.Utils;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

public class Utils {

    @NonNull
    public static String capitalizeString(@NonNull String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static void showAlertDialog(String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("OK",null);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    @NonNull
    public static String emailToPath(@NonNull String email){
        return email.replace(".", "-");
    }

    @NonNull
    public static String emailForFCM(@NonNull String email){
        return email.replace(".", "-").replace("@", "%");
    }

    @NonNull
    public static String emailToUserPath(@NonNull String email){
        return "User_" + email.replace(".", "-");
    }

    @NonNull
    public static String emailToPersonPath(@NonNull String email){
        return "Person_" + email.replace(".", "-");
    }

    @NonNull
    public static String productNameToPath(@NonNull String name){
        String path = "product_"+name.replace(" ","-");
        path = path.replace(".", "-");
        return path;
    }

    @NonNull
    public static String emailToAdminPath(@NonNull String email) {
        return "Admin_" + email.replace(".", "-");
    }

    /**
     * get uri to drawable or any other resource type if u wish
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    public static Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
    }
}
