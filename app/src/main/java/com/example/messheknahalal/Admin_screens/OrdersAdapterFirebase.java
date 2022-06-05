package com.example.messheknahalal.Admin_screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.R;
import com.example.messheknahalal.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrdersAdapterFirebase extends FirebaseRecyclerAdapter<Cart, OrdersAdapterFirebase.OrderViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;

    public OrdersAdapterFirebase(@NonNull FirebaseRecyclerOptions<Cart> options, Context context) {
        super(options);
        this.context = context;
    }

    public static class OrderViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_user_name;
        final TextView tv_status;
        final TextView tv_final_price;
        final TextView tv_date;
        final TextView tv_order_num;

        public OrderViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            tv_user_name = itemView.findViewById(R.id.orders_rv_item_tv_user_name);
            tv_status = itemView.findViewById(R.id.orders_rv_item_tv_status_status);
            tv_final_price = itemView.findViewById(R.id.orders_rv_item_tv_final_price_num);
            tv_date = itemView.findViewById(R.id.orders_rv_item_tv_date);
            tv_order_num = itemView.findViewById(R.id.orders_rv_item_tv_order_num);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView){

            }
        }

    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapterFirebase.OrderViewHolderFirebase holder, int position, @NonNull Cart model) {
        holder.tv_user_name.setText(model.getUserName());
        holder.tv_status.setText(model.isDelivered() ? "delivered" : "not delivered");
        holder.tv_final_price.setText("" + model.getFinalPrice());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(-model.getDate());

        Date c = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);

        holder.tv_date.setText(formattedDate);
        holder.tv_order_num.setText("Order №: " + model.getNumber());
    }

    @NonNull
    @Override
    public OrdersAdapterFirebase.OrderViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_rv_item_admin, parent, false);

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
}
