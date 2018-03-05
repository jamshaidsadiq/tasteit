package com.i360ihrd.tasteit.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.i360ihrd.tasteit.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamshaid on 08-02-2018.
 */

public class Database extends SQLiteAssetHelper {


    private static final String DB_NAME =  "tasteItDB.db";
    private static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }


    public List<Order> getCarts()
    {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","ProductId","ProductName","Quantity","Price","Discount"};

        queryBuilder.setTables("OrderDetail");
        Cursor cursor = queryBuilder.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();

        if(cursor.moveToFirst()){

            do{
                result.add(
                    new Order (cursor.getString(cursor.getColumnIndex("ID"))  ,cursor.getString(cursor.getColumnIndex("ProductId"))  ,
                    cursor.getString(cursor.getColumnIndex("ProductName"))  ,
                    cursor.getString(cursor.getColumnIndex("Quantity"))  ,
                    cursor.getString(cursor.getColumnIndex("Price"))  ,
                    cursor.getString(cursor.getColumnIndex("Discount"))
                        ));
            }while (cursor.moveToNext());


        }

        return result;

    }


    public void addToCart(Order order){


        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()
                );
        db.execSQL(query);

    }


    public void ClearCart(){


        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }


    public void removeFromCart(String id){


        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail where ID = %s",id);

        db.execSQL(query);

    }



}
