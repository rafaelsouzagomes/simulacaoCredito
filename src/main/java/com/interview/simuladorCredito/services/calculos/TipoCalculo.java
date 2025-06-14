package com.interview.simuladorCredito.services.calculos;

public enum TipoCalculo {
    PADRAO("PADRAO"),
    TABELA_PRICE("TABELA_PRICE");

    private final String codigo;

    TipoCalculo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static TipoCalculo fromCodigo(String codigo) {
        for (TipoCalculo tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        return PADRAO; // Default to PADRAO if no match found
    }
} 