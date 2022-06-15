package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.ProductsAdapter;
import com.example.messheknahalal.R;
import com.example.messheknahalal.notifications.FCMSend;
import com.example.messheknahalal.models.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrdersAdapterFirebase extends FirebaseRecyclerAdapter<Order, OrdersAdapterFirebase.OrderViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;
    private final int mode;

    private final static int RV_ORDERS_USER = R.layout.orders_rv_item_user;
    private final static int RV_ORDERS_ADMIN = R.layout.orders_rv_item_admin;

    private final onOrderClickListener onOrderClickListener;

    private int oldPosition = -1;

    public OrdersAdapterFirebase(@NonNull FirebaseRecyclerOptions<Order> options, Context context, boolean isAdmin, onOrderClickListener onOrderClickListener) {
        super(options);
        this.context = context;
        this.mode = isAdmin ? RV_ORDERS_ADMIN : RV_ORDERS_USER;
        this.onOrderClickListener = onOrderClickListener;
    }

    public class OrderViewHolderFirebase extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {

        final TextView tv_user_name;
        final TextView tv_status;
        final TextView tv_final_price;
        final TextView tv_final_price_2;
        final TextView tv_date;
        final TextView tv_order_num;

        final RecyclerView rv_products;

        public OrderViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            tv_user_name = itemView.findViewById(R.id.orders_rv_item_tv_user_name);
            tv_status = itemView.findViewById(R.id.orders_rv_item_tv_status_status);
            tv_final_price = itemView.findViewById(R.id.orders_rv_item_tv_final_price);
            tv_final_price_2 = itemView.findViewById(R.id.orders_rv_item_tv_final_price_num_2);
            tv_date = itemView.findViewById(R.id.orders_rv_item_tv_date);
            tv_order_num = itemView.findViewById(R.id.orders_rv_item_tv_order_num);

            rv_products = itemView.findViewById(R.id.orders_rv_item_rv_products);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void expand(){
            rv_products.setVisibility(View.VISIBLE);
            tv_final_price.setVisibility(View.GONE);
            tv_final_price_2.setVisibility(View.VISIBLE);
        }

        public void collapse(){
            rv_products.setVisibility(View.GONE);
            tv_final_price.setVisibility(View.VISIBLE);
            tv_final_price_2.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView){
                if (rv_products.getVisibility() == View.VISIBLE){
                    collapse();
                }
                else {
                    expand();
                }

                Log.d("ariel", "oldPosition = " + oldPosition);
                Log.d("ariel", "getBindingAdapterPosition() = " + getBindingAdapterPosition());

                if (oldPosition > -1 && oldPosition != getBindingAdapterPosition()){
                    onOrderClickListener.onOrderClick(oldPosition, getBindingAdapterPosition());
                }

                oldPosition = getBindingAdapterPosition();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (mode){
                case RV_ORDERS_ADMIN:
                    updateOrderStatus();
                    break;
                case RV_ORDERS_USER:
                    deleteOrder();
                    break;
            }
            return false;
        }

        public void updateOrderStatus(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm changing the order's status");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getRef(getBindingAdapterPosition()).child("delivered").setValue(true);
                            Order order = getItem(getBindingAdapterPosition());
                            FCMSend.sendNotificationToPerson(context, order.getUserEmail(), order.getNumber());
                        }
                    });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.setMessage("Do you really want to change the status of this order?");
            dialog.show();
        }

        public void deleteOrder(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm deleting of order");
            builder.setPositiveButton("Yes",
                    (dialogInterface, i) -> getRef(getBindingAdapterPosition()).removeValue());
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog dialog = builder.create();
            dialog.setMessage("Do you really want to delete this order?");
            dialog.show();
        }

    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapterFirebase.OrderViewHolderFirebase holder, int position, @NonNull Order model) {
        holder.tv_user_name.setText(model.getUserName());
        holder.tv_status.setText(model.isDelivered() ? "delivered" : "not delivered");
        holder.tv_final_price.setText("Price: " + model.getFinalPrice() + " ₪");
        holder.tv_final_price_2.setText("Total price: " + model.getFinalPrice() + " ₪");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(-model.getDate());

        Date c = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);

        holder.tv_date.setText(formattedDate);
        holder.tv_order_num.setText("Order №: " + model.getNumber());

        ProductsAdapter productsAdapter = new ProductsAdapter(model.getProducts(), context);
        holder.rv_products.setAdapter(productsAdapter);
        holder.rv_products.setLayoutManager(new WrapContentLinearLayoutManager(context));
    }

    @NonNull
    @Override
    public OrdersAdapterFirebase.OrderViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mode, parent, false);

        return new OrderViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface onOrderClickListener{
        void onOrderClick(int oldPosition, int position);
    }
}
