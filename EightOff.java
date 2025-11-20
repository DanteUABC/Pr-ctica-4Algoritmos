package eightoff;

public class EightOff 
{
    private Mazo mazo;
    private ListaSimple<TableauDeck> tableau = new ListaSimple();
    private ListaSimple<ReserveDeck> reserva = new ListaSimple();
    private ListaSimple<FoundationDeck> foundation = new ListaSimple();
    private ListaDoble<Movimiento> historial = new ListaDoble<>();
    private NodoDoble<Movimiento> punteroHistorial = null;
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
    
    private boolean ejecutarT2F(int posTableau) 
    {
        TableauDeck td = tableau.buscaPosicion(posTableau);
        if (td == null || td.isEmpty()) return false;
        CartaInglesa c = td.getUltimaCarta();
        FoundationDeck fd = foundation.buscaPosicion(c.getPalo().ordinal());
        if (fd.agregarCarta(c)) { 
            td.removerUltimaCarta();
            lastFoundationUpdated = fd;
            return true;
        }
        return false;
    }
    
    private boolean ejecutarT2R(int posTableau, int posReserva) 
    {
        TableauDeck td = tableau.buscaPosicion(posTableau);
        ReserveDeck rd = reserva.buscaPosicion(posReserva);
        if (td == null || rd == null || td.isEmpty() || !rd.isEmpty()) return false;
        CartaInglesa c = td.getUltimaCarta();
        if (rd.agregarCarta(c)) {
            td.removerUltimaCarta();
            return true;
        }
        return false;
    }
    
    private boolean ejecutarR2T(int posReserva, int posTableau) 
    {
        ReserveDeck rd = reserva.buscaPosicion(posReserva);
        TableauDeck td = tableau.buscaPosicion(posTableau);
        if (rd == null || td == null || rd.isEmpty()) 
            return false;
        CartaInglesa c = rd.getCarta();
        if (td.agregarCarta(c)) {
            rd.removerCarta(); 
            return true;
        }
        return false;
    }
    
    private boolean ejecutarR2F(int posReserva) 
    {
        ReserveDeck rd = reserva.buscaPosicion(posReserva);
        if (rd == null || rd.isEmpty()) 
            return false;
        CartaInglesa c = rd.getCarta();
        FoundationDeck fd = foundation.buscaPosicion(c.getPalo().ordinal());
        if (fd.agregarCarta(c)) 
        {
            rd.removerCarta(); 
            lastFoundationUpdated = fd;
            return true;
        }
        return false;
    }
    
    private boolean ejecutarMovimientoGuardado(Movimiento mov) 
    {
        if (mov == null) 
            return false;
        switch(mov.getTipoOrigen()) {
            case TABLEAU:
                switch(mov.getTipoDestino()) {
                    case TABLEAU:
                        TableauDeck origenT = tableau.buscaPosicion(mov.getIdOrigen());
                        TableauDeck destinoT = tableau.buscaPosicion(mov.getIdDestino());
                        if (destinoT.sePuedeAgregarCarta(mov.getCartasMovidas().getPrimerDato())) 
                        {
                           origenT.removerUltimasCartas(mov.getCartasMovidas().getTamano());
                           destinoT.agregarBloqueDeCartas(mov.getCartasMovidas());
                           return true;
                        }
                        return false;
                    case FOUNDATION:
                        return ejecutarT2F(mov.getIdOrigen());
                    case RESERVA:
                        return ejecutarT2R(mov.getIdOrigen(), mov.getIdDestino());
                }
                break;
            case RESERVA:
                 switch(mov.getTipoDestino()) {
                    case TABLEAU:
                        return ejecutarR2T(mov.getIdOrigen(), mov.getIdDestino());
                    case FOUNDATION:
                        return ejecutarR2F(mov.getIdOrigen());
                 }
                break;
        }
        return false;
    }
    
    public boolean moveTableauToTableau(int posOrigen, int posDestino) 
    {
        ListaSimple<CartaInglesa> cartasMovidas = getCartasMovidasT2T(posOrigen, posDestino);
        if (cartasMovidas == null) return false;

        TableauDeck origenT = tableau.buscaPosicion(posOrigen);
        TableauDeck destinoT = tableau.buscaPosicion(posDestino);
        origenT.removerUltimasCartas(cartasMovidas.getTamano());
        destinoT.agregarBloqueDeCartas(cartasMovidas);
        
        historial.cortarDesdeUnNodo(punteroHistorial);
        Movimiento mov = new Movimiento(TipoMazo.TABLEAU, posOrigen, TipoMazo.TABLEAU, posDestino, cartasMovidas);
        punteroHistorial = historial.insertarDespuesDeUnNodo(punteroHistorial, mov);
        return true;
    }
    
