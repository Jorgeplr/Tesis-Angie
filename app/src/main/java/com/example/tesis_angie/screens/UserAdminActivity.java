package com.example.tesis_angie.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tesis_angie.R;
import java.util.ArrayList;
import java.util.List;

public class UserAdminActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private UserAdapter userAdapter;
    private List<String> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        rvUsers = findViewById(R.id.rv_users);
        userList = new ArrayList<>();
        userList.add("Juan Perez");
        userList.add("Ana Torres");
        userList.add("Carlos Ruiz");

        userAdapter = new UserAdapter(userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(int position) {
                showEditUserDialog(position);
            }
            @Override
            public void onDelete(int position) {
                showDeleteUserDialog(position);
            }
        });
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);

        // Cards de acción
        findViewById(R.id.card_add_user).setOnClickListener(v -> showAddUserDialog());
        findViewById(R.id.card_edit_user).setOnClickListener(v -> showSelectUserDialog(true));
        findViewById(R.id.card_delete_user).setOnClickListener(v -> showSelectUserDialog(false));

        // TopBar funcionalidad
        initializeTopBar("Administración de Usuarios");
        View notificationBtn = findViewById(R.id.topbar_notification_btn);
        if (notificationBtn != null) {
            notificationBtn.setOnClickListener(v -> Toast.makeText(this, "No hay notificaciones nuevas", Toast.LENGTH_SHORT).show());
        }
        View userAvatar = findViewById(R.id.topbar_user_avatar_card);
        if (userAvatar != null) {
            userAvatar.setOnClickListener(v -> Toast.makeText(this, "Perfil de usuario", Toast.LENGTH_SHORT).show());
        }
    }

    // Inicializa topbar para pantallas internas
    private void initializeTopBar(String titleText) {
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        View backBtn = findViewById(R.id.topbar_back_btn);
        TextView title = findViewById(R.id.topbar_title);

        if (menuBtn != null) menuBtn.setVisibility(View.GONE);
        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> finish());
        }
        if (title != null) title.setText(titleText);
    }

    private void showAddUserDialog() {
        EditText input = new EditText(this);
        input.setHint("Nombre del usuario");
        new AlertDialog.Builder(this)
            .setTitle("Agregar Usuario")
            .setView(input)
            .setPositiveButton("Agregar", (dialog, which) -> {
                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
                    userList.add(name);
                    userAdapter.notifyItemInserted(userList.size() - 1);
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void showEditUserDialog(int position) {
        EditText input = new EditText(this);
        input.setText(userList.get(position));
        new AlertDialog.Builder(this)
            .setTitle("Editar Usuario")
            .setView(input)
            .setPositiveButton("Guardar", (dialog, which) -> {
                String name = input.getText().toString().trim();
                if (!name.isEmpty()) {
                    userList.set(position, name);
                    userAdapter.notifyItemChanged(position);
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void showDeleteUserDialog(int position) {
        new AlertDialog.Builder(this)
            .setTitle("Eliminar Usuario")
            .setMessage("¿Seguro que deseas eliminar este usuario?")
            .setPositiveButton("Eliminar", (dialog, which) -> {
                userList.remove(position);
                userAdapter.notifyItemRemoved(position);
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    // Diálogo para seleccionar usuario a editar/eliminar
    private void showSelectUserDialog(boolean isEdit) {
        if (userList.isEmpty()) {
            Toast.makeText(this, "No hay usuarios disponibles", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] usersArray = userList.toArray(new String[0]);
        new AlertDialog.Builder(this)
            .setTitle(isEdit ? "Selecciona usuario para editar" : "Selecciona usuario para eliminar")
            .setItems(usersArray, (dialog, which) -> {
                if (isEdit) {
                    showEditUserDialog(which);
                } else {
                    showDeleteUserDialog(which);
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}

// Adaptador profesional para la lista de usuarios
class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<String> users;
    private final OnUserActionListener listener;
    public interface OnUserActionListener {
        void onEdit(int position);
        void onDelete(int position);
    }
    public UserAdapter(List<String> users, OnUserActionListener listener) {
        this.users = users;
        this.listener = listener;
    }
    @Override
    public UserViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.bind(users.get(position), listener, position);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final android.widget.TextView tvUserName;
        private final android.widget.ImageButton btnEditUser;
        private final android.widget.ImageButton btnDeleteUser;
        public UserViewHolder(android.view.View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            btnEditUser = itemView.findViewById(R.id.btn_edit_user);
            btnDeleteUser = itemView.findViewById(R.id.btn_delete_user);
        }
        public void bind(String userName, OnUserActionListener listener, int position) {
            tvUserName.setText(userName);
            btnEditUser.setOnClickListener(v -> listener.onEdit(position));
            btnDeleteUser.setOnClickListener(v -> listener.onDelete(position));
        }
    }
}
