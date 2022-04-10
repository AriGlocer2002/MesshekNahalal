package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.Admin;
import com.example.messheknahalal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminsAdapterFirebase extends
        FirebaseRecyclerAdapter<Admin, AdminsAdapterFirebase.AdminViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private ObservableSnapshotArray<Admin> adminsSnapshotArray;
    private FirebaseRecyclerOptions<Admin> options;
    private final Context context;

    //    ArrayList<Admin> admins;
    StorageReference rStore;
    private LayoutInflater inflater;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference adminsRef;
    private final DatabaseReference peopleRef;

    public AdminsAdapterFirebase(@NonNull FirebaseRecyclerOptions<Admin> options, Context context) {
        super(options);
        this.adminsSnapshotArray = options.getSnapshots();
        this.context = context;

        adminsRef = FirebaseDatabase.getInstance().getReference("Admin");
        peopleRef = FirebaseDatabase.getInstance().getReference("Person");
    }

    public class AdminViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RelativeLayout rl_admin_item;

        TextView tv_full_name;
        TextView tv_email;
        TextView tv_phone;

        ImageView admins_rv_item_iv_phone;
        ImageView admins_rv_item_iv_email;
        ImageView admins_rv_item_iv_message;

        ImageView admins_rv_item_iv_pp;
        ImageView admins_rv_item_iv_pp_frame;

        public AdminViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            rl_admin_item = itemView.findViewById(R.id.rl_admin_item);

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
                        email = "Admin_" + email.replace(".","-");
                        deleteAdmin(email);
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

        public void deleteAdmin(String path){
            adminsRef.child(path).removeValue();
            peopleRef.child(path).removeValue();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminsAdapterFirebase.AdminViewHolderFirebase holder, int position, @NonNull Admin model) {

        holder.tv_full_name.setText(model.getName()+" "+ model.getLast_name());
        holder.tv_email.setText(model.getEmail());
        holder.tv_phone.setText(model.getPhone());

        rStore = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = rStore.child("profiles/pp_" + model.getEmail().replace(".","-")+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).centerCrop().into(holder.admins_rv_item_iv_pp);
            }
        });
    }

    @NonNull
    @Override
    public AdminsAdapterFirebase.AdminViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_rv_item, parent, false);

        return new AdminsAdapterFirebase.AdminViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }
}

