package eightoff;

import java.util.ArrayList;
import java.util.Iterator;

public class TableauDeck 
{
    ListaSimple cartas = new ListaSimple();

    public void inicializar(ListaSimple cartas) 
    {
        this.cartas = cartas;
    }

    public ListaSimple removeStartingAt(int value) 
    {
        ListaSimple removed = new ListaSimple();
        
        ListaSimple aEliminar = new ListaSimple();
        
        Nodo r = cartas.getInicio();
        while (r != null) 
        {
            CartaInglesa next = (CartaInglesa) r.getDato();
            if (next.isFaceup())
                if (next.getValor() <= value) 
                {
                    removed.insertarFin(next);
                    aEliminar.insertarFin(next);
                }
            r = r.getSig();
        }
        Nodo e = aEliminar.getInicio();
        while (e != null)
        {
            cartas.eliminaX((CartaInglesa) e.getDato());
            e = e.getSig();
        }
        
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) 
    {
        CartaInglesa cartaConElValorDeseado = null;
        
        Nodo r = cartas.getInicio();
        while (r != null) 
        {
            CartaInglesa next = (CartaInglesa) r.getDato();
            if (next.isFaceup())
                if (next.getValor() <= value) 
                {
                    cartaConElValorDeseado = next;
                    break;
                }
            r = r.getSig();
        }
        return cartaConElValorDeseado;
    }

    public boolean agregarCarta(CartaInglesa carta) 
    {
        boolean agregado = false;

        if (sePuedeAgregarCarta(carta)) 
        {
            carta.makeFaceUp();
            cartas.insertarFin(carta);
            agregado = true;
        }
        return agregado;
    }

    CartaInglesa verUltimaCarta() 
    {
        if (cartas.isEmpty()) {
            return null;
        }
        return (CartaInglesa) cartas.getUltimoDato();
    }

    CartaInglesa removerUltimaCarta() 
    {
    CartaInglesa ultimaCarta = null;
    
    if (!cartas.isEmpty()) {
        ultimaCarta = (CartaInglesa) cartas.getUltimoDato(); 
        cartas.eliminarFin();
    }
    return ultimaCarta;
}

    @Override
    public String toString() 
    {
        StringBuilder builder = new StringBuilder();

        if (cartas.isEmpty()) { 
            builder.append("---");
        } else {
            Nodo r = cartas.getInicio();
            while (r != null) 
            {
                CartaInglesa carta = (CartaInglesa) r.getDato();
                builder.append(carta.toString());
                builder.append(" ");
                r = r.getSig();
            }
        }
        return builder.toString();
    }

    public boolean agregarBloqueDeCartas(ListaSimple cartasRecibidas) 
    {
        boolean resultado = false;

        if (cartasRecibidas != null && !cartasRecibidas.isEmpty()) 
        {
            CartaInglesa primera = (CartaInglesa) cartasRecibidas.getPrimerDato(); 
            
            if (sePuedeAgregarCarta(primera)) 
            {
                cartas.agregarTodo(cartasRecibidas); 
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean isEmpty() 
    {
        return cartas.isEmpty();
    }

    public boolean sePuedeAgregarCarta(CartaInglesa cartaInicialDePrueba) 
    {
        boolean resultado = false;
        if (!cartas.isEmpty()) 
        {
            CartaInglesa ultima = (CartaInglesa) cartas.getUltimoDato();
            if (ultima.getPalo().equals(cartaInicialDePrueba.getPalo()))
                if (ultima.getValor() == cartaInicialDePrueba.getValor() + 1)
                    resultado = true;
        } 
        else 
            if (cartaInicialDePrueba.getValor() == 13) 
                resultado = true;
        return resultado;
    }
    
    public void agregarCartaSetup(CartaInglesa carta)
    {
        cartas.insertarFin(carta);
    }
    
    public void agregarBloqueCartasUndo(ListaSimple<CartaInglesa> cartasRecibidas) 
    {
        cartas.agregarTodo(cartasRecibidas);
    }
    
    public ListaSimple getSecuenciaValidaAlFinal()
    {
        ListaSimple<CartaInglesa> secuencia = new ListaSimple<>();
        int tam = cartas.getTamano();

        if (tam == 0)
            return secuencia;

        CartaInglesa ultima = (CartaInglesa) cartas.getUltimoDato();
        secuencia.insertarInicio(ultima);

        for (int i = tam - 2; i >= 0; i--) 
        {
            CartaInglesa penultima = (CartaInglesa) cartas.buscaPosicion(i);
            if (penultima.getPalo().equals(ultima.getPalo()) && penultima.getValor() == ultima.getValor() + 1)
            {
                secuencia.insertarInicio(penultima);
                ultima = penultima; 
            } 
            else 
                break;
        }

        return secuencia;
    }
    
    public void removerUltimasCartas(int n)
    {
        for (int i = 0; i < n; i++)
            cartas.eliminarFin();
    }

    public CartaInglesa getUltimaCarta() 
    {
        if (cartas.isEmpty()) 
        {
            return null;
        }
        return (CartaInglesa) cartas.getUltimoDato();
    }

    public ListaSimple getCards() 
    {
        return cartas;
    }
}
