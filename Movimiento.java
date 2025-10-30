package eightoff;

public class Movimiento 
{
    
    private TipoMazo tipoOrigen;
    private TipoMazo tipoDestino;
    private int idOrigen;
    private int idDestino;
    
    private ListaSimple<CartaInglesa> cartasMovidas;
    
    public Movimiento(TipoMazo origen, int idOrigen, TipoMazo destino, int idDestino, ListaSimple<CartaInglesa> cartas) 
    {
        this.tipoOrigen = origen;
        this.idOrigen = idOrigen;
        this.tipoDestino = destino;
        this.idDestino = idDestino;
        this.cartasMovidas = cartas;
    }

    public TipoMazo getTipoOrigen() 
    { 
        return tipoOrigen; 
    }
    
    public TipoMazo getTipoDestino() 
    { 
        return tipoDestino; 
    }
    public int getIdOrigen() 
    { 
        return idOrigen; 
    }
    public int getIdDestino() 
    { 
        return idDestino; 
    }
    public ListaSimple<CartaInglesa> getCartasMovidas() 
    { 
        return cartasMovidas; 
    }
}
