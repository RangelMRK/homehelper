package com.rangelmrk.homehelper.dto;

public class GastoDTO {

    public static class GastoRequest {
        private String categoria;
        private String mes;
        private double valor;

        public GastoRequest() {}

        public GastoRequest(String categoria, String mes, double valor) {
            this.categoria = categoria;
            this.mes = mes;
            this.valor = valor;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }
    }

    public static class MesRequest {
        private String mes;

        public MesRequest() {}

        public MesRequest(String mes) {
            this.mes = mes;
        }

        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }
    }
}
