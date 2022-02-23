package com.example.messheknahalal.Admin_screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messheknahalal.Objects.User;
import com.example.messheknahalal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UsersAdapterFirebase  extends
        FirebaseRecyclerAdapter<User, UsersAdapterFirebase.UserViewHolderFirebase> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private ObservableSnapshotArray<User> usersSnapshotArray;
    private FirebaseRecyclerOptions<User> options;
    private final Context context;

//    ArrayList<User> users;
    StorageReference rStore;
    private LayoutInflater inflater;
    private final DatabaseReference usersRef;

    public UsersAdapterFirebase(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.usersSnapshotArray = options.getSnapshots();
        this.context = context;

        usersRef = FirebaseDatabase.getInstance().getReference("User");
        rStore = FirebaseStorage.getInstance().getReference();
    }

    public class UserViewHolderFirebase extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        RelativeLayout rl_user_item;

        TextView tv_full_name;
        TextView tv_email;
        TextView tv_phone;

        ImageView users_lv_item_iv_phone;
        ImageView users_lv_item_iv_email;
        ImageView users_lv_item_iv_message;

        ImageView users_lv_item_iv_pp;
        ImageView users_lv_item_iv_pp_frame;

        public UserViewHolderFirebase(@NonNull View itemView) {
            super(itemView);

            rl_user_item = itemView.findViewById(R.id.rl_user_item);

            tv_full_name = itemView.findViewById(R.id.users_lv_item_tv_full_name);
            tv_email = itemView.findViewById(R.id.users_lv_item_tv_email);
            tv_phone = itemView.findViewById(R.id.users_lv_item_tv_phone);

            users_lv_item_iv_phone = itemView.findViewById(R.id.users_lv_item_iv_phone);
            users_lv_item_iv_phone.setOnClickListener(this);

            users_lv_item_iv_email = itemView.findViewById(R.id.users_lv_item_iv_email);
            users_lv_item_iv_email.setOnClickListener(this);

            users_lv_item_iv_message = itemView.findViewById(R.id.users_lv_item_iv_message);
            users_lv_item_iv_message.setOnClickListener(this);

            users_lv_item_iv_pp = itemView.findViewById(R.id.users_lv_item_iv_pp);
            users_lv_item_iv_pp_frame = itemView.findViewById(R.id.users_lv_item_iv_pp_frame);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == users_lv_item_iv_phone) {
                Toast.makeText(context, getItem(getBindingAdapterPosition()).getPhone(), Toast.LENGTH_SHORT).show();
            }
            if (view == users_lv_item_iv_email) {
                Toast.makeText(context, getItem(getBindingAdapterPosition()).getEmail(), Toast.LENGTH_SHORT).show();
            }
            if (view == users_lv_item_iv_message) {
                Toast.makeText(context, getItem(getBindingAdapterPosition()).getPhone(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (view == itemView){

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Confirm delete of user");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = "User_" + getItem(getBindingAdapterPosition()).getEmail().replace(".","-");
                        deleteUser(email);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setMessage("Do you really want to delete this user?");
                dialog.show();
            }

            return true;
        }

        public void deleteUser(String path){
            usersRef.child(path).removeValue();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolderFirebase holder, int position, @NonNull User model) {
        User tmp = getItem(position);

        holder.tv_full_name.setText(tmp.getName()+" "+tmp.getLast_name());
        holder.tv_email.setText(tmp.getEmail());
        holder.tv_phone.setText(tmp.getPhone());

        Log.d("ariel", "E-mail is " + getItem(position).getEmail());
        StorageReference profileRef = rStore.child("profiles/pp_" +getItem(position).getEmail().replace(".","-")+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).centerCrop().into(holder.users_lv_item_iv_pp);

            }
        });

        /*int backgroundColor = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            backgroundColor = ((GradientDrawable) holder.rl_user_item.getBackground()).getColor().getDefaultColor();
        }
        holder.users_lv_item_iv_pp.setBackgroundColor(backgroundColor);*/
    }

    @NonNull
    @Override
    public UserViewHolderFirebase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_lv_item, parent, false);

        return new UserViewHolderFirebase(view);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().size();
    }
}
