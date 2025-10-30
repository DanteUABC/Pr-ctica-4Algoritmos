package eightoff;

import java.util.ArrayList;

public class FoundationDeck {
    Palo palo;
    ListaSimple cartas = new ListaSimple();

    public FoundationDeck(Palo palo) 
    {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1)
            cartas.insertarFin(carta);
    }

    /**
     * Agrega una carta al montículo. Sólo la agrega si
     * la carta es del palo del montículo y el la siguiente
     * carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.isEmpty()) {
                if (carta.getValorBajo() == 1) {
                    // si no hay cartas entonces la carta debe ser un A
                    cartas.insertarFin(carta);
                    agregado = true;
                }
            } else {
                // si hay cartas entonces debe haber secuencia
                CartaInglesa ultimaCarta = (CartaInglesa) cartas.getUltimoDato();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    // agregar la carta si el la siguiente a la última
                    cartas.insertarFin(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }
    
    public boolean sePuedeAgregarCarta(CartaInglesa carta) 
    {
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.isEmpty()) {
                if (carta.getValorBajo() == 1) {
                    return true;
                }
            } else {
                CartaInglesa ultimaCarta = (CartaInglesa) cartas.getUltimoDato();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public CartaInglesa removerUltimaCarta() 
    {
        return (CartaInglesa) cartas.eliminarFin(); 
    }

    public boolean estaVacio() 
    {
        return cartas.isEmpty();
    }

    public CartaInglesa getUltimaCarta() 
    {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = (CartaInglesa) cartas.getUltimoDato();
        }
        return ultimaCarta;
    }
    
    public Palo getPalo() 
    {
        return palo;
    }
    
    @Override
    public String toString() 
    {
        CartaInglesa ultima = getUltimaCarta();
        if (ultima == null) {
            return "---";
        }
        return ultima.toString();
    }
}
