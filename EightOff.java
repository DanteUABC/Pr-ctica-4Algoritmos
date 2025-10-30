package eightoff;

public class EightOff 
{
    private Mazo mazo;
    private ListaSimple<TableauDeck> tableau = new ListaSimple();
    private ListaSimple<ReserveDeck> reserva = new ListaSimple();
    private ListaSimple<FoundationDeck> foundation = new ListaSimple();
    private ListaSimple<Movimiento> historial = new ListaSimple<>();
    private FoundationDeck lastFoundationUpdated;
    
    public EightOff()
    {
        mazo = new Mazo();
    }
    
    public void iniciarJuego()
    {
        crearFoundations();
        crearReserva();
        crearTableaux(); 

        for (int i = 0; i < 8; i++) 
        {
            TableauDeck td = (TableauDeck) tableau.buscaPosicion(i);
            for (int j = 0; j < 6; j++) 
            {
                CartaInglesa c = mazo.obtenerUnaCarta();
                if (c != null) 
                    td.agregarCartaSetup(c);
            }
        }

        for (int i = 0; i < 4; i++) 
        {
            ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(i);
            CartaInglesa c = mazo.obtenerUnaCarta();
            if (c != null)
                rd.agregarCarta(c);
        }
    }
    
    public boolean moveTableauToTableau(int posicionOrigen, int posicionDestino)
    {
        TableauDeck origen = (TableauDeck) tableau.buscaPosicion(posicionOrigen);
        TableauDeck destino = (TableauDeck) tableau.buscaPosicion(posicionDestino);
        int maxCartasMovibles = getCeldasReservaLibres() + 1;
        ListaSimple<CartaInglesa> secuencia = origen.getSecuenciaValidaAlFinal();
        CartaInglesa baseSecuencia = (CartaInglesa) secuencia.getPrimerDato();
        int tamSecuencia = secuencia.getTamano();
        boolean sePuedeMoverEscalera = false;
        
        if (posicionOrigen == posicionDestino) 
            return false;
        if (origen == null || destino == null || origen.isEmpty())
            return false;
        if (tamSecuencia <= maxCartasMovibles && destino.sePuedeAgregarCarta(baseSecuencia))
            sePuedeMoverEscalera = true;

        if (sePuedeMoverEscalera)
        {
            origen.removerUltimasCartas(tamSecuencia);
            destino.agregarBloqueDeCartas(secuencia);
            
            Movimiento movimiento = new Movimiento(TipoMazo.TABLEAU, posicionOrigen, TipoMazo.TABLEAU, posicionDestino, secuencia);
            historial.insertarInicio(movimiento);
            return true;
        }
        
        if (tamSecuencia > 1) 
        {
            CartaInglesa unaCarta = origen.getUltimaCarta();
            if (destino.sePuedeAgregarCarta(unaCarta))
            {
                origen.removerUltimaCarta(); 
                destino.agregarCarta(unaCarta);
                
                ListaSimple<CartaInglesa> unaCartaList = new ListaSimple<>();
                unaCartaList.insertarInicio(unaCarta);
                Movimiento movimiento = new Movimiento(TipoMazo.TABLEAU, posicionOrigen, TipoMazo.TABLEAU, posicionDestino, unaCartaList);
                historial.insertarInicio(movimiento);
                return true;
            }
        }
        return false;
    }
    
