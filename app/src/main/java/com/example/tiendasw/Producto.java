package com.example.tiendasw;

public class Producto {
    private int id;
    private String tipo;
    private String genero;
    private String talla;
    private double precio;

    public Producto(int id, String tipo, String genero, String talla, double precio) {
        this.id = id;
        this.tipo = tipo;
        this.genero = genero;
        this.talla = talla;
        this.precio = precio;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getGenero() {
        return genero;
    }

    public String getTalla() {
        return talla;
    }

    public double getPrecio() {
        return precio;
    }
}
