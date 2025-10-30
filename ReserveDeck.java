package eightoff;

public class ReserveDeck 
{
    CartaInglesa carta;
    public ReserveDeck()
    {
        carta = null;
    }
    
    public ReserveDeck(CartaInglesa carta)
    {
        this.carta = carta;
    }
    
    public boolean agregarCarta(CartaInglesa cartaAAgregar)
    {
        if(isEmpty())
        {
            carta = cartaAAgregar;
            return true;
        }
        else
            return false;
    }
    
    public CartaInglesa removerCarta()
    {
        CartaInglesa temp = this.carta;
        this.carta = null;
        return temp;
    }
    
    public boolean isEmpty()
    {
        return carta == null;
    }
    
    public CartaInglesa getCarta()
    {
        return carta;
    }
    
    public String toString()
    {
        if (isEmpty()) {
            return "---";
        }
        return carta.toString();
    }
}