    public boolean moveTableauToFoundation(int posTableau) {
        TableauDeck td = tableau.buscaPosicion(posTableau);
        if (td == null || td.isEmpty()) return false;
        CartaInglesa c = td.getUltimaCarta();
        int cualFoundation = c.getPalo().ordinal();

        if (ejecutarT2F(posTableau)) 
        {
            historial.cortarDesdeUnNodo(punteroHistorial);
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.TABLEAU, posTableau, TipoMazo.FOUNDATION, cualFoundation, movidas);
            punteroHistorial = historial.insertarDespuesDeUnNodo(punteroHistorial, mov);
            return true;
        }
        return false;
    }
    
    public boolean moveReservaToFoundation(int posReserva) 
    {
        ReserveDeck rd = reserva.buscaPosicion(posReserva);
        if (rd == null || rd.isEmpty()) return false;
        CartaInglesa c = rd.getCarta();
        int cualFoundation = c.getPalo().ordinal();

        if (ejecutarR2F(posReserva)) 
        {
            historial.cortarDesdeUnNodo(punteroHistorial);
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.RESERVA, posReserva, TipoMazo.FOUNDATION, cualFoundation, movidas);
            punteroHistorial = historial.insertarDespuesDeUnNodo(punteroHistorial, mov);
            return true;
        }
        return false;
    }
    
    public boolean moveReservaToTableau(int posReserva, int posTableau) 
    {
        ReserveDeck rd = reserva.buscaPosicion(posReserva);
        if (rd == null || rd.isEmpty()) return false;
        CartaInglesa c = rd.getCarta();

        if (ejecutarR2T(posReserva, posTableau)) 
        {
            historial.cortarDesdeUnNodo(punteroHistorial);
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.RESERVA, posReserva, TipoMazo.TABLEAU, posTableau, movidas);
            punteroHistorial = historial.insertarDespuesDeUnNodo(punteroHistorial, mov);
            return true;
        }
        return false;
    }
    
    public boolean moveTableauToReserva(int posTableau, int posReserva) 
    {
        TableauDeck td = tableau.buscaPosicion(posTableau);
        if (td == null || td.isEmpty()) return false;
        CartaInglesa c = td.getUltimaCarta();

        if (ejecutarT2R(posTableau, posReserva)) {
            historial.cortarDesdeUnNodo(punteroHistorial);
            ListaSimple<CartaInglesa> movidas = new ListaSimple<>();
            movidas.insertarInicio(c);
            Movimiento mov = new Movimiento(TipoMazo.TABLEAU, posTableau, TipoMazo.RESERVA, posReserva, movidas);
            punteroHistorial = historial.insertarDespuesDeUnNodo(punteroHistorial, mov);
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
    
    public boolean deshacerMovimiento() {
        if (!canUndo()) {
            System.out.println("No hay movimientos que deshacer.");
            return false;
        }

        Movimiento mov = punteroHistorial.getInfo();
        ListaSimple<CartaInglesa> cartasADevolver = mov.getCartasMovidas();
        
        switch(mov.getTipoDestino()) {
            case TABLEAU:
                TableauDeck td = tableau.buscaPosicion(mov.getIdDestino());
                td.removerUltimasCartas(cartasADevolver.getTamano());
                break;
            case RESERVA:
                ReserveDeck rd = reserva.buscaPosicion(mov.getIdDestino());
                rd.removerCarta();
                break;
            case FOUNDATION:
                FoundationDeck fd = foundation.buscaPosicion(mov.getIdDestino());
                fd.removerUltimaCarta();
                break;
        }
        
        switch(mov.getTipoOrigen()) {
            case TABLEAU:
                TableauDeck td = tableau.buscaPosicion(mov.getIdOrigen());
                td.agregarBloqueCartasUndo(cartasADevolver); 
                break;
            case RESERVA:
                ReserveDeck rd = reserva.buscaPosicion(mov.getIdOrigen());
                rd.agregarCarta(cartasADevolver.getPrimerDato());
                break;
        }
        
        punteroHistorial = punteroHistorial.getAnt();
        return true;
    }
    
    public boolean rehacerMovimiento() {
        if (!canRedo()) {
            System.out.println("No hay movimientos que rehacer.");
            return false;
        }
        
        NodoDoble<Movimiento> nodoSiguiente = (punteroHistorial == null) 
                                                ? historial.getInicio() 
                                                : punteroHistorial.getSig();
        
        if (ejecutarMovimientoGuardado(nodoSiguiente.getInfo())) {
            punteroHistorial = nodoSiguiente;
            return true;
        }
        return false;
    }
    
    public void revertirAEstado(NodoDoble<Movimiento> estadoObjetivo) 
    {
        while (punteroHistorial != estadoObjetivo && canUndo()) {
            deshacerMovimiento();
        }
        while (punteroHistorial != estadoObjetivo && canRedo()) {
            rehacerMovimiento();
        }
    }
    
    public void cortarHistorialDesdePuntero() 
    {
        historial.cortarDesdeUnNodo(punteroHistorial);
    }
    
    public boolean canUndo() 
    {
        return punteroHistorial != null;
    }
    public boolean canRedo() {
        if (punteroHistorial == null) return !historial.isEmpty();
        return punteroHistorial.getSig() != null;
    }
    
    public Movimiento getMovimientoActual() {
        return (punteroHistorial == null) ? null : punteroHistorial.getInfo();
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
    
    private ListaSimple<CartaInglesa> getCartasMovidasT2T(int posOrigen, int posDestino) {
        TableauDeck origen = tableau.buscaPosicion(posOrigen);
        TableauDeck destino = tableau.buscaPosicion(posDestino);
        if (origen == null || destino == null || origen.isEmpty()) return null;
            
        int maxCartasMovibles = getCeldasReservaLibres() + 1;
        ListaSimple<CartaInglesa> secuencia = origen.getSecuenciaValidaAlFinal();
        if (secuencia.isEmpty()) return null;
        
        int tamSecuencia = secuencia.getTamano();
        CartaInglesa baseSecuencia = secuencia.getPrimerDato();
        
        if (tamSecuencia <= maxCartasMovibles && destino.sePuedeAgregarCarta(baseSecuencia)) {
            return secuencia;
        }
       
        if (tamSecuencia > 1) {
            CartaInglesa unaCarta = origen.getUltimaCarta();
            if (destino.sePuedeAgregarCarta(unaCarta)) {
                ListaSimple<CartaInglesa> unaCartaList = new ListaSimple<>();
                unaCartaList.insertarInicio(unaCarta);
                return unaCartaList;
            }
        }
        return null;
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
    
    public NodoDoble getPunteroHistorial()
    {
        return punteroHistorial;
    }
}
