package com.mob.schneiderpersson.agrohortav1;

public class Catalogo {
    String key, tipo;

    public Catalogo(){}

    public Catalogo(String nome, String key, String tipo)
    {
        key = key;
        tipo = tipo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
