package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public class OrderViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_user_name;
        TextView tv_status;
        TextView tv_final_price;
        TextView tv_date;
        TextView tv_order_num;

        public OrderViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            tv_user_name = itemView.findViewById(R.id.orders_rv_item_tv_user_name);
            tv_status = itemView.findViewById(R.id.orders_rv_item_tv_status_status);
            tv_final_price = itemView.findViewById(R.id.orders_rv_item_tv_final_price_num);
            tv_date = itemView.findViewById(R.id.orders_rv_item_tv_date);
            tv_order_num = itemView.findViewById(R.id.orders_rv_item_tv_order_num);

        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            if (view == itemView){

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm delete of admin");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you really want to delete this admin?");
                dialog.show();
            }

            return true;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapterFirebase.OrderViewHolderFirebase holder, int position, @NonNull Cart model) {

        holder.tv_user_name.setText(model.getUserName());
        holder.tv_status.setText(model.isDelivered() ? "delivered" : "not delivered");
        holder.tv_final_price.setText(""+model.getFinalPrice());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(-model.getDate());

        Date c = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);

        holder.tv_date.setText(formattedDate);
        holder.tv_order_num.setText("Order №: "+model.getNumber());
    }

    @NonNull
    @Override
    public OrdersAdapterFirebase.OrderViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_rv_item_admin, parent, false);

        return new OrdersAdapterFirebase.OrderViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }
}