    public boolean moveTableauToFoundation(int posTableau)
    {
        TableauDeck td = (TableauDeck) tableau.buscaPosicion(posTableau);
        if (td == null || td.isEmpty()) 
            return false;
        
        CartaInglesa c = td.getUltimaCarta();
        int cualFoundation = c.getPalo().ordinal();
        FoundationDeck fd = (FoundationDeck) foundation.buscaPosicion(cualFoundation);
        
        if (fd.agregarCarta(c)) 
        { 
            td.removerUltimaCarta();
            lastFoundationUpdated = fd;
            
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.TABLEAU, posTableau, TipoMazo.FOUNDATION, cualFoundation, movidas);
            historial.insertarInicio(mov);
            return true;
        }
        return false;
    }
    
    public boolean moveReservaToFoundation(int posReserva)
    {
        ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(posReserva);
        if (rd == null || rd.isEmpty()) 
            return false;
        
        CartaInglesa c = rd.getCarta();
        int cualFoundation = c.getPalo().ordinal();
        FoundationDeck fd = (FoundationDeck) foundation.buscaPosicion(cualFoundation);
        
        if (fd.agregarCarta(c)) 
        {
            rd.removerCarta(); 
            lastFoundationUpdated = fd;
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.RESERVA, posReserva, TipoMazo.FOUNDATION, cualFoundation, movidas);
            historial.insertarInicio(mov);
            return true;
        }
        return false;
    }
    
    public boolean moveReservaToTableau(int posReserva, int posTableau)
    {
        ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(posReserva);
        TableauDeck td = (TableauDeck) tableau.buscaPosicion(posTableau);
        
        if (rd == null || td == null || rd.isEmpty()) return false;
        
        CartaInglesa c = rd.getCarta();
        
        if (td.agregarCarta(c)) 
        {
            rd.removerCarta(); 
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.RESERVA, posReserva, TipoMazo.TABLEAU, posTableau, movidas);
            historial.insertarInicio(mov);
            return true;
        }
        return false;
    }
    
    public boolean moveTableauToReserva(int posTableau, int posReserva)
    {
        TableauDeck td = (TableauDeck) tableau.buscaPosicion(posTableau);
        ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(posReserva);

        if (td == null || rd == null || td.isEmpty() || !rd.isEmpty())
            return false;
        
        CartaInglesa c = td.getUltimaCarta();
        
        if (rd.agregarCarta(c)) {
            td.removerUltimaCarta();
            
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.TABLEAU, posTableau, TipoMazo.RESERVA, posReserva, movidas);
            historial.insertarInicio(mov);
            return true;
        }
        return false;
    }
    
    public boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino)
    {
        return destino.agregarCarta(carta);
    }
    
    public boolean moveCartaToReserve(CartaInglesa carta, ReserveDeck destino)
    {
        return destino.agregarCarta(carta);
    }
    
    public boolean isGameOver() 
    {
        if (foundation.getTamano() != 4) 
            return false;

        Nodo r = foundation.getInicio();
        while (r != null)
        {
            FoundationDeck fd = (FoundationDeck) r.getDato(); 
            if (fd == null || fd.estaVacio())
                return false;
            CartaInglesa ultimaCarta = fd.getUltimaCarta(); 
            if (ultimaCarta == null || ultimaCarta.getValor() != 13)
                return false;
            r = r.getSig();
        }
        return true; 
    }
    
    public boolean deshacerMovimiento() {
        if (historial.isEmpty()) 
        {
            System.out.println("No hay movimientos que deshacer.");
            return false;
        }

        Movimiento movimiento = historial.eliminarInicio();
        ListaSimple<CartaInglesa> cartasADevolver = movimiento.getCartasMovidas();

        switch(movimiento.getTipoDestino()) 
        {
            case TABLEAU:
                TableauDeck td = (TableauDeck) tableau.buscaPosicion(movimiento.getIdDestino());
                td.removerUltimasCartas(cartasADevolver.getTamano());
                break;
            case RESERVA:
                ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(movimiento.getIdDestino());
                rd.removerCarta();
                break;
            case FOUNDATION:
                FoundationDeck fd = (FoundationDeck) foundation.buscaPosicion(movimiento.getIdDestino());
                fd.removerUltimaCarta();
                break;
        }

        switch(movimiento.getTipoOrigen()) 
        {
            case TABLEAU:
                TableauDeck td = (TableauDeck) tableau.buscaPosicion(movimiento.getIdOrigen());
                td.agregarBloqueCartasUndo(cartasADevolver);
                break;
            case RESERVA:
                ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(movimiento.getIdOrigen());
                rd.agregarCarta((CartaInglesa) cartasADevolver.getPrimerDato());
                break;
            case FOUNDATION:
                break;
        }
        return true;
    }
    
    private boolean puedeMoverDeTableauATableau(int posOrigen, int posDestino) 
    {
        if (posOrigen == posDestino) 
            return false;
        TableauDeck origen = (TableauDeck) tableau.buscaPosicion(posOrigen);
        TableauDeck destino = (TableauDeck) tableau.buscaPosicion(posDestino);
        if (origen == null || destino == null || origen.isEmpty()) 
            return false;
        
        int maxMovibles = getCeldasReservaLibres() + 1;
        ListaSimple<CartaInglesa> secuencia = origen.getSecuenciaValidaAlFinal();
        CartaInglesa baseSecuencia = (CartaInglesa) secuencia.getPrimerDato();
        
        if (secuencia.getTamano() <= maxMovibles && destino.sePuedeAgregarCarta(baseSecuencia)) 
            return true;
        
        if (secuencia.getTamano() > 1) 
        {
            CartaInglesa unaCarta = origen.getUltimaCarta();
            if (destino.sePuedeAgregarCarta(unaCarta))
                return true;
        }
        return false;
    }
    
    public String buscarPista() 
    {
        for (int i = 0; i < tableau.getTamano(); i++) 
        {
            TableauDeck td = (TableauDeck) tableau.buscaPosicion(i);
            if (!td.isEmpty()) 
            {
                CartaInglesa c = td.getUltimaCarta();
                FoundationDeck fd = (FoundationDeck) foundation.buscaPosicion(c.getPalo().ordinal());
                if (fd.sePuedeAgregarCarta(c))
                    return "PISTA: Mover " + c + " de Tableau 0" + (i+1) + " a Foundation.";
            }
        }
        for (int i = 0; i < reserva.getTamano(); i++) 
        {
            ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(i);
            if (!rd.isEmpty()) 
            {
                CartaInglesa c = rd.getCarta();
                FoundationDeck fd = (FoundationDeck) foundation.buscaPosicion(c.getPalo().ordinal());
                if (fd.sePuedeAgregarCarta(c))
                    return "PISTA: Mover " + c + " de Reserva 0" + (i+1) + " a Foundation.";
            }
        }

        for (int i = 0; i < tableau.getTamano(); i++)
            for (int j = 0; j < tableau.getTamano(); j++)
                if (i != j && puedeMoverDeTableauATableau(i, j))
                    return "PISTA: Mover cartas de Tableau 0" + (i+1) + " a Tableau 0" + (j+1) + ".";
        
        for (int i = 0; i < reserva.getTamano(); i++) {
            ReserveDeck rd = (ReserveDeck) reserva.buscaPosicion(i);
            if (!rd.isEmpty()) 
            {
                CartaInglesa c = rd.getCarta();
                for (int j = 0; j < tableau.getTamano(); j++) {
                    TableauDeck td = (TableauDeck) tableau.buscaPosicion(j);
                    if (td.sePuedeAgregarCarta(c))
                        return "PISTA: Mover " + c + " de Reserva 0" + (i+1) + " a Tableau 0" + (j+1) + ".";
                }
            }
        }
        
        int celdaLibre = getIndiceCeldaLibre();
        if (celdaLibre != -1)
            for (int i = 0; i < tableau.getTamano(); i++) 
            {
                TableauDeck td = (TableauDeck) tableau.buscaPosicion(i);
                if (!td.isEmpty())
                    return "PISTA: Mover " + td.getUltimaCarta() + " de Tableau 0" + (i+1) + " a Reserva 0" + celdaLibre + ".";
            }
        return null;
    }
    
    public boolean isJuegoBloqueado() 
    {
        return buscarPista() == null;
    }
    
    private int getIndiceCeldaLibre() 
    {
        for (int i = 0; i < reserva.getTamano(); i++)
            if (reserva.buscaPosicion(i).isEmpty())
                return i;
        return -1;
    }
    
    public int getCeldasReservaLibres()
    {
        int libres = 0;
        Nodo r = reserva.getInicio();
        while (r != null)
        {
            ReserveDeck rd = (ReserveDeck) r.getDato();
            if (rd.isEmpty()) 
                libres++;
            r = r.getSig();
        }
        return libres;
    }
    
    public void crearTableaux()
    {
        for (int i = 0; i < 8; i++) 
        {
            TableauDeck tableauDeck = new TableauDeck();
            tableau.insertarFin(tableauDeck);
        }
    }
    
    public void crearFoundations()
    {
        for(Palo palo : Palo.values())
            foundation.insertarFin(new FoundationDeck(palo));
    }
    
    public void crearReserva()
    {
        for (int i = 0; i < 8; i++)
            reserva.insertarFin(new ReserveDeck());
    }
    
    public ListaSimple getTableau()
    {
        return tableau;
    }
    
    public FoundationDeck getLastFoundationUpdated()
    {
        return lastFoundationUpdated;
    }
    
    public ListaSimple getFoundation() 
    {
        return foundation;
    }

    public ListaSimple getReserva() 
    {
        return reserva;
    }
}
