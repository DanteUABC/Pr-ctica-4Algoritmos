package eightoff;

public class Mazo 
{
    private ListaCircularDoble cartas = new ListaCircularDoble();

    public Mazo() 
    {
        llenar();
        mezclar();
    }
    
    public ListaCircularDoble getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta()
    {
        if (cartas.getTamano() > 0)
            return (CartaInglesa) cartas.eliminarInicio();
        return null;
    }
    private void mezclar() 
    {
        cartas.barajear();
    }

    private void llenar() 
    {
        for (int i = 2; i <=14 ; i++)
            for (Palo palo : Palo.values()) 
            {
                CartaInglesa c = new CartaInglesa(i,palo, palo.getColor());
                cartas.insertarFin(c);
            }
    }

    public void ordenar() 
    {
        cartas.ordenar();
    }

    @Override
    public String toString() 
    {
        return cartas.toString();
    }
}
