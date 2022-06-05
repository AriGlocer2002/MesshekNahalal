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
import com.example.messheknahalal.delete_user.FCMSend;
import com.example.messheknahalal.models.Admin;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminsAdapterFirebase extends FirebaseRecyclerAdapter<Admin, AdminsAdapterFirebase.AdminViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;

    private final DatabaseReference adminsRef;
    private final DatabaseReference peopleRef;

    public AdminsAdapterFirebase(@NonNull FirebaseRecyclerOptions<Admin> options, Context context) {
        super(options);
        this.context = context;

        adminsRef = FirebaseDatabase.getInstance().getReference("Admin");
        peopleRef = FirebaseDatabase.getInstance().getReference("Person");
    }

    public class AdminViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ConstraintLayout cl_admin_item;

        final TextView tv_full_name;
        final TextView tv_email;
        final TextView tv_phone;

        final ImageView admins_rv_item_iv_phone;
        final ImageView admins_rv_item_iv_email;
        final ImageView admins_rv_item_iv_message;

        final ImageView admins_rv_item_iv_pp;
        final ImageView admins_rv_item_iv_pp_frame;

        public AdminViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            cl_admin_item = itemView.findViewById(R.id.rl_admin_item);

            tv_full_name = itemView.findViewById(R.id.admins_rv_item_tv_full_name);
            tv_email = itemView.findViewById(R.id.admins_rv_item_tv_email);
            tv_phone = itemView.findViewById(R.id.admins_rv_item_tv_phone);

            admins_rv_item_iv_phone = itemView.findViewById(R.id.admins_rv_item_iv_phone);
            admins_rv_item_iv_phone.setOnClickListener(this);

            admins_rv_item_iv_email = itemView.findViewById(R.id.admins_rv_item_iv_email);
            admins_rv_item_iv_email.setOnClickListener(this);

            admins_rv_item_iv_message = itemView.findViewById(R.id.admins_rv_item_iv_message);
            admins_rv_item_iv_message.setOnClickListener(this);

            admins_rv_item_iv_pp = itemView.findViewById(R.id.admins_rv_item_iv_pp);
            admins_rv_item_iv_pp_frame = itemView.findViewById(R.id.admins_rv_item_iv_pp_frame);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == admins_rv_item_iv_phone) {
                Intent intent_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getItem(getBindingAdapterPosition()).getPhone()));
                context.startActivity(intent_phone);
            }
            if (view == admins_rv_item_iv_email) {
                Intent intent_mail = new Intent(Intent.ACTION_SENDTO);
                intent_mail.setData(Uri.parse("mailto:"));
                intent_mail.putExtra(Intent.EXTRA_EMAIL, new String[]{getItem(getBindingAdapterPosition()).getEmail()});
                context.startActivity(intent_mail);
            }
            if (view == admins_rv_item_iv_message) {
                String text = "";
                String number = "+972" + getItem(getBindingAdapterPosition()).getPhone();
                String url = "https://api.whatsapp.com/send?phone="+number + "&text=" + text;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (view == itemView){

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Confirm delete of admin");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = getItem(getBindingAdapterPosition()).getEmail();
                        deleteAdmin(email);
                    }
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you really want to delete this admin?");
                dialog.show();
            }

            return true;
        }

        public void deleteAdmin(String email){
            String adminPath = Utils.emailToAdminPath(email);
            String personPath = Utils.emailToPersonPath(email);

            adminsRef.child(adminPath).removeValue();
            peopleRef.child(personPath).removeValue();

            FCMSend.sendNotificationsToDeletePerson(context, email);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsAdapterFirebase.AdminViewHolderFirebase holder, int position, @NonNull Admin model) {

        holder.tv_full_name.setText(model.getFullName());
        holder.tv_email.setText(model.getEmail());
        holder.tv_phone.setText(model.getPhone());

        if (model.getPicture() != null && !model.getPicture().isEmpty()){
            Glide.with(context).load(model.getPicture()).centerCrop().into(holder.admins_rv_item_iv_pp);
        }
        else {
            Glide.with(context).load(R.drawable.sample_profile).centerCrop().into(holder.admins_rv_item_iv_pp);
        }
    }

    @NonNull
    @Override
    public AdminsAdapterFirebase.AdminViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_rv_item_updated, parent, false);

        return new AdminsAdapterFirebase.AdminViewHolderFirebase(view);
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
