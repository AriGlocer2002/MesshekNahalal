package com.example.messheknahalal.Utils;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import com.example.messheknahalal.Objects.Product;

import java.util.ArrayList;

public class Utils {

    public final static String DATABASE_NAME = "db3";


    public final static String TABLE_PRODUCTS_NAME = "tbl_products";
    public final static String TABLE_PRODUCTS_COL_ID = "product_id";
    public final static String TABLE_PRODUCTS_COL_NAME = "product_name";
    public final static String TABLE_PRODUCTS_COL_TYPE = "product_type";
    public final static String TABLE_PRODUCTS_COL_STOCK = "product_stock";
    public final static String TABLE_PRODUCTS_COL_PRICE = "product_price";
    public final static String TABLE_PRODUCTS_COL_AMOUNT = "product_amount";
    public final static String TABLE_PRODUCTS_COL_PICTURE = "product_picture";
    public final static String TABLE_PRODUCTS_COL_COUNTABLE = "product_countable";

    public static void createAllTables(@NonNull SQLiteDatabase db){
        db.execSQL("create table if not exists tbl_products(product_id integer primary key autoincrement, " +
                "product_type text, " +
                "product_name text, " +
                "product_stock double," +
                "product_price double," +
                "product_amount double," +
                "product_picture text," +
                "product_countable integer default 0)");
    }

    public static void deleteTable(@NonNull SQLiteDatabase db){
        db.execSQL("drop table if exists tbl_products");
    }

    /*public static void addProduct(@NonNull SQLiteDatabase db, String name){
        Cursor cursor = db.rawQuery("select*from tbl_products where product_name ='" + name + "'", null);

        int amount = 1;

        while (cursor.moveToNext()){
            amount = cursor.getInt(2);
            amount++;
        }
        db.execSQL("update product_name set = " + amount + " where product_name ='" + name +"'");
    }*/

    public static void addProduct(@NonNull SQLiteDatabase db, @NonNull Product product){
        Cursor cursor = db.rawQuery("select*from tbl_products where product_name ='" + product.getName() + "'", null);
        if (!cursor.moveToNext()){
            ContentValues cv = new ContentValues();
            cv.put(TABLE_PRODUCTS_COL_TYPE, product.getType());
            cv.put(TABLE_PRODUCTS_COL_NAME, product.getName());
            cv.put(TABLE_PRODUCTS_COL_STOCK, product.getStock());
            cv.put(TABLE_PRODUCTS_COL_PRICE, product.getPrice());
            cv.put(TABLE_PRODUCTS_COL_AMOUNT, product.getAmount());
            cv.put(TABLE_PRODUCTS_COL_PICTURE, product.getPicture());
            cv.put(TABLE_PRODUCTS_COL_COUNTABLE, product.isCountable());

            db.insert(TABLE_PRODUCTS_NAME, TABLE_PRODUCTS_COL_ID, cv);
        }
        else {
            db.execSQL("update tbl_products set product_amount =" + product.getAmount() + " where product_name ='" + product.getName() + "'");
        }

        cursor.close();
    }

    @NonNull
    public static ArrayList<Product> getSavedProducts(@NonNull SQLiteDatabase db){
        ArrayList<Product> products = new ArrayList<>();

        Cursor cursor = db.rawQuery("select*from tbl_products", null);
        while (cursor.moveToNext()){
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            double stock = cursor.getDouble(3);
            double price = cursor.getDouble(4);
            double amount = cursor.getDouble(5);
            String picture = cursor.getString(6);
            boolean countable = cursor.getInt(7) == 1;

            Product product = new Product(type, name, stock, price, amount, picture, countable);
            products.add(product);
        }

        cursor.close();

        return products;
    }

    public static void addAllProducts(SQLiteDatabase db, @NonNull ArrayList<Product> products) {
        for (Product product : products) {
            addProduct(db, product);
        }
    }

    public static int getAmountOfProductInCart(@NonNull SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select*from tbl_products", null);
        int amount = cursor.getCount();
        cursor.close();
        return amount;
    }

    public static double getTotalPrice(@NonNull SQLiteDatabase db){
        Cursor cursor = db.rawQuery("select product_price, product_amount from tbl_products", null);

        double totalPrice = 0;
        while (cursor.moveToNext()){
            double price = cursor.getDouble(0);
            double amount = cursor.getDouble(1);
            totalPrice += price*amount;
        }

        cursor.close();

        return totalPrice;
    }

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
