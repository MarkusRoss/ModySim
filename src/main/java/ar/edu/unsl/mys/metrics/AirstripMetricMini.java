package ar.edu.unsl.mys.metrics;

public class AirstripMetricMini {
    // ANALITICAS
    int id;
    float tiempoOcioTotal;
    float tiempoOcioMaximo;
    float tamañoMaximoCola;
    float durabilidadRestante;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getTiempoOcioTotal() {
        return tiempoOcioTotal;
    }
    public void setTiempoOcioTotal(float tiempoOcioTotal) {
        this.tiempoOcioTotal = tiempoOcioTotal;
    }
    public float getTiempoOcioMaximo() {
        return tiempoOcioMaximo;
    }
    public void setTiempoOcioMaximo(float tiempoOcioMaximo) {
        this.tiempoOcioMaximo = tiempoOcioMaximo;
    }
    public float getTamañoMaximoCola() {
        return tamañoMaximoCola;
    }
    public void setTamañoMaximoCola(float tamañoMaximoCola) {
        this.tamañoMaximoCola = tamañoMaximoCola;
    }
    public float getDurabilidadRestante() {
        return durabilidadRestante;
    }
    public void setDurabilidadRestante(float durabilidadRestante) {
        this.durabilidadRestante = durabilidadRestante;
    }
    @Override
    public String toString() {
        return "AirstripMetricMini [durabilidadRestante=" + durabilidadRestante + ", id=" + id + ", tamañoMaximoCola="
                + tamañoMaximoCola + ", tiempoOcioMaximo=" + tiempoOcioMaximo + ", tiempoOcioTotal=" + tiempoOcioTotal
                + "]";
    }

    
    
}
