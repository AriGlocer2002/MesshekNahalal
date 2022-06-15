package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.notifications.FCMSend;
import com.example.messheknahalal.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersAdapterFirebase extends FirebaseRecyclerAdapter<User, UsersAdapterFirebase.UserViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;

    private final DatabaseReference usersRef;
    private final DatabaseReference peopleRef;

    public UsersAdapterFirebase(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);

        this.context = context;

        this.usersRef = FirebaseDatabase.getInstance().getReference("User");
        this.peopleRef = FirebaseDatabase.getInstance().getReference("Person");
    }

    public class UserViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        final ConstraintLayout rl_user_item;

        final TextView tv_full_name;
        final TextView tv_email;
        final TextView tv_phone;

        final ImageView users_lv_item_iv_phone;
        final ImageView users_lv_item_iv_email;
        final ImageView users_lv_item_iv_message;

        final ImageView users_lv_item_iv_pp;

        public UserViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            rl_user_item = itemView.findViewById(R.id.rl_user_item);

            tv_full_name = itemView.findViewById(R.id.users_rv_item_tv_full_name);
            tv_email = itemView.findViewById(R.id.users_rv_item_tv_email);
            tv_phone = itemView.findViewById(R.id.users_rv_item_tv_phone);

            users_lv_item_iv_phone = itemView.findViewById(R.id.users_rv_item_iv_phone);
            users_lv_item_iv_phone.setOnClickListener(this);

            users_lv_item_iv_email = itemView.findViewById(R.id.users_rv_item_iv_email);
            users_lv_item_iv_email.setOnClickListener(this);

            users_lv_item_iv_message = itemView.findViewById(R.id.users_rv_item_iv_message);
            users_lv_item_iv_message.setOnClickListener(this);

            users_lv_item_iv_pp = itemView.findViewById(R.id.users_rv_item_iv_pp);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == users_lv_item_iv_phone) {
                Intent intent_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getItem(getBindingAdapterPosition()).getPhone()));
                context.startActivity(intent_phone);
            }
            if (view == users_lv_item_iv_email) {
                Intent intent_mail = new Intent(Intent.ACTION_SENDTO);
                intent_mail.setData(Uri.parse("mailto:"));
                intent_mail.putExtra(Intent.EXTRA_EMAIL, new String[]{getItem(getBindingAdapterPosition()).getEmail()});
                context.startActivity(intent_mail);
            }
            if (view == users_lv_item_iv_message) {
                String text = "";
                String number = "+972" + getItem(getBindingAdapterPosition()).getPhone();
                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + text;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (view == itemView){

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm delete of user");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = getItem(getBindingAdapterPosition()).getEmail();
                        deleteUser(email);
                    }
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you really want to delete this user?");
                dialog.show();
            }

            return true;
        }

        public void deleteUser(String email){
            String userPath = Utils.emailToUserPath(email);
            String personPath = Utils.emailToPersonPath(email);

            usersRef.child(userPath).removeValue();
            peopleRef.child(personPath).removeValue();

            FCMSend.sendNotificationToDeletePerson(context, email);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolderFirebase holder, int position, @NonNull User model) {
        holder.tv_full_name.setText(model.getFullName());
        holder.tv_email.setText(model.getEmail());
        holder.tv_phone.setText(model.getPhone());

        if (model.getPicture() != null && !model.getPicture().isEmpty()){
            Glide.with(context).load(model.getPicture()).centerCrop().into(holder.users_lv_item_iv_pp);
        }
        else {
            Glide.with(context).load(R.drawable.sample_profile).centerCrop().into(holder.users_lv_item_iv_pp);
        }
    }

    @NonNull
    @Override
    public UsersAdapterFirebase.UserViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rv_item_updated, parent, false);

        return new UserViewHolderFirebase(view);
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