package com.example.tiendasw;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Context context;
    private List<Producto> productoList;

    public ProductoAdapter(Context context, List<Producto> productoList) {
        super(context, R.layout.item_producto, productoList);
        this.context = context;
        this.productoList = productoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        // Obtener el producto actual
        Producto producto = productoList.get(position);

        // Referencias a las vistas del item
        TextView txtTipo = convertView.findViewById(R.id.txtTipo);
        TextView txtGenero = convertView.findViewById(R.id.txtGenero);
        Button btnVer = convertView.findViewById(R.id.btnVer);

        // Setear los datos en las vistas
        txtTipo.setText("Tipo: " + producto.getTipo());
        txtGenero.setText("Género: " + producto.getGenero());

        // Mostrar solo talla y precio en el diálogo
        btnVer.setOnClickListener(v -> mostrarDialogo(producto));

        return convertView;
    }

    private void mostrarDialogo(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Detalles del Producto");
        builder.setMessage(
                "Talla: " + producto.getTalla() + "\n" +
                        "Precio: S/. " + producto.getPrecio()
        );
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
